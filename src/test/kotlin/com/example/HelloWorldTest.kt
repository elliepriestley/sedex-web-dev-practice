package com.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasHeader
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


class HelloWorldTest {

    @Test
    fun `Test that hello endpoint returns Hello when no name parameter is used`() {
        assertEquals(Response(OK).body("Hi"), app(Request(GET, "/hello")))
    }

    @Test
    fun `Test that hello endpoint accepts optional name param and uses it in response when available`() {
        assertEquals(Response(OK).body("Hi Ellie"), app(Request(GET, "/hello?name=Ellie")))
        assertEquals(Response(OK).body("Hi Leah"), app(Request(GET, "/hello?name=Leah")))
        assertEquals(Response(OK).body("Hi Alice"), app(Request(GET, "/hello?name=Alice")))

    }

    @Disabled // no longer relevant - hello endpoint now reads Accept-Language header instead
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
                "Accept-encoding: ",
                "Sec-ch-ua: ",
                "Accept: ",
                "Sec-fetch-dest: ",
                "Sec-fetch-user: ",
                "Connection: ",
                "Host: ",
                "Sec-fetch-site: ",
                "Sec-ch-ua-platform: ",
                "Sec-fetch-mode: ",
                "User-agent: ",
                "Accept-language: ",
                "Upgrade-insecure-requests: ",
               "Sec-ch-ua-mobile: ",
                "Cache-control: ")

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


    @Test
    fun `Test that echo_headers endpoint returns json object when Accept heading contains json, and string object otherwise`() {
        val requestWithHeaders = Request(GET, "/echo_headers").header("Accept", "")
        val requestWithHeadersThatAcceptsJSON = Request(GET, "/echo_headers").header("Accept", "application/json")
            .header("Content Type", "")

        val responseThatDoesNotAcceptJSON = app(requestWithHeaders)
        val responseThatAcceptsJSON = app(requestWithHeadersThatAcceptsJSON)

        // Assert the JSON type response
        assertEquals(responseThatAcceptsJSON.status, OK)
        // uses the objectMapper from the Jackson lib to parse the body into a JSON object.
        // assert it's not null, as if the body is not a valid JSON string, the readValue function will throw an IOException.
        val objectMapper = ObjectMapper()
        val jsonObject = objectMapper.readValue(responseThatAcceptsJSON.bodyString(), Map::class.java)
        assertNotNull(jsonObject)

        // Assert the string type response
        assertEquals(responseThatDoesNotAcceptJSON.status, OK)
        assertInstanceOf(String::class.java, responseThatDoesNotAcceptJSON.bodyString())
    }

    @Test
    fun `Test that in the case that the prefix is not null, echo_headers endpoint with a query parameter returns empty response body`() {
        assertEquals(Response(OK).body(""), app(Request(GET, "http://localhost:9000/echo_headers?as_response_headers_with_prefix=X-Echo-")))
        assertEquals(Response(OK).body(""), app(Request(GET, "http://localhost:9000/echo_headers?as_response_headers_with_prefix=Y-Echo-")))
        assertEquals(Response(OK).body(""), app(Request(GET, "http://localhost:9000/echo_headers?as_response_headers_with_prefix=TEST")))
        assertEquals(Response(OK).body(""), app(Request(GET, "http://localhost:9000/echo_headers?as_response_headers_with_prefix=AnotherTest")))
        assertEquals(Response(OK).body(""), app(Request(GET, "http://localhost:9000/echo_headers?as_response_headers_with_prefix=10000")))
    }

    @Test
    fun `Test that when echo_headers endpoint accepts a query param, request headers are echoed back as response headers but with given prefix attached to header name`() {
        val requestWithHeaders = Request(GET, "http://localhost:9000/echo_headers?as_response_headers_with_prefix=X-Echo-")
            .header("X-Custom-Header", "some x value")
            .header("Y-Custom-Header", "some y value")

        val actualResponse: Response = app(requestWithHeaders)

        assertThat(actualResponse, hasStatus(OK)
            .and(hasHeader("X-Echo-X-Custom-Header", "some x value"))
            .and(!hasHeader("X-Echo-Y-Custom-Header")))
    }

    @Test
    fun `Test that hello endpoint reads the Accept_language header and response in the appropriate language when name query param is provided`() {
        val requestThatAcceptsBritishEnglish = Request(GET, "http://localhost:9000/hello?name=Ellie")
            .header("Accept-language", "en-GB")
        val requestThatAcceptsAustralianEnglish = Request(GET, "http://localhost:9000/hello?name=Ellie")
            .header("Accept-language", "en-AU")
        val requestThatAcceptsAmericanEnglish = Request(GET, "http://localhost:9000/hello?name=Ellie")
            .header("Accept-language", "en-US")
        val requestThatAcceptsItalian = Request(GET, "http://localhost:9000/hello?name=Ellie")
            .header("Accept-language", "it-IT")
        val requestThatAcceptsFrench = Request(GET, "http://localhost:9000/hello?name=Ellie")
            .header("Accept-language", "fr-FR")
        val requestWithDefaultLanguage = Request(GET, "http://localhost:9000/hello?name=Ellie")

        assertThat(app(requestThatAcceptsBritishEnglish), hasStatus(OK)
            .and(hasBody("Alright, Ellie?")))

        assertThat(app(requestThatAcceptsAustralianEnglish), hasStatus(OK)
            .and(hasBody("G'day Ellie")))

        assertThat(app(requestThatAcceptsAmericanEnglish), hasStatus(OK)
            .and(hasBody("Hello, Ellie")))

        assertThat(app(requestThatAcceptsItalian), hasStatus(OK)
            .and(hasBody("Salve Ellie")))

        assertThat(app(requestThatAcceptsFrench), hasStatus(OK)
            .and(hasBody("Bonjour Ellie")))

        assertThat(app(requestWithDefaultLanguage), hasStatus(OK)
            .and(hasBody("Hi Ellie")))
    }

    // what if multiple languages are permitted?
    // need to change it so that it is still language specific even when name is not provided.

    @Test
    fun `Test that hello endpoint reads the Accept_language header and response in the appropriate language when name query param is not provided`() {
        val requestThatAcceptsBritishEnglish = Request(GET, "http://localhost:9000/hello")
            .header("Accept-language", "en-GB")
        val requestThatAcceptsAustralianEnglish = Request(GET, "http://localhost:9000/hello")
            .header("Accept-language", "en-AU")
        val requestThatAcceptsAmericanEnglish = Request(GET, "http://localhost:9000/hello")
            .header("Accept-language", "en-US")
        val requestThatAcceptsItalian = Request(GET, "http://localhost:9000/hello")
            .header("Accept-language", "it-IT")
        val requestThatAcceptsFrench = Request(GET, "http://localhost:9000/hello")
            .header("Accept-language", "fr-FR")
        val requestWithDefaultLanguage = Request(GET, "http://localhost:9000/hello")

        assertThat(app(requestThatAcceptsBritishEnglish), hasStatus(OK)
            .and(hasBody("Alright?")))

        assertThat(app(requestThatAcceptsAustralianEnglish), hasStatus(OK)
            .and(hasBody("G'day")))

        assertThat(app(requestThatAcceptsAmericanEnglish), hasStatus(OK)
            .and(hasBody("Hello")))

        assertThat(app(requestThatAcceptsItalian), hasStatus(OK)
            .and(hasBody("Salve")))

        assertThat(app(requestThatAcceptsFrench), hasStatus(OK)
            .and(hasBody("Bonjour")))

        assertThat(app(requestWithDefaultLanguage), hasStatus(OK)
            .and(hasBody("Hi")))
    }

}
