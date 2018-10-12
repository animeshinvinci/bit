package bitcoin.db.table

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.keys.{PartitionKey}


/**
  * For bitcoin.db in bitsearch
  * Created by Steven T Zhou on 11/10/2018
  */

case class BlockRecord(
                  block: Int,
                  info: String
                )

abstract class BlockTable extends Table[BlockTable,BlockRecord]{
    object block extends IntColumn with PartitionKey
    object info extends StringColumn

    def findByBlock(block :Int)={
      select.where(_.block eqs(block)).one()
    }
}
