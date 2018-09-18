package bitcoin.actors

import akka.actor.{Actor, ActorLogging, Props}
import bitcoin.actors.TrackMSG.{PreviousRecord, Record, Start}
import bitcoin.model.Transaction.Trans
import play.api.libs.ws.WSClient

/**
  * For bitcoin.actors.msg in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
object TranTrackActor{
  def props(ws:WSClient):Props={
    Props(new RetrieveActor(ws))
  }
}

class TranTrackActor(ws: WSClient) extends Actor with ActorLogging {

  var records: Seq[Trans] = Seq()
  val retrieveActor = context.actorOf(TranTrackActor.props(ws))

  def startTraceTran(tran: Trans) = {
    tran.inputs map {
      inTrx =>
        inTrx.prevOut map {
          preOut =>
            preOut.addr match {
              case Some(address: String) => retrieveActor ! PreviousRecord(address, preOut.txIndex, self)
                log.info(s"Send retrive work :$preOut")
            }
        }
    }
  }

  def receive = {
    case Start(tran) =>
      records = records :+ tran
      startTraceTran(tran)
      log.info(s"Start for trace: $tran")
    case Record(tran) =>
      records = records :+ tran
      log.info(s"Record length: ${records.length}")
      log.info(s"all record:$records")
    case msg =>
      log.debug(msg.toString)
  }
}
