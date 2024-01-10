package com.example

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.format.Jackson.asJsonObject

class HelloWorldClient (baseURL: String){
    val baseURL = baseURL
    private val client: HttpHandler = JavaHttpClient()



    fun sayHello(name: String? = null, language: String? = null): String {
        val requestURL = "$baseURL/hello${if (name != null) "?name=$name" else ""}"
        val request = Request(GET, requestURL).header("Accept-language", "$language")
        return client(request).bodyString()
    }

    fun echoHeaders(headerList: List<Pair<String, String>>, prefix:String? = null): String {
        val requestURL = "$baseURL/echo_headers${if (prefix != null) "?as_response_headers_with_prefix=$prefix" else "" }"
        var request = Request(GET, requestURL)
        headerList.forEach { (key, value) ->
            request = request.header(key, value)
        }
        return if (prefix != null) {
            client(request).headers.toString()
        } else {
            client(request).bodyString()
        }

    }





}



fun main() {

    val client = HelloWorldClient("http://localhost:3000")
//    println(client.sayHello())
//    println(client.sayHello(name="Ellie"))
//    println(client.sayHello(name="Ellie", language = "en-GB"))
//    println(client.sayHello(language = "fr-FR"))
    val headers = listOf(Pair("X-Header", "x value"), Pair("Y-Header", "y value"))
    val headersAcceptJson = listOf(Pair("X-Header", "x value"), Pair("Y-Header", "y value"), Pair("Accept", "application/json"))
//    println(client.echoHeaders(headers))
//    println(client.echoHeaders(headersAcceptJson))
    println(client.echoHeaders(headers, "PREFIX-"))
//    println(client.echoHeaders(listOf(Pair("Some-X-Header", "some x value"), Pair("Some-Y-Header", "some y value"))))





}
