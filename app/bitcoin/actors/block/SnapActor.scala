package bitcoin.actors.block

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import akka.actor.{Actor, ActorLogging, ReceiveTimeout}
import bitcoin.actors.block.AccountActor.SnapResult
import bitcoin.config.Const
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * For bitcoin.actors.block in bitsearch
  * Created by Steven T Zhou on 21/09/2018
  */
class SnapActor(blockIndex: Int) extends Actor with ActorLogging {
  lazy val snapFolder = Const.snapCacheLocation
  var snap:Seq[SnapResult] = Seq()
  context.setReceiveTimeout(1000 milliseconds)
  def receive ={
    case msg:SnapResult => snap = snap :+ msg
    case ReceiveTimeout ⇒
        handleSnapshotTimeout

    case msg =>
      log.debug(msg.toString)
  }

  private def handleSnapshotTimeout() = {
    log.info(s"Take Snap for : $blockIndex:")
    Files.write(Paths.get(snapFolder+blockIndex), snap.toString.getBytes(StandardCharsets.UTF_8))
    context.stop(self)
  }
}
