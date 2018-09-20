package bitcoin.model

/**
  * For bitcoin.model in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
object AddressTrans extends SnakifiedSprayJsonSupport {

  import bitcoin.model.thick.Transaction._

  case class AddressTransResult(hash160: String, address: String, nTX: Int, totalReceived: Long,
                                totalSent: Long, finalBalance: Long, txs: Seq[Trans])

  implicit val addressTranFormat = jsonFormat7(AddressTransResult)
}
