package bitcoin.model.slim

import bitcoin.model.SnakifiedSprayJsonSupport
import bitcoin.model.slim.Transaction.Trans


/**
  * For bitcoin.model.slim in bitsearch
  * Created by Steven T Zhou on 20/09/2018
  */
object BlockTrans extends SnakifiedSprayJsonSupport {


  case class BlockTransResult(prevBlock: String, time: Long,   height: Int,  tx: Seq[Trans])

  implicit val blockTranFormat = jsonFormat4(BlockTransResult)
}
