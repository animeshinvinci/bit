package bitcoin.actors.block

import akka.actor.{Actor, ActorLogging, Props}
import bitcoin.actors.block.AccountActor.{Snap, SnapResult, TxIn, TxOut}

/**
  * For bitcoin.actors.block in bitsearch
  * Created by Steven T Zhou on 21/09/2018
  */
object AccountActor {

  trait TxRecord{def address: String}

  case class TxIn(val address: String, number: Long)extends TxRecord

  case class TxOut(val address: String, number: Long)extends TxRecord

  case class Snap(index: Int)

  case class SnapResult(txIn: Long, txOut: Long, index: Int, address: String)

  def accountProps(address: String) = {
    Props(new AccountActor(address))
  }
}

class AccountActor(address: String) extends Actor with ActorLogging {
  var txIn: Long = 0
  var txOut: Long = 0

  def receive = {
    case TxIn(_, number) => txIn += number
    case TxOut(_, number) => txOut += number
    case Snap(index) => sender() ! SnapResult(txIn, txOut, index, address)
    case msg =>
      log.debug(msg.toString)
  }
}
