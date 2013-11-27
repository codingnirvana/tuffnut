package org.codingnirvana.akka

import akka.actor.{Props, ActorSystem, Actor, ActorRef}
  case class Start(thePonger: ActorRef)
  class Pinger extends Actor {
    def receive = {
      case Start(x) => x ! "Ping!"
      case x => println("Pinger: " + x); sender ! "Ping!"
    }
  }
  class Ponger extends Actor {
    def receive = { case x => println("Ponger: " + x); sender ! "Pong!" }
  }

  object PingPong extends App {
    val system = ActorSystem()
    val ponger = system.actorOf(Props[Ponger])
    val pinger = system.actorOf(Props[Pinger])
    pinger ! Start(ponger)
  }

