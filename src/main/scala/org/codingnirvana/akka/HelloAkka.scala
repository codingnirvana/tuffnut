package org.codingnirvana.akka

import akka.actor.{Props, ActorSystem, ActorLogging, Actor}

object HelloAkka extends App{

  case class Greeting(who: String)

  class GreetingActor extends Actor with ActorLogging {
    def receive = {
      case Greeting(who) =>
        log.info(s"Hello from $who")
        //context.stop(self)
    }
  }

  val system = ActorSystem()
  val greeter = system.actorOf(Props[GreetingActor])

  // Tell

  greeter ! Greeting("Rajesh")


  // Ask

}
