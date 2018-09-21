package bitcoin.actors.block

import akka.actor.{Actor, ActorLogging, ReceiveTimeout}
import bitcoin.actors.block.AccountActor.Snap

import scala.concurrent.duration._

/**
  * For bitcoin.actors.block in bitsearch
  * Created by Steven T Zhou on 21/09/2018
  */
class SnapActor extends Actor with ActorLogging {
  var snap:Seq[Snap] = Seq()
  context.setReceiveTimeout(1000 milliseconds)
  def receive ={
    case msg:Snap => snap = snap :+ msg
    case ReceiveTimeout ⇒

    case msg =>
      log.debug(msg.toString)
  }
}
