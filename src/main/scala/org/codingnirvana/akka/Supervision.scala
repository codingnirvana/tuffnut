package org.codingnirvana.akka

import akka.actor._
import akka.actor.SupervisorStrategy.{Restart, Resume}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import akka.actor.OneForOneStrategy

object Supervision extends App{

  var errorCount = 0

  class RogueActor extends Actor {
    def receive = {
      case i: Int if i % 10 == 0=>
          errorCount += 1
          println(s"Error $errorCount")
          throw new ArithmeticException
      case msg =>
        println(msg)
    }
  }


  class MySupervisor extends Actor {
    override val supervisorStrategy = OneForOneStrategy(
      maxNrOfRetries = 10,
      withinTimeRange = Duration(1, TimeUnit.MINUTES)) {

      case ae: ArithmeticException => Resume
      case np: NullPointerException => Restart
    }

    def receive = {
      case _ =>
    }

    val rogue = context.actorOf(Props[RogueActor])

    (1 to 100).map(i => rogue ! i)
  }

  val system = ActorSystem("Supervision")
  system.actorOf(Props[MySupervisor])


}
