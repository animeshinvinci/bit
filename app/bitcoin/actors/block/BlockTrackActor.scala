package bitcoin.actors.block

import akka.actor.{Actor, ActorLogging, Props}
import bitcoin.actors.block.BlockTrackActor.{BlockTrack, StartBlockTrack}
import bitcoin.services.WsService
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * For bitcoin.actors.block in bitsearch
  * Created by Steven T Zhou on 20/09/2018
  */
object BlockTrackActor {


  case class StartBlockTrack(blockHash: String, trackTo: Int)

  case class BlockTrack(blockHash: String, retry: Int)

  def props(wss: WsService): Props = {
    Props(new BlockTrackActor(wss))
  }
}

class BlockTrackActor(wss: WsService) extends Actor with ActorLogging {
  var tracedTo: Int = 0
  val maxTry: Int = 5

  def getBlockRecord(blockTrack: BlockTrack) = {
    if(blockTrack.retry == 0){
      log.info(s"Retried failed for : ${blockTrack.blockHash}")
    }
    wss.getSlimBlock(blockTrack.blockHash) map {
      blockResult =>
        blockResult match {
          case Some(blockResult) =>
                if(blockResult.height> tracedTo){
                  self ! BlockTrack(blockResult.prevBlock,maxTry)
                }else{
                  log.info(s"Traced to ${blockResult.height}")
                }

          case _ =>
            log.error(s"Times retry remain: ${blockTrack.retry-1}")
            self ! BlockTrack(blockTrack.blockHash,blockTrack.retry-1)
        }
    }
  }

  def receive = {
    case blockTrack@BlockTrack(_, _) =>
      getBlockRecord(blockTrack)
    case StartBlockTrack(blockHash,trackNum)=>
      tracedTo = trackNum
      getBlockRecord(BlockTrack(blockHash,maxTry))
    case msg =>
      log.debug(msg.toString)
  }
}
