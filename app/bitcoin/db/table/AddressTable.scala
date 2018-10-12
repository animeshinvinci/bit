package bitcoin.db.table

import com.outworkers.phantom.Table
import com.outworkers.phantom.keys.{PartitionKey, PrimaryKey}

/**
  * For bitcoin.db.provider in bitsearch
  * Created by Steven T Zhou on 11/10/2018
  */
case class AddrRecord(
                     addr:String,
                     tr_index:String,
                     tx:String
                     )

abstract class AddressTable extends  Table[AddressTable,AddrRecord]{
  object addr extends StringColumn with PartitionKey
  object tr_index extends  StringColumn with PrimaryKey
  object tx extends StringColumn

}
