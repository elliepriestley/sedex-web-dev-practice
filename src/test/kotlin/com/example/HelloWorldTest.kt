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
        assertEquals(Response(OK).body("Hello"), app(Request(GET, "/hello")))
    }

    @Test
    fun `Test that hello endpoint accepts optional name param and uses it in response when available`() {
        assertEquals(Response(OK).body("Hello, Ellie"), app(Request(GET, "hello?name=Ellie")))
        assertEquals(Response(OK).body("Hello, Leah"), app(Request(GET, "hello?name=Leah")))
        assertEquals(Response(OK).body("Hello, Alice"), app(Request(GET, "hello?name=Alice")))

    }

}
