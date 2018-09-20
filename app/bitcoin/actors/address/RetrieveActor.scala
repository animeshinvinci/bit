package bitcoin.actors.address

import akka.actor.{Actor, ActorLogging, ActorRef}
import MSG.{PreviousRecord, Record, TrackInfo}
import bitcoin.model.AddressTrans.AddressTransResult
import bitcoin.services.WsService

import scala.concurrent.ExecutionContext.Implicits.global

class RetrieveActor(wss: WsService) extends Actor with ActorLogging {
  def getTranPreviousOut(address: String, trIndex: Long, recorder: ActorRef, trackInfo: TrackInfo) = {
    log.info(s"Start Get Address:$address")
    wss.getAddress(address) map {
      addressTrans =>
        addressTrans match {
          case Some(trans: AddressTransResult) => trans.txs.filter(tx => tx.txIndex == trIndex).map {
            tx =>
              recorder ! Record(tx, trackInfo)
              tx.inputs.map {
                inTrx =>
                  inTrx.prevOut map {
                    preOut =>
                      preOut.addr match {
                        case Some(address: String) => self ! PreviousRecord(address, preOut.txIndex, recorder, TrackInfo(trackInfo.originAddress, trackInfo.tranNum + 1))
                          log.info(s"RETRIEVE BEFORE : $preOut")
                        case None => log.info(s"No preOut need for : $preOut")
                      }
                  }
              }
          }
          case _=>
        }
    }
  }

  def receive = {
    case PreviousRecord(address: String, trIndex: Long, recorder: ActorRef, trackInfo) =>
      getTranPreviousOut(address, trIndex, recorder, trackInfo)
    case msg =>
      log.debug(msg.toString)
  }
}
