package bitcoin.controllers

import akka.actor.{ActorSystem, Props}
import cakesolutions.kafka.akka.KafkaConsumerActor.Subscribe
import cakesolutions.kafka.{KafkaProducerRecord, KafkaTopicPartition}
import gig.consumer.Consumer
import gig.producer.GigProducer
import javax.inject.Inject
import play.api.Logger
import play.api.mvc.{AbstractController, ControllerComponents}
import test.gig.downstreamactor.DownStreamTestActor
import test.gig.gigConsumer.TestConsumer3.{randomString, randomTopicPartition}

import scala.util.Random

/**
  * For bitcoin.controllers in gen
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
class BitController  @Inject()(cc: ControllerComponents, system:ActorSystem) extends AbstractController(cc) {
  private def randomString: String = Random.alphanumeric.take(5).mkString("")
  private def randomTopicPartition = KafkaTopicPartition(randomString, 0)
  implicit val system2:ActorSystem = system

  def get = Action { implicit request =>
    val producer = GigProducer.createProducer()
    Logger.debug("Gig producer started:")
    val topicPartition = randomTopicPartition
    Logger.info(s"Sending data to topic: $topicPartition")
    producer.send(KafkaProducerRecord(topicPartition.topic(), None, "valueCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC"))
    producer.flush()
    val downStreamTestActor = system.actorOf(Props[DownStreamTestActor],"downstreamActor")
    val consumer= Consumer.createConsumer(downStreamTestActor)
    val subscription = Subscribe.AutoPartition(List(topicPartition.topic()))
    consumer.subscribe(subscription)
    Thread.sleep(1999)
    Ok("Success")
  }
}
