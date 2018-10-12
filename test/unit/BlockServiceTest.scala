package unit

import bitcoin.db.service.BlockService
import bitcoin.db.table.BlockRecord
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import com.outworkers.phantom.dsl._

/**
  * For unit in bitsearch
  * Created by Steven T Zhou on 12/10/2018
  */
class BlockServiceTest extends FlatSpec with Matchers with BeforeAndAfterAll
  with ScalaFutures{
  val blockService = new BlockService with TestDatabaseProvider{}

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    blockService.database.create()
  }

  "block record " should " be saved in cassandra" in{
    val block1 = BlockRecord(11,"test info")
    val chain = blockService.storeBlock(block1) flatMap {
      _=> blockService.findByBlock(11)
    }
    whenReady(chain){
      case Some(result)=>result.info shouldEqual("test info")
      case re=> re shouldBe a  [BlockRecord]
    }
  }
}
