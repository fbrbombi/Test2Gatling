package TestGatling.helper

case class PaPageObject(
                         var modifiers: List[RequestModifiers] = List(),
                         var collection: String = "papages",
                         var resultsPerPage: String = "1",
                         var searchPhrase: String = "",
                       )
