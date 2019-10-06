package TestGatling.helper

case class SearchObject(
                        var modifiers: List[RequestModifiers]=List(),
                        var collection: String ="insights",
                        var searchPhrase: String = "",
                        var FreeText: String =""
                      )
case class RequestModifiers(
                             var key: String = "",
                             var value: Object = List()
                           )
case class ValueList(
                  var navigatorName: String = "",
                  var operator: String = "",
                  var modifier: List[String] = List()
                )


