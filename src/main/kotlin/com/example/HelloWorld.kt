package com.example

import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Query
import org.http4k.lens.Path
import org.http4k.lens.string
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.core.Body
import org.http4k.format.Jackson.auto
import org.http4k.routing.header


val optionalQuery = Query.string().optional("name")
val locale = Path.string().of("locale")
data class JSONHeaderData(val headers: Map<String, String?>)
val jsonHeaderLens = Body.auto<JSONHeaderData>().toLens()
val stringHeaderLens = Body.auto<String>().toLens()

val app: HttpHandler = routes(
    "/{locale}/hello" bind GET to { req ->
        val locale: String = locale(req)
        val name: String? = optionalQuery(req)

        if (name.isNullOrEmpty()) {
            Response(OK).body("Hello")
        } else {
            when (locale) {
                "en-US" -> Response(OK).body("Hello, $name")
                "fr-FR" -> Response(OK).body("Bonjour $name")
                "en-AU" -> Response(OK).body("G'day $name")
                "it-IT" -> Response(OK).body("Salve $name")
                "en-UK" -> Response(OK).body("Alright, $name?")
                else ->  Response(OK).body("I don't know what language you speak but hello, $name")
            }
        }
    },
    "/echo_headers" bind GET to { req ->
        val prefix = Query.optional("as_response_headers_with_prefix").extract(req)?.substringAfter("=")
        var headers: Headers = req.headers
        val headerString = headers.joinToString("\n") { "${it.first}: ${it.second}"}
        val headerJSON = JSONHeaderData(headers.toMap())

        val acceptValuePair = headers.find{it.first == "Accept"}.toString()

        if (prefix != null) {
            val response = Response(OK)
            response.body(prefix.toString())
//            stringHeaderLens.inject(headers.toString(), response)
//            stringHeaderLens.inject(prefix, response)

        } else if (acceptValuePair.contains("json")) {
            val response = Response(OK)
            jsonHeaderLens.inject(headerJSON, response)

        } else {
            Response(OK).body(headerString)
        }

    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}

