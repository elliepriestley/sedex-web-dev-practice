package com.example

import org.http4k.core.Headers
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response

class HelloWorldClient (baseURL: String) {
    val baseURL = baseURL

    fun hello(name: String? = null, language: String? = null): Response {
        val requestURL = "${baseURL}/hello${if (name != null) "?name=$name" else ""}"

        val request: Request = Request(GET, requestURL).header("Accept-language", language)

        return app(request)


        // accepts optional name param

    }

    fun echoHeadersAsString(headersList: List<Pair<String , String>>?): String {
        val requestURL = "$baseURL/echo_headers"
        var request = Request(GET, requestURL)

        headersList?.forEach { (key,  value) ->
            request = request.header(key, value)
        }
        // There is a better way of writing this ^ that uses a map and no val, but I can't work it out ...

        val response: Response = app(request)
        return response.bodyString()




        // returns list of request headers as response text
        // optional prefix, ?as_response_headers_with_prefix=
    }

    fun echoHeadersAsJson() {
        TODO()
    }

}



fun main() {

// val client: HttpHandler = JavaHttpClient()

//    val printingClient: HttpHandler = PrintResponse().then(client)

//    val response: Response = printingClient(Request(GET, "http://localhost:9000/hello"))

    val client = HelloWorldClient("http://localhost:9000/")

    println(client.hello("Ellie", "en-GB"))


}
