package bitcoin.actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import bitcoin.actors.TrackMSG.{PreviousRecord, Record, Start}
import bitcoin.model.AddressTrans.AddressTransResult
import play.api.Logger

import play.api.libs.ws.WSClient
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global

class RetrieveActor(ws: WSClient) extends Actor with ActorLogging {

  def getTranPreviousOut(address: String, trIndex :Long, recorder: ActorRef)={
    log.info(s"Start Get Address:$address")
    ws.url(s" https://blockchain.info/rawaddr/$address").get().map{
      response=>
        log.info(s"End get address : $address")

        val addressTrans= try{
        Some(response.body.parseJson.convertTo[AddressTransResult])
      }catch {
        case ex:Exception =>
          Logger.error(ex.getMessage)
          None
      }
        addressTrans match {
          case Some(trans:AddressTransResult)=> trans.txs.filter(tx=> tx.txIndex == trIndex).map{
            tx=>recorder ! Record(tx)
              tx.inputs.map{
              inTrx=>
                inTrx.prevOut map{
                preOut=>
                  preOut.addr match {
                  case Some(address:String) => self ! PreviousRecord(address,preOut.txIndex,recorder)
                      log.info(s"RETRIEVE BEFORE : $preOut")
                  case None=> log.info(s"No preOut need for : $preOut")
                }
              }
            }
          }
        }
    }
  }


  def receive = {
    case PreviousRecord(address: String, trIndex :Long, recorder: ActorRef) =>
        getTranPreviousOut(address,trIndex,recorder)
    case msg =>
      log.debug(msg.toString)
  }
}
