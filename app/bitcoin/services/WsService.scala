package bitcoin.services


import bitcoin.model.AddressTrans.AddressTransResult
import javax.inject.Inject
import play.api.Logger
import play.api.libs.ws.WSClient
import spray.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WsService @Inject()(ws: WSClient,cache:CacheService) {
  def getAddress(address: String) =
  cache.get(address).flatMap{
    result => result match {
      case Some(value:String)=> Future(try{
        Logger.info(s"Using Cache value for $address")
        Logger.info("*****************************************************************************************")
        Some(value.parseJson.convertTo[AddressTransResult])
      }catch {
        case ex: Exception =>
          Logger.error(ex.getMessage)
          Logger.error(s"Fail to get the Json Format : ${value}")
          None
      })
      case None =>ws.url(s" https://blockchain.info/rawaddr/$address").get().map {
        response =>
          val addressTrans = try {
            Logger.info(s"Using ws Value for $address")
            val re =Some(response.body.parseJson.convertTo[AddressTransResult])
            cache.save(address,response.body)
            re
          } catch {
            case ex: Exception =>
              Logger.error(ex.getMessage)
              Logger.error(s"Fail to get the Json Format: ${response.body}")
              None
          }
          addressTrans
      }
    }
  }
}
