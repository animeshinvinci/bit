package bitcoin.services

import bitcoin.db.provider.RDatabaseProvider
import bitcoin.db.service.BlockService
import scala.language.reflectiveCalls

/**
  * For bitcoin.db.service in bitsearch
  * Created by Steven T Zhou on 11/10/2018
  */
class DBService extends BlockService with RDatabaseProvider {

}
