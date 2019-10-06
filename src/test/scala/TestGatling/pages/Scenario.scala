package TestGatling.pages

import java.io.{File, PrintWriter}
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import TestGatling.helper._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

trait Scenario extends Simulation {

  val defaultUrl = "http://computer-database.gatling.io."

  val regularSearch = "navigators"
  val advancedSearch = "navigators_as"

  lazy val versionHeader = CustomHeader("q=0.9,*/*;q=0.8")

  lazy val userAgentHeader = CustomHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  lazy val acceptHeaders: HttpHeader = List(HtmlHeader, XHtmlHeader, XmlHeader, versionHeader)
    .foldLeft(CustomHeader(""): HttpHeader)(_ ++ _)

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(baseUrl.getOrElse(defaultUrl))
    .acceptHeader(acceptHeaders.header)
    .doNotTrackHeader(DoTrack.header)
    .acceptEncodingHeader(Gzip.header)
    .userAgentHeader(userAgentHeader.header)

  val baseURI: String = "/#"
  val homePageURI: String = baseURI + "/home"
  val advancedSearchURI: String = baseURI + "/adsearch"
  val peopleFinderURI: String = baseURI + "/peopleFinder"
  val peopleProfileURI: String = baseURI + "/peopleProfile/44751"
  val searchURI: String = baseURI + "/search"
  var value: ValueList = ValueList()
  var modifier: RequestModifiers = RequestModifiers()
  var searchObject: SearchObject = SearchObject()
  var papageObject: PaPageObject = PaPageObject()
  var requestObject: RequestObject = RequestObject()

  val homeSimpleScenario: ScenarioBuilder = scenario("home")
    .exec(http("RequestHomePage")
      .get(baseURI))

  val advancedSearchSimpleScenario: ScenarioBuilder = scenario("advanced-search")
    .exec(http("RequestAdvancedSearch")
      .get(advancedSearchURI))

  val peopleFinderSimpleScenario: ScenarioBuilder = scenario("people-finder")
    .exec(http("request-3")
      .get(peopleFinderURI))

  val peopleProfileSimpleScenario: ScenarioBuilder = scenario("people-profile")
    .exec(http("request-4")
      .get(peopleProfileURI))

  def baseUrl: Option[String] = Option(System.getenv("TEST_ENV"))

  def requestBuilder(name: String, requestName: String, path: String): ScenarioBuilder = {
    scenario(name)
      .exec(http(requestName)
        .get(path.replace("+", "%20"))
        .check(bodyString.saveAs("response")))
      .exec(session => {
        createFiles(name, path.replace("+", "%20"))
        session
      })
  }

  def getRequestObject: String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    println(requestObject)
    URLEncoder.encode(write(requestObject), StandardCharsets.UTF_8.toString)
  }

  def createRequestObject(): Unit = {
    requestObject.papageObject = papageObject
    requestObject.searchObject = searchObject
    searchObject = SearchObject()
  }

  def createFiles(name: String, message: String): Unit = {
    val writer = new PrintWriter(new File(name + ".txt"))
    writer.write(defaultUrl + message)
    writer.close()
  }
}
