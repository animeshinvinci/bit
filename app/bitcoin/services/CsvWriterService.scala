package bitcoin.services

import java.io.{BufferedWriter, FileWriter}
import bitcoin.config.Const
import bitcoin.model.slim.BlockTrans.BlockTransResult
import com.opencsv.CSVWriter

/**
  * For bitcoin.services in bitsearch
  * Created by Steven T Zhou on 25/09/2018
  */
object CsvWriterService {
  lazy val cacheFolder = Const.snapCacheLocation

  def writeToFile(fileName: String, content: Seq[Array[String]]) = {
    val outputFile = new BufferedWriter(new FileWriter(cacheFolder+ fileName))
    val csvWriter = new CSVWriter(outputFile)
    content.map {
      record => csvWriter.writeNext(record)
    }
    csvWriter.close()
  }

  def openFile(fileName:String):CSVWriter ={
    val outputFile = new BufferedWriter(new FileWriter(cacheFolder+ fileName))
    new CSVWriter(outputFile)
  }

  def writeToFile(csvWriter: CSVWriter, content: Seq[Array[String]]): Unit ={
    content.map(record=>csvWriter.writeNext(record))
  }

  def closeFile(csvWriter: CSVWriter)={
    csvWriter.close()
  }

  def handleBlockToCSV(blockResult:BlockTransResult,csvWriter: CSVWriter): Unit ={
    var trxSeq = Seq[Array[String]]()
    blockResult.tx.map{
      tx=>
        tx.inputs.map {
          input =>
            input.prevOut.map {
              prevOut =>
                prevOut.addr.map {
                  _ => trxSeq:+= Array(prevOut.addr.getOrElse(""),"",prevOut.value.toString,tx.time.toString)
                }
            }
        }
        tx.out.map {
          out =>
            out.addr.map {
              _ =>trxSeq :+= Array("",out.addr.getOrElse(""),out.value.toString,tx.time.toString)
            }
        }
    }
    CsvWriterService.writeToFile(csvWriter,trxSeq)
  }
}
