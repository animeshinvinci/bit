package bitcoin.model

/**
  * For bitcoin.model in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
object BlockTrans extends SnakifiedSprayJsonSupport {

  import Transaction._

  case class BlockTransResult(hash: String, ver: Int, prevBlock: String, mrklRoot: String,
                              time: Long, bits: Long, fee: Long, nonce: Long,
                              nTx: Int, size: Int, blockIndex: Int, mainChain: Boolean,
                              height: Int, receivedTime: Long, relayedBy: String, tx: Seq[Trans])

  implicit val blockTranFormat = jsonFormat16(BlockTransResult)
}
