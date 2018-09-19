package bitcoin.actors

import akka.actor.{Actor, ActorLogging, Props}
import bitcoin.actors.TrackMSG.{PreviousRecord, Record, Start, TrackInfo}
import bitcoin.model.Transaction.Trans
import bitcoin.services.WsService

/**
  * For bitcoin.actors.msg in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
object TranTrackActor {
  def props(wss:WsService): Props = {
    Props(new RetrieveActor(wss))
  }
}

class TranTrackActor(wss:WsService) extends Actor with ActorLogging {
  def startTraceTran(tran: Trans) = {
    val retrieveActor = context.actorOf(TranTrackActor.props(wss))
    val recordActor = context.actorOf(Props[RecordActor])
    recordActor ! Record(tran, TrackInfo(tran.txIndex.toString,0))
    tran.inputs map {
      inTrx =>
        inTrx.prevOut map {
          preOut =>
            preOut.addr match {
              case Some(address: String) => retrieveActor ! PreviousRecord(address, preOut.txIndex, recordActor,TrackInfo(tran.txIndex.toString,1))
                log.info(s"Send retrive work :$preOut")
            }
        }
    }
  }

  def receive = {
    case Start(tran) =>
      startTraceTran(tran)
      log.info(s"Start for trace: $tran")

    case msg =>
      log.debug(msg.toString)
  }
}
