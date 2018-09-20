package bitcoin.model.slim

import bitcoin.model.SnakifiedSprayJsonSupport


/**
  * For bitcoin.model.slim in bitsearch
  * Created by Steven T Zhou on 20/09/2018
  */
object BlockTrans extends SnakifiedSprayJsonSupport {

  import bitcoin.model.thick.Transaction._

  case class BlockTransResult(prevBlock: String, time: Long,   height: Int,  tx: Seq[Trans])

  implicit val blockTranFormat = jsonFormat4(BlockTransResult)
}
