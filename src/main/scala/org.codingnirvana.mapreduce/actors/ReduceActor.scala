package org.codingnirvana.mapreduce.actors

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable
import org.codingnirvana.mapreduce.messages.{ReduceData, Word, MapData}

class ReduceActor(aggregateActor: ActorRef) extends Actor {

  val defaultCount: Int = 1

  def receive: Receive = {
    case message: MapData =>
      aggregateActor !  reduce(message.dataList)
  }

  def reduce(dataList: Array[Word]): ReduceData = {
    val reducedMap = new mutable.HashMap[String,Int]
    dataList.foreach(data => reducedMap += {(data.word, reducedMap.getOrElse(data.word,0) + defaultCount)})
    new ReduceData(reducedMap.toMap)
  }
}