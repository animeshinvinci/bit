package bitcoin.db.provider

import com.outworkers.phantom.dsl._

/**
  * For bitcoin.db.provider in bitsearch
  * Created by Steven T Zhou on 11/10/2018
  */

object RConnector{
  val connector = ContactPoint.local
    .noHeartbeat()
    .keySpace(KeySpace("bit_search").ifNotExists()
      .option(replication eqs SimpleStrategy.replication_factor(1))
    )
}
object RDatabase extends AppDatabase(RConnector.connector)

trait RDatabaseProvider extends AppDatabaseProvider{
  override def database: AppDatabase = RDatabase
}
