package bitcoin.model.slim

import bitcoin.model.SnakifiedSprayJsonSupport

/**
  * For bitcoin.model.slim in bitsearch
  * Created by Steven T Zhou on 20/09/2018
  */
object Transaction extends SnakifiedSprayJsonSupport {

  case class PrevOut(addr: Option[String], value: Long)

  case class Input(prevOut: Option[PrevOut])

  case class Out( addr: Option[String], value: Long)

  case class Trans( inputs: Seq[Input],  out: Seq[Out], time: Long, txIndex: Long)

  implicit val prevoutFormat = jsonFormat2(PrevOut)
  implicit val inputFormat = jsonFormat1(Input)
  implicit val outFormat = jsonFormat2(Out)
  implicit val transFormat = jsonFormat4(Trans)
}
