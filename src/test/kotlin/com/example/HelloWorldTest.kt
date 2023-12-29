package com.example

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloWorldTest {

    @Test
    fun `Test that hello endpoint returns Hello when no name parameter is used`() {
        assertEquals(Response(OK).body("Hello"), app(Request(GET, "/en-US/hello")))
    }

    @Test
    fun `Test that hello endpoint accepts optional name param and uses it in response when available`() {
        assertEquals(Response(OK).body("Hello, Ellie"), app(Request(GET, "/en-US/hello?name=Ellie")))
        assertEquals(Response(OK).body("Hello, Leah"), app(Request(GET, "/en-US/hello?name=Leah")))
        assertEquals(Response(OK).body("Hello, Alice"), app(Request(GET, "/en-US/hello?name=Alice")))

    }

    @Test
    fun `Test that endpoint has a path variable that responds with hello from supported languages`() {
        assertEquals(Response(OK).body("Hello, Ellie"), app(Request(GET, "/en-US/hello?name=Ellie")))
        assertEquals(Response(OK).body("Bonjour Ellie"), app(Request(GET, "/fr-FR/hello?name=Ellie")))
        assertEquals(Response(OK).body("G'day Ellie"), app(Request(GET, "/en-AU/hello?name=Ellie")))
        assertEquals(Response(OK).body("Salve Ellie"), app(Request(GET, "/it-IT/hello?name=Ellie")))
        assertEquals(Response(OK).body("Alright, Ellie?"), app(Request(GET, "/en-UK/hello?name=Ellie")))
    }

}
