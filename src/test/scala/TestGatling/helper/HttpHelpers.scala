package TestGatling.helper

sealed abstract class HttpHeader(val header : String) {
  def ++(a: HttpHeader) : HttpHeader = new ComposeHeader(header , a.header)
}
case object HtmlHeader extends HttpHeader("text/html")
case object XHtmlHeader extends HttpHeader("application/xhtml+xml")
case object XmlHeader extends HttpHeader("application/xml")
case object Gzip extends HttpHeader("gzip, deflate")
case object DoTrack extends HttpHeader("0")
case object DoNotTrack extends HttpHeader("1")
private case class ComposeHeader(val v1: String, val v2: String) extends HttpHeader(v1 + v2)
case class CustomHeader(override val header: String) extends HttpHeader(header)
