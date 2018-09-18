package bitcoin.services


import bitcoin.model.AddressTrans.AddressTransResult
import javax.inject.Inject
import play.api.Logger
import play.api.libs.ws.WSClient
import spray.json._
import scala.concurrent.ExecutionContext.Implicits.global

class WsService @Inject()(ws: WSClient) {
  def getAddress(address: String) = {
    ws.url(s" https://blockchain.info/rawaddr/$address").get().map {
      response =>
        val addressTrans = try {
          Some(response.body.parseJson.convertTo[AddressTransResult])
        } catch {
          case ex: Exception =>
            Logger.error(ex.getMessage)
            None
        }
        addressTrans
    }
  }
}

object WsService{

}
