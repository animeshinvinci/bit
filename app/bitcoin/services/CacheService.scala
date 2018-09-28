package bitcoin.services

import scala.concurrent.Future

/**
  * For bitcoin.services in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/18
  */
trait CacheService {
  def get(key: String): Future[Option[String]]

  def save(key: String, item: String): Unit

  def remove(key:String)
}
