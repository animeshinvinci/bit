package bitcoin.db.provider

import bitcoin.db.table.{AddrRecord, AddressTable, BlockRecord, BlockTable}
import com.outworkers.phantom.builder.query.CreateQuery.Default
import com.outworkers.phantom.connectors.{CassandraConnection, KeySpace}
import com.outworkers.phantom.database.Database

/**
  * For bitcoin.db.provider in bitsearch
  * Created by Steven T Zhou on 11/10/2018
  */
class AppDatabase (override val connector: CassandraConnection)
extends Database[AppDatabase](connector){
  object blockTable extends BlockTable with Connector{
    override def autocreate(keySpace: KeySpace): Default[BlockTable, BlockRecord] = {
      create.ifNotExists()(keySpace)
    }
  }
  object addresses extends  AddressTable with Connector{
    override def autocreate(keySpace: KeySpace): Default[AddressTable, AddrRecord] = {
      create.ifNotExists()(keySpace)
    }
  }
}
