package bitcoin.controllers

import akka.actor.{ActorSystem, Props}
import bitcoin.actors.address.MSG.Start
import bitcoin.actors.address.TranTrackActor
import bitcoin.actors.block.BlockTrackActor
import bitcoin.actors.block.BlockTrackActor.{ReplayBlock, StartBlockTrack}
import bitcoin.model.AddressTrans.AddressTransResult
import bitcoin.services.WsService
import cakesolutions.kafka.{KafkaProducerRecord, KafkaTopicPartition}
import gig.producer.GigProducer
import javax.inject.Inject
import play.api.Logger
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

object BitController{
  def props(wss:WsService):Props={
    Props(new TranTrackActor(wss))
  }
}
/**
  * For bitcoin.controllers in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
class BitController  @Inject()(cc: ControllerComponents, ws: WSClient, system:ActorSystem,wss:WsService) extends AbstractController(cc) {

  val producer = GigProducer.createProducer()
  val blockTrackActor= system.actorOf(BlockTrackActor.props(wss))

  def get(address:String) = Action.async { implicit request =>

    Logger.debug("Gig producer started:")
    val topicPartition = KafkaTopicPartition("bitcoin", 0)
    wss.getAddress(address)map{
      addressTrans=>addressTrans match {
        case Some(trans:AddressTransResult)=> trans.txs.map{
          tran=>producer.send(KafkaProducerRecord(topicPartition.topic(), None, tran.toString))
        }
          val trackActor = system.actorOf(BitController.props(wss))
          trackActor ! Start(trans.txs(0))
          //trackActor ! Start(trans.txs(10))
          //          trans.txs.map{
          //            tx=> trackActor ! Start(tx)
          //          }

          producer.flush()
          Ok("Success")
        case None =>
          Ok("Wrong address")
      }
    }


  }

  def getBlock(blockHash: String)= Action{
    blockTrackActor ! StartBlockTrack(blockHash,0)
    Ok("Success")
  }

  def replayBlock(number:Int) =Action{
    blockTrackActor ! ReplayBlock(number)
    Ok("Success")
  }
}
