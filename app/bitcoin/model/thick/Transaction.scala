package bitcoin.model.thick

import bitcoin.model.SnakifiedSprayJsonSupport

/**
  * For bitcoin.model in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */

object Transaction extends SnakifiedSprayJsonSupport {

  case class PrevOut(spent: Boolean, txIndex: Long, addr: Option[String], value: Long, n: Int, script: String)

  case class Input(sequence: Long, witness: String, prevOut: Option[PrevOut], script: String)

  case class Out(spent: Boolean, txIndex: Long, addr: Option[String], value: Long, n: Int, script: String)

  case class Trans(ver: Int, inputs: Seq[Input], weight: Int, blockHeight: Option[Int], relayedBy: String, out: Seq[Out],
                   lockTime: Int, size: Int, doubleSpend:Option[Boolean], time: Long, txIndex: Long, vinSz: Int,
                   hash: String, voutSz: Int)

  implicit val prevoutFormat = jsonFormat6(PrevOut)
  implicit val inputFormat = jsonFormat4(Input)
  implicit val outFormat = jsonFormat6(Out)
  implicit val transFormat = jsonFormat14(Trans)
}