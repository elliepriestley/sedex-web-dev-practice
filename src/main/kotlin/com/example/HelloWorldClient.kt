package com.example

import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintResponse

class HelloWorldClient (baseURL: String) {
    val baseURL = baseURL

    fun hello(name: String? = null, language: String? = null): Response {
        val requestURL = "${baseURL}/hello${if (name != null) "?name=$name" else ""}"

        val request: Request = Request(GET, requestURL).header("Accept-language", language)

        val response = app(request)

        return response


        // accepts optional name param

    }

    fun echoHeaders() {
        TODO()
        // optional param for json - if present return a json
        // returns list of request headers as response text
        // optional prefix, ?as_response_headers_with_prefix=
    }

}



fun main() {

// val client: HttpHandler = JavaHttpClient()

//    val printingClient: HttpHandler = PrintResponse().then(client)

//    val response: Response = printingClient(Request(GET, "http://localhost:9000/hello"))

    val client = HelloWorldClient("http://localhost:9000/")

    println(client.hello())

}
