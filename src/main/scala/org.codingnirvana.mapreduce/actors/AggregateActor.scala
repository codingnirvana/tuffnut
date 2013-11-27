package org.codingnirvana.mapreduce.actors

import akka.actor.Actor
import collection.mutable
import org.codingnirvana.mapreduce.messages.{Result, ReduceData}

class AggregateActor extends Actor {

  var finalReducedMap = new mutable.HashMap[String, Int]

  def receive: Receive = {
    case data: ReduceData => aggregateInMemoryReduce(data.reduceDataMap)
    case result: Result => System.out.println(finalReducedMap)
  }

  def aggregateInMemoryReduce(reducedList: Map[String, Int]) {
    reducedList.foreach {
      case(k,v)  => finalReducedMap.put(k, finalReducedMap.getOrElse(k, 0)  + reducedList(k))
    }
  }
}