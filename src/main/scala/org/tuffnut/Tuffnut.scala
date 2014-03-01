package org.tuffnut

import com.yammer.dropwizard.ScalaService
import com.yammer.dropwizard.config.{Bootstrap, Environment}
import com.yammer.dropwizard.bundles.ScalaBundle

object X extends App {
  Tuffnut.run(Array("server","conf/config.yml"))
}

object Tuffnut extends ScalaService[TuffnutConfiguration]{

  def run(conf: TuffnutConfiguration, env: Environment) {
    env.addResource(new ProductResource())
  }

  def initialize(bootstrap: Bootstrap[TuffnutConfiguration]){
    bootstrap.setName("tuffnut")
    bootstrap.addBundle(new ScalaBundle)
  }
}

