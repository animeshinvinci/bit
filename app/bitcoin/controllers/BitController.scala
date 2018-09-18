package bitcoin.controllers


import akka.actor.{ActorSystem, Props}
import bitcoin.actors.TrackMSG.Start
import bitcoin.actors.TranTrackActor
import bitcoin.model.AddressTrans.AddressTransResult
import bitcoin.services.WsService
import cakesolutions.kafka.{KafkaProducerRecord, KafkaTopicPartition}
import gig.producer.GigProducer
import javax.inject.Inject
import play.api.Logger
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global

object BitController{
  def props(ws:WSClient):Props={
    Props(new TranTrackActor(ws))
  }
}
/**
  * For bitcoin.controllers in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
class BitController  @Inject()(cc: ControllerComponents, ws: WSClient, system:ActorSystem,wss:WsService) extends AbstractController(cc) {

  val producer = GigProducer.createProducer()
  def get(address:String) = Action.async { implicit request =>

    Logger.debug("Gig producer started:")
    val topicPartition = KafkaTopicPartition("bitcoin", 0)
    ws.url(s" https://blockchain.info/rawaddr/$address").get().map{
      response=>val addressTrans= try{
        Some(response.body.parseJson.convertTo[AddressTransResult])
      }catch {
        case ex:Exception =>
          Logger.error(ex.getMessage)
          None
      }
      addressTrans match {
        case Some(trans:AddressTransResult)=> trans.txs.map{
          tran=>producer.send(KafkaProducerRecord(topicPartition.topic(), None, tran.toString))
        }
          val trackActor = system.actorOf(BitController.props(ws))
          trackActor ! Start(trans.txs(0))
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
}
