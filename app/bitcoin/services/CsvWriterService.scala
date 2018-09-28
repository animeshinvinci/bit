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
    val trxSeq: Seq[Array[String]] = blockToIOPut(blockResult)
    CsvWriterService.writeToFile(csvWriter,trxSeq)
  }

  def handleBlockTxToCSV(blockResult:BlockTransResult,csvWriter: CSVWriter): Unit ={
    val trxSeq: Seq[Array[String]] = blockToTx(blockResult)
    CsvWriterService.writeToFile(csvWriter,trxSeq)
  }

  private def blockToTx(blockResult: BlockTransResult)={
    var trxSq = Seq[Array[String]]()
    blockResult.tx.map{
      tx=>
        val inputStr = tx.inputs match {
          case Nil=>""
          case input=> input.map{
            input =>input.prevOut match {
              case None=>""
              case Some(prevOut)=> prevOut.addr.getOrElse("") + ":" + prevOut.value
            }
          }.mkString(" ")
        }
        val outputStr = tx.out match {
          case Nil=>""
          case output=> output.map{
            out=>out.addr.getOrElse("") + ":" + out.value
          }.mkString(" ")
        }
      trxSq :+= Array(inputStr,outputStr,tx.time.toString)
    }
    trxSq
  }

  private def blockToIOPut(blockResult: BlockTransResult) = {
    var trxSeq = Seq[Array[String]]()
    blockResult.tx.map {
      tx =>
        tx.inputs.map {
          input =>
            input.prevOut.map {
              prevOut =>
                prevOut.addr.map {
                  _ => trxSeq :+= Array(prevOut.addr.getOrElse(""), "", prevOut.value.toString, tx.time.toString)
                }
            }
        }
        tx.out.map {
          out =>
            out.addr.map {
              _ => trxSeq :+= Array("", out.addr.getOrElse(""), out.value.toString, tx.time.toString)
            }
        }
    }
    trxSeq
  }
}
