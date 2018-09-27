package bitcoin.services

import bitcoin.model.AddressTrans.AddressTransResult
import bitcoin.model.slim.BlockTrans.BlockTransResult
import javax.inject.Inject
import play.api.Logger
import play.api.libs.ws.WSClient
import spray.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WsService @Inject()(ws: WSClient, cache: CacheService) {
  private def getFromEndpoint(endpoint: String, address: String) = {
    cache.get(address).flatMap {
      result =>
        result match {
          case Some(value: String) =>
            Logger.info(s"Get value from cache: $address")
            Future(value)
          case None => ws.url(endpoint + address).get().map {
            response => response.body
          }
        }
    }
  }

  private def tryParseAndCache[T](address: String, value: String, fun: String => T) = {
    try {
      val re = Some(fun(value))
      re
    } catch {
      case ex: Exception =>
        Logger.error(ex.getMessage)
        Logger.error(s"Fail to get the Json Format : ${value}")
        None
    }
  }

  def getAddress(address: String) = {
    getFromEndpoint("https://blockchain.info/rawaddr/", address) map {
      value =>
        cache.save(address,value)
        tryParseAndCache(address, value, value => value.parseJson.convertTo[AddressTransResult])
    }
  }

  def getSlimBlock(address: String) = {
    getFromEndpoint("https://blockchain.info/rawblock/", address) map {
      value =>
        val va =tryParseAndCache(address, value, value => value.parseJson.convertTo[BlockTransResult])
        cache.save(address,va.get.toJson.toString())
        va
    }
  }

  def getSlimBlockString(address:String) ={
    getFromEndpoint("https://blockchain.info/rawblock/", address)
  }
}
