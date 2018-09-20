package bitcoin.actors.address

import akka.actor.{Actor, ActorLogging}
import MSG.Record

class RecordActor  extends Actor with ActorLogging {
  var records: Seq[Record] = Seq()
  def receive = {
    case record:Record =>
      records = records :+ record
      log.info(s"Record length: ${records.length}")
      log.info(s"Record received: $record")
    case msg =>
      log.debug(msg.toString)
  }
}
