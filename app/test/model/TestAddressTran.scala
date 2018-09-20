package test.model

import bitcoin.model.AddressTrans.AddressTransResult
import bitcoin.model.thick.Transaction.Trans
import spray.json._

import scala.io.Source
/**
  * For test.model in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
object TestAddressTran {
  def main(args: Array[String]): Unit = {
    val addressTranJson =  Source.fromFile("./conf/test/addressTran.data").getLines.mkString.parseJson
    val addressTran = {
      addressTranJson.convertTo[AddressTransResult]
    }
    println(addressTran.hash160)
  }
}
