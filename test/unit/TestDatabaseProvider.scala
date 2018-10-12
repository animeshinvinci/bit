package unit
import bitcoin.db.provider.{AppDatabase, AppDatabaseProvider}
import com.outworkers.phantom.dsl._

/**
  * For  in bitsearch
  * Created by Steven T Zhou on 11/10/2018
  */
object TestConnector{
  val connector = ContactPoint.local
    .noHeartbeat()
    .keySpace(KeySpace("bit_search_test").ifNotExists()
      .option(replication eqs SimpleStrategy.replication_factor(1)))
}

object TestDataBase extends AppDatabase(TestConnector.connector)

trait TestDatabaseProvider extends AppDatabaseProvider{
  override def database: AppDatabase = TestDataBase
}
