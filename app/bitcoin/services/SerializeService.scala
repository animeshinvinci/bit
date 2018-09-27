package bitcoin.services

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import bitcoin.services.BlockNumberCacheService.cacheFileName

/**
  * For bitcoin.services in bitsearch
  * Created by Steven T Zhou on 27/09/2018
  */
trait SerializeService {
  def Serialize(fileName:String,content:Any)={
    val fos = new FileOutputStream(cacheFileName)
    val oos = new ObjectOutputStream(fos)
    oos.writeObject(content)
    oos.close()
  }

  def read[T](fileName:String)={
    val fis = new FileInputStream(fileName)
    val ois = new ObjectInputStream(fis)
    val re = ois.readObject
    ois.close()
    re
  }
}
