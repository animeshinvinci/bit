package bitcoin.actors.block

import akka.actor.{Actor, ActorLogging, Props}
import bitcoin.actors.block.BlockTrackActor.{BlockTrack, ReplayBlock, StartBlockTrack}
import bitcoin.services.{CsvWriterService, WsService}
import cakesolutions.kafka.{KafkaProducerRecord, KafkaTopicPartition}
import com.opencsv.CSVWriter
import gig.producer.GigProducer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.Map
import spray.json._

/**
  * For bitcoin.actors.block in bitsearch
  * Created by Steven T Zhou on 20/09/2018
  */
object BlockTrackActor {


  case class StartBlockTrack(blockHash: String, trackTo: Int)

  case class BlockTrack(blockHash: String, retry: Int)

  case class ReplayBlock(number: Int)

  def props(wss: WsService): Props = {
    Props(new BlockTrackActor(wss))
  }
}

class BlockTrackActor(wss: WsService) extends Actor with ActorLogging {
  var tracedTo: Int = 0
  val maxTry: Int = 5
  val recorder: Map[Int, String] = Map()
  val producer = GigProducer.createProducer()
  val topicPartitionBlock = KafkaTopicPartition("bitcoinBlock", 0)
  var csvWriter: CSVWriter = _


  def getBlockRecord(blockTrack: BlockTrack) = {
    if (blockTrack.retry == 0) {
      log.info(s"Retried failed for : ${blockTrack.blockHash}")
    }
    wss.getSlimBlock(blockTrack.blockHash) map {
      blockResult =>
        blockResult match {
          case Some(blockResult) =>
            CsvWriterService.handleBlockToCSV(blockResult, csvWriter)
            recorder(blockResult.height) = blockResult.toJson.toString()
            if (blockResult.height > tracedTo) {
              self ! BlockTrack(blockResult.prevBlock, maxTry)
            } else {
              log.info(s"Traced to ${blockResult.height}")
              csvWriter.close()
            }

          case _ =>
            log.error(s"Times retry remain: ${blockTrack.retry - 1}")
            self ! BlockTrack(blockTrack.blockHash, blockTrack.retry - 1)
        }
    }
  }

  def handleReplay(number: Int) = {
    (0 to number) map {
      i =>
        recorder.get(i).map {
          blockResult =>
            producer.send(KafkaProducerRecord(topicPartitionBlock.topic(), None, blockResult))
            log.info(s"Send block record to Kafka blockID:$i")
        }
    }
    producer.flush()
  }

  def receive = {
    case blockTrack@BlockTrack(_, _) =>
      getBlockRecord(blockTrack)
    case StartBlockTrack(blockHash, trackNum) =>
      tracedTo = trackNum
      csvWriter = CsvWriterService.openFile("trans.csv")
      getBlockRecord(BlockTrack(blockHash, maxTry))
    case ReplayBlock(number) => handleReplay(number)
    case msg =>
      log.debug(msg.toString)
  }
}
