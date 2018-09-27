package bitcoin.config

import scala.reflect.io.File

/**
  * For bitcoin in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/24
  */
object Const {
  val cacheLocation ="../cache/"
  val blocknumbercache = "blocknumbermap.txt"

  lazy val snapCacheLocation = cacheLocation + "snap/"
  lazy val cacheFolder = if(File(snapCacheLocation).exists){
    snapCacheLocation
  }else{
    File(snapCacheLocation).createDirectory()
    snapCacheLocation
  }
}
