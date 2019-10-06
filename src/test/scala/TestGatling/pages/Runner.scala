package TestGatling.pages

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder


object Runner {
  def main(args: Array[String]): Unit = {
    val simClass = classOf[SecondTestTaxonomy].getName
    val props = new GatlingPropertiesBuilder
    props.simulationClass(simClass)
    Gatling.fromMap(props.build)
  }
}
