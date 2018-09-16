package controllers

import bitcoin.model.AddressTrans.AddressTransResult
import bitcoin.model.BlockTrans.BlockTransResult
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import bitcoin.model.Transaction._
import spray.json._

import scala.io.Source

/**
  * For controllers in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
class JsonModelSpec extends PlaySpec with GuiceOneAppPerTest {

  "Transaction Json " should {
    "converted to Trans Class" in {
      val transJson = Source.fromFile("./conf/test/tran.data").getLines.mkString.parseJson
      val trans = transJson.convertTo[Trans]
      trans.hash must include("b6f6991d03df0e2e04dafffcd6bc418aac66049e2cd74b80f14ac86db1e3f0da")
    }
  }

  "AddressTran Json " should {
    "converted to addressTranResult Class" in {
      val addressTranJson = Source.fromFile("./conf/test/addressTran.data").getLines.mkString.parseJson
      val addressTran = addressTranJson.convertTo[AddressTransResult]
      addressTran.hash160 must include("660d4ef3a743e3e696ad990364e555c271ad504b")
    }
  }

  "Block Json " should {
    "converted to blockTranResult Class" in {
      val blockTranJson = Source.fromFile("./conf/test/blockTran.data").getLines.mkString.parseJson
      val blockTran = blockTranJson.convertTo[BlockTransResult]
      blockTran.hash must include("0000000000000bae09a7a393a8acded75aa67e46cb81f7acaa5ad94f9eacd103")
    }
  }

}
