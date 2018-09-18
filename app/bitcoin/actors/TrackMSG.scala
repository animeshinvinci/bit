package bitcoin.actors

import akka.actor.ActorRef
import bitcoin.model.Transaction.Trans

object TrackMSG {
  case class Start(start:Trans)
  case class PreviousRecord(address: String, trIndex :Long, recorder: ActorRef)
  case class Record(tran: Trans)
}
