package bitcoin.services

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import bitcoin.config.Const

import scala.concurrent.Future
import scala.io.Source
import scala.reflect.io.File
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * For bitcoin.services in gen
  * Created by Steven T Zhou on 19/09/2018
  */
class FileCacheService extends CacheService {
  lazy val cacheLocation = Const.cacheLocation
  lazy val cacheFolder = if(File(cacheLocation).exists){
    cacheLocation
  }else{
    File(cacheLocation).createDirectory()
    cacheLocation
  }
  override def get(key: String): Future[Option[String]] = File(cacheFolder + key).exists match {
    case true => Future(Some(Source.fromFile(cacheFolder + key).getLines.mkString))
    case _ => Future(None)
  }

  override def save(key: String, item: String): Unit = {
    Files.write(Paths.get(cacheFolder + key), item.getBytes(StandardCharsets.UTF_8))
  }
}
