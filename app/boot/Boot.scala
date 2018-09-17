package boot

import akka.actor.{ActorSystem, Props}
import cakesolutions.kafka.KafkaTopicPartition
import cakesolutions.kafka.akka.KafkaConsumerActor.Subscribe
import cakesolutions.kafka.testkit.KafkaServer
import gig.constent.GigConfig
import gig.consumer.Consumer
import javax.inject.Inject
import play.api.inject.ApplicationLifecycle
import play.api.Logger
import test.gig.downstreamactor.DownStreamTestActor

/**
  * For boot in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
class Boot  @Inject( ) (lifecycle: ApplicationLifecycle,system:ActorSystem)  {
  def init(): Unit ={

  }
  lazy val config = GigConfig.load()
  lazy val kafkaPort = config.getString("gig.kafka.port")
  val localKafkaServer= new KafkaServer(kafkaPort.toInt)
  localKafkaServer.startup()
  Logger.info(s"kafka server started on port: $kafkaPort")
  Logger.info("Application started..")
  implicit val system2 :ActorSystem  =system
  val downStreamTestActor = system.actorOf(Props[DownStreamTestActor],"downstreamActor")
  val consumer= Consumer.createConsumerActor(downStreamTestActor,Map("max.poll.records"->Int.box(500)))
  val topicPartition  =KafkaTopicPartition("bitcoin", 0)
  val subscription = Subscribe.AutoPartition(List(topicPartition.topic()))
  consumer.subscribe(subscription)
  init()

  lifecycle.addStopHook { () =>
    Logger.info("application about to stop")
    consumer.unsubscribe()
    localKafkaServer.close()
    system.terminate()
  }
}
