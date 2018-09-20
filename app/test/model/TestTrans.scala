package test.model

import bitcoin.model.thick.Transaction.Trans
import bitcoin.model.thick.Transaction._
import spray.json._

import scala.io.Source
/**
  * For model in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
object TestTrans {
  def main(args: Array[String]): Unit = {
    val transJson =  Source.fromFile("./conf/test/tran.data").getLines.mkString.parseJson
    val trans = {
      transJson.convertTo[Trans]
    }
    println(trans.hash)
  }
}
