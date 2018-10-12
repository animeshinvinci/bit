package bitcoin.db.service

import bitcoin.db.provider.AppDatabaseProvider
import bitcoin.db.table.BlockRecord
import com.outworkers.phantom.dsl._



/**
  * For bitcoin.db.service in bitsearch
  * Created by Steven T Zhou on 11/10/2018
  */
trait BlockService extends  AppDatabaseProvider{
  def storeBlock(block:BlockRecord)={
    db.blockTable.store(block).future()
  }

  def findByBlock(blockId:Int) = {
    db.blockTable.findByBlock(blockId)
  }
}
