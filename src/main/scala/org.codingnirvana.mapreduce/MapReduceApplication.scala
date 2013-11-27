package org.codingnirvana.mapreduce

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import org.codingnirvana.mapreduce.messages.Result
import org.codingnirvana.mapreduce.actors.MasterActor

object MapReduceApplication {

  def main(args: Array[String]) {
    val system = ActorSystem("MapReduceApp")
    val master = system.actorOf(Props[MasterActor], name = "master")

    master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
    master ! "Dog is man's best friend"
    master ! "Dog and Fox belong to the same family"

    Thread.sleep(500)
    master ! new Result

    Thread.sleep(500)
    system.shutdown
  }
}