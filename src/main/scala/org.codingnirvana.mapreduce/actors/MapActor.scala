package org.codingnirvana.mapreduce.actors

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import org.codingnirvana.mapreduce.messages.{Word, MapData}

class MapActor(reduceActor: ActorRef) extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")

  val defaultCount: Int = 1

  def receive: Receive = {
    case message: String =>
      reduceActor ! evaluateExpression(message)
  }

  private def evaluateExpression(line: String): MapData = {
    new MapData(line.split("\\s+")
      .map(_.toLowerCase)
      .filter(!STOP_WORDS_LIST.contains(_))
      .map(new Word(_, defaultCount)))
  }
}