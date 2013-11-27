package org.codingnirvana.mapreduce.actors

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import org.codingnirvana.mapreduce.messages.Result

class MasterActor extends Actor {

  val aggregateActor: ActorRef = context.actorOf(Props[AggregateActor], name = "aggregate")
  val reduceActor: ActorRef = context.actorOf(Props.create(classOf[ReduceActor], aggregateActor), name = "reduce")
  val mapActor: ActorRef = context.actorOf(Props.create(classOf[MapActor],reduceActor), name = "map")

  def receive: Receive = {
    case message: String => mapActor ! message
    case message: Result => aggregateActor ! message
  }
}
