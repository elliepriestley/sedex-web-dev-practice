package com.example

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
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
        assertEquals(Response(OK).body("I don't know what language you speak but hello, Ellie"), app(Request(GET, "/en-HA/hello?name=Ellie")))
    }

    @Test
    fun `Test that echo_headers endpoint returns list of all request headers in response`() {
        val headersKeys: List<String> = listOf(
                "Accept-encoding",
                "Sec-ch-ua",
                "Accept",
                "Sec-fetch-dest",
                "Sec-fetch-user",
                "Connection",
                "Host",
                "Sec-fetch-site",
                "Sec-ch-ua-platform",
                "Sec-fetch-mode",
                "User-agent",
                "Accept-language",
                "Upgrade-insecure-requests",
               "Sec-ch-ua-mobile",
                "Cache-control")

        val requestWithHeaders = Request(GET, "/echo_headers").header("Accept-encoding", "")
            .header("Sec-ch-ua", "") .header("Accept", "") .header("Sec-fetch-dest", "")
            .header("Sec-fetch-user", "") .header("Connection", "") .header("Host", "")
            .header("Sec-fetch-site", "") .header("Sec-ch-ua-platform", "") .header("Sec-fetch-mode", "")
            .header("User-agent", "") .header("Accept-language", "") .header("Upgrade-insecure-requests", "")
            .header("Sec-ch-ua-mobile", "") .header("Cache-control", "")

        val actualResponse = app(requestWithHeaders)
        val actualHeaderKeys = actualResponse.bodyString().split("\n")

        assertEquals(actualResponse.status, OK)
        assertEquals(headersKeys.sorted(), actualHeaderKeys.sorted())
    }

    @Disabled
    @Test
    fun `Test that echo_headers endpoint recognises whether json is accepted`() {
        assertEquals(Response(OK).body("does not accept json"), app(Request(GET, "echo_headers")))
    }


}
