package bitcoin.actors.block

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import bitcoin.actors.block.AccountActor.{Snap, SnapStart, TxRecord}

import scala.collection.mutable.Map
/**
  * For bitcoin.actors.block in bitsearch
  * Created by Steven T Zhou on 21/09/2018
  */
object AuditActor{
  def propsSnapActor(blockIndex:Int)={
    Props(new SnapActor(blockIndex))
  }
}
class AuditActor extends Actor with ActorLogging {
  val allAccount:Map[String,ActorRef] = Map()

  def handleTxRecord(tx: TxRecord)={
    allAccount.get(tx.address) match {
      case Some(actorRef) => actorRef ! tx
      case _=> val actorRef= context.actorOf(AccountActor.accountProps(tx.address),tx.address)
        allAccount(tx.address)=actorRef
        actorRef ! tx
    }
  }

  def handleSnap(snapStart:SnapStart)={
    val snapActor = context.actorOf(AuditActor.propsSnapActor(snapStart.index))
    for(record<-allAccount){
      record._2 ! Snap(snapStart.index,snapActor)
    }
  }

  def receive ={
    case msg:TxRecord=> handleTxRecord(msg)
    case msg:SnapStart => handleSnap(msg)
    case msg =>
      log.debug(msg.toString)
  }
}
