package com.example


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Request

class HelloWorldClient (val baseURL: String){
    private val client = JavaHttpClient.invoke()

    fun sayHello(name: String? = null, language: String? = null): String {
        val requestURL = "$baseURL/hello${if (name != null) "?name=$name" else ""}"
        val request = Request(GET, requestURL).header("Accept-language", "$language")
        return client(request).bodyString()
    }

    fun echoHeaders(headerList: List<Pair<String, String>>, prefix:String? = null): Map<String, String?> {
        val requestURL = "$baseURL/echo_headers${if (prefix != null) "?as_response_headers_with_prefix=$prefix" else "" }"
        var request = Request(GET, requestURL)
        headerList.forEach { (key, value) ->
            request = request.header(key, value)
        }
        return if (prefix != null) {
            client(request).headers.toMap()

        } else {
            val headers = client(request).bodyString()
            // Has to be a better way of figuring out if it's a json or not below
            if(headers.startsWith("""{"headers"""")) {
                return turnJsonStringIntoMap(headers)
            } else {
                return turnStringIntoMap(headers)
            }

        }

    }

    private fun turnStringIntoMap(headerString: String): Map<String, String> {
        val listHeader =  headerString.split("\n")
        val map = listHeader.associate {element ->
            val pair = element.split(":")
            pair.first() to pair.last()
        }
        return map
    }

    private fun turnJsonStringIntoMap(jsonString: String): Map<String, String> {
        val mapper = jacksonObjectMapper()
        val parsedMap: Map<String, Any> = mapper.readValue(jsonString)
        val headersMap = parsedMap["headers"] as Map<String, String>
        return headersMap
    }


}



fun main() {

    val client = HelloWorldClient("http://localhost:3000")

    // Testing the sayHello function
//            println(client.sayHello())
//            println(client.sayHello(name="Ellie"))
//            println(client.sayHello(name="Ellie", language = "en-GB"))
//            println(client.sayHello(language = "fr-FR"))

    // Testing the echoHeaders function

        val headers = listOf(Pair("X-Header", "x value"), Pair("Y-Header", "y value"))
        val headersAcceptJson = listOf(Pair("X-Header", "x value"), Pair("Y-Header", "y value"), Pair("Accept", "application/json"))
            println(client.echoHeaders(headers))
            println(client.echoHeaders(headersAcceptJson))
            println(client.echoHeaders(headers, "PREFIX-"))

}
