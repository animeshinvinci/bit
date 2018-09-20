package bitcoin.actors.address

import akka.actor.ActorRef
import bitcoin.model.thick.Transaction.Trans

object MSG {

  case class Start(start: Trans)

  case class TrackInfo(originAddress: String, tranNum :Int)

  case class PreviousRecord(address: String, trIndex: Long, recorder: ActorRef, trackInfo: TrackInfo)

  case class Record(tran: Trans, trackInfo: TrackInfo)

}
