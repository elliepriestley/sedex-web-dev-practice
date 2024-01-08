package com.example

import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintResponse

class HelloWorldClient constructor(baseURL: String) {
    val baseURL = baseURL


    // initializes with a base url

    fun hello(name: String? = null): Response {
        val requestURL: String = "${baseURL}/hello${if (name != null) "?name=$name" else ""}"
        return app(Request(GET, requestURL))


        // accepts optional name param
        // accepts language to act as a parameter in Accept_Language header
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
