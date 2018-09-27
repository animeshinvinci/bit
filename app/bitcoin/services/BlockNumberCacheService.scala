package bitcoin.services

import bitcoin.config.Const
import scala.collection.mutable.Map
import scala.reflect.io.File


/**
  * For bitcoin.services in bitsearch
  * Created by Steven T Zhou on 26/09/2018
  */
object BlockNumberCacheService extends SerializeService {

  lazy val cacheFileName: String = Const.snapCacheLocation + Const.blocknumbercache
  lazy val blockNumberMap: Map[Int,String]=getMap()

  private def getMap(): Map[Int, String] = {
    File(cacheFileName).exists match {
      case true => read(cacheFileName).asInstanceOf[Map[Int,String]]
      case _ => Map()
    }
  }

  def saveMap() = {
    Serialize(cacheFileName,blockNumberMap)
  }
}
