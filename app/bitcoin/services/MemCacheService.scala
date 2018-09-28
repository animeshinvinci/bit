package bitcoin.services

import javax.inject.Inject
import play.api.cache.AsyncCacheApi
import scala.concurrent.Future

/**
  * For bitcoin.services in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/18
  */
class MemCacheService@Inject()(cache: AsyncCacheApi) extends CacheService {
  override def get(key: String): Future[Option[String]] = cache.get(key)

  override def save(key: String, item: String): Unit = cache.set(key,item)

  override def remove(key: String): Unit = cache.remove(key)
}
