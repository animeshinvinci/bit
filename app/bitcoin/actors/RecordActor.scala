package bitcoin.actors

import akka.actor.{Actor, ActorLogging}
import bitcoin.actors.TrackMSG.Record
import bitcoin.model.Transaction.Trans

class RecordActor  extends Actor with ActorLogging {
  var records: Seq[Trans] = Seq()
  def receive = {
    case Record(tran) =>
      records = records :+ tran
      log.info(s"Record length: ${records.length}")
    case msg =>
      log.debug(msg.toString)
  }
}
