package TestGatling.pages

import TestGatling.helper.{RequestModifiers, RequestObject, ValueList}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.FiniteDuration

trait URIBuilder extends Scenario {

  var modifiers = new ListBuffer[String]()
  var values = new ListBuffer[ValueList]()
  var searchModifiers = new ListBuffer[RequestModifiers]()

  def setSearchModifierList(): URIBuilder = {
    searchObject.modifiers = searchModifiers.toList
    searchModifiers = new ListBuffer[RequestModifiers]
    this
  }

  def addSearchModifier(): URIBuilder = {
    searchModifiers += modifier
    modifier = RequestModifiers()
    this
  }

  def setKeySearch(keyNavigator: String): URIBuilder = {
    modifier.key = keyNavigator
    this
  }

  def setValueList(): URIBuilder = {
    modifier.value = values.toList
    values = new ListBuffer[ValueList]
    this
  }

  def setValueList(filter: String): URIBuilder = {
    modifier.value = filter
    values = new ListBuffer[ValueList]
    this
  }

  def addValue(): URIBuilder = {
    values += value
    value = ValueList()
    this
  }

  def setNavigator(navigatorName: String): URIBuilder = {
    value.navigatorName = navigatorName
    this
  }

  def addModifier(modifierName: String): URIBuilder = {
    modifiers += modifierName
    this
  }

  def setModifiersList(): URIBuilder = {
    value.modifier = modifiers.toList
    modifiers = new ListBuffer[String]
    this
  }

  def changeTerm(searchTerm: String): URIBuilder = {
    searchObject.searchPhrase = searchTerm
    papageObject.searchPhrase = searchTerm
    this
  }

  def changeCollections(collection: String): URIBuilder = {
    searchObject.collection = collection
    this
  }

  def createSearchURI(): String = {
    val path = searchURI + "?requestObject=" + getRequestObject + "&requestMethod=search"
    requestObject = RequestObject()
    path
  }

  def changeCollectionTo(collection: String, searchTerm: String): ScenarioBuilder = {
    changeTerm(searchTerm)
    changeCollections(collection)
    createRequestObject()
    requestBuilder("Changing to " + collection, "Request For " + collection, createSearchURI())
  }
}

class FirstTestURIBuilder extends URIBuilder {

  val pauseTime: FiniteDuration = 0
  val allScenarios: ScenarioBuilder = scenario("Performance testing")
    .exec(homeSimpleScenario.pause(pauseTime))
    .exec(changeCollectionTo("cases", "agile").pause(pauseTime))
    .exec(changeCollectionTo("proposals", "agile").pause(pauseTime))
    .exec(changeCollectionTo("insights", "agile").pause(pauseTime))
    .exec(advancedSearchSimpleScenario.pause(pauseTime))

  setUp(allScenarios
    .inject(atOnceUsers(1))
    .protocols(httpProtocol)
  )
}

class SecondTestTaxonomy extends URIBuilder {

  def addTaxonomy(collection: String, searchTerm: String, typeSearch: String): ScenarioBuilder = {
    setNavigator("industrytaxonomynavigator")
      .addModifier("|Advanced Manufacturing & Services")
      .setModifiersList()
      .addValue()
      .setValueList()
      .setKeySearch(typeSearch)
      .addSearchModifier()
      .setKeySearch("industrytaxonomynavigator")
      .setValueList("|Advanced Manufacturing & Services")
      .addSearchModifier()
      .setSearchModifierList()
      .changeTerm(searchTerm)
      .changeCollections(collection)
      .createRequestObject()
    requestBuilder("Adding Taxonomy to Industry in " + typeSearch, "Request For Industry in " + typeSearch, createSearchURI() + "&st=" + searchTerm)
  }


  val pauseTime: FiniteDuration = 1
  val allScenarios: ScenarioBuilder = scenario("Performance testing")
    .exec(homeSimpleScenario.pause(pauseTime))
    .exec(changeCollectionTo("cases", "agile").pause(pauseTime))
    .exec(changeCollectionTo("proposals", "agile").pause(pauseTime))
    .exec(changeCollectionTo("insights", "agile").pause(pauseTime))
    .exec(addTaxonomy("insights", "agile", regularSearch).pause(pauseTime))
    .exec(advancedSearchSimpleScenario.pause(pauseTime))
    .exec(addTaxonomy("insights", "agile", advancedSearch).pause(pauseTime))


  setUp(allScenarios
    .inject(atOnceUsers(1))
    .protocols(httpProtocol)
  )
}
