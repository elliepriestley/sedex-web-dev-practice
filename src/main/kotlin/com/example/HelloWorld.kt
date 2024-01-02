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

val optionalQuery = Query.string().optional("name")
val locale = Path.string().of("locale")

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
        val headers: Headers = req.headers
        val headerString = headers.joinToString("\n") { it.first }

//        if ("json" in headers["Accept"] )
//        Response(OK).body(headerString)
        val acceptValuePair = headers.find{it.first == "Accept"}.toString()

        if (acceptValuePair.contains("json")) {
            Response(OK).body("contains json")
        } else {
            Response(OK).body("does not accept json")
        }

//        Response(OK).body(acceptValuePair)

    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}
