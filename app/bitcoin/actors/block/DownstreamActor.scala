package bitcoin.actors.block

import akka.actor.{Actor, ActorLogging, Props}
import bitcoin.actors.block.AccountActor.{Snap, SnapStart, TxIn, TxOut}
import bitcoin.model.slim.BlockTrans.BlockTransResult
import cakesolutions.kafka.akka.ConsumerRecords
import gig.msg.ConsumerMsg.GigAckOffset

import scala.collection.JavaConverters._
import spray.json._

/**
  * For bitcoin.actors.block in bitsearch
  * Created by Steven T Zhou on 21/09/2018
  */
class DownstreamActor extends Actor with ActorLogging {

  val auditActor = context.actorOf(Props[AuditActor])

  def handleBlockTrans(blockTransResult: BlockTransResult): Unit = {
    blockTransResult.tx.map {
      tx =>
        tx.inputs.map {
          input =>
            input.prevOut.map {
              prevOut =>
                prevOut.addr.map {
                  addr => auditActor ! TxOut(prevOut.addr.get, prevOut.value)
                }
            }
        }
        tx.out.map {
          out =>
            out.addr.map {
              addr => auditActor ! TxIn(out.addr.get, out.value)
            }
        }
    }
  }

  override def preStart() = log.info("Yo, start  new downstream receiver ")

  def receive = {
    case ConsumerRecords(offsets, records) =>
      log.info(offsets.toString())
      records.partitions().asScala map {
        partition =>
          for (ele <- records.records(partition).asScala.zipWithIndex) {
            val blockResult = ele._1.value().toString.parseJson.convertTo[BlockTransResult]
            handleBlockTrans(blockResult)
            if(blockResult.height >0 && blockResult.height %100 ==0){


              auditActor ! SnapStart(blockResult.height)
              log.info(s"Taking Snap shot in Block :${blockResult.height}")
            }
          }
      }
      sender() ! GigAckOffset(offsets)
  }
}
