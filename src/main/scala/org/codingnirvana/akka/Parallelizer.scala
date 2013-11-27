package org.codingnirvana.akka

import akka.actor.{Props, ActorRef, ActorSystem, Actor}
import akka.routing.RoundRobinRouter

object Parallelizer extends App{

    class MyActor extends Actor {
      def receive = {
        case x => //println(x)
       }
    }

    val system = ActorSystem()
    val router: ActorRef = system.actorOf(Props[MyActor].
      withRouter(RoundRobinRouter(nrOfInstances = 5)))

    for (i <- 1 to 10000000) router ! i

    println("Done")
}
