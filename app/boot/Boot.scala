package boot

import akka.actor.ActorSystem
import cakesolutions.kafka.testkit.KafkaServer
import gig.constent.GigConfig
import javax.inject.Inject
import play.{Application, Play}
import play._
import play.api.inject.ApplicationLifecycle
import play.api.Logger

import scala.concurrent.Future

/**
  * For boot in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
class Boot  @Inject() (lifecycle: ApplicationLifecycle)  {

  lazy val configuration = Play.application().configuration()
  lazy val config = GigConfig.load()
  lazy val kafkaPort = config.getString("gig.kafka.port")
  val localKafkaServer= new KafkaServer(kafkaPort.toInt)
  localKafkaServer.startup()
  Thread.sleep(199)
  Logger.info(s"kafka server started on port: $kafkaPort")
  implicit  val system:ActorSystem = ActorSystem("gen")
  Logger.info("Application started..")
  lifecycle.addStopHook { () =>
    Logger.info("application about to stop")
    Future.successful(system.terminate())
  }
}
