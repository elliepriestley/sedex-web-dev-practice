package com.example


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Response
import org.http4k.format.Json
import org.http4k.format.JsonType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled

class HelloWorldClientTest {

    // test that client.hello returns Hello without name param
    @Test
    fun `Test that client function hello returns Hi when there is no name param or language param provided`() {
        val client = HelloWorldClient(baseURL = "http://localhost:9000")
        assertEquals( "Hi", client.hello().bodyString())

    }

    // test that client.hello returns Hello with different name params
    @Test
    fun `Test that client function hello returns Hi name when name parameter is provided`() {
        val client = HelloWorldClient("http://localhost:9000")
        assertEquals("Hi Ellie", client.hello(name="Ellie").bodyString())
        assertEquals("Hi Alice", client.hello(name="Alice").bodyString())
        assertEquals("Hi Leah", client.hello(name="Leah").bodyString())

    }

    // test that client.hello returns Hello in appropriate languages without name param


    @Test
    fun `Test that client function hello returns a greeting in appropriate language, without name parameter`() {
        val client = HelloWorldClient("http://localhost:9000")
        assertEquals("Bonjour", client.hello(language="fr-FR").bodyString())
        assertEquals("Salve",  client.hello(language="it-IT").bodyString())
        assertEquals("Hello", client.hello(language="en-US").bodyString())
        assertEquals("G'day", client.hello(language="en-AU").bodyString())
        assertEquals( "Alright?", client.hello(language="en-GB").bodyString())

    }

    // test that client.hello returns Hello in appropriate language with name param
    @Test
    fun `Test that client function hello returns hello in appropriate language, with correct name param`() {
        val client = HelloWorldClient("http://localhost:9000")
        assertEquals("Bonjour Ellie", client.hello(name="Ellie", language="fr-FR").bodyString())
        assertEquals("Salve Leah",  client.hello(name="Leah", language="it-IT").bodyString())
        assertEquals("Hello, Alice", client.hello(name="Alice", language="en-US").bodyString())
        assertEquals("G'day Frank", client.hello(name="Frank", language="en-AU").bodyString())
        assertEquals( "Alright, Jules?", client.hello(name="Jules", language="en-GB").bodyString())
    }

    // test that client.echo_headers returns list of request headers as response body
    @Test
    fun `Test that client function echoHeadersAsString returns a string of request headers as response body`() {
        val client = HelloWorldClient("http://localhost:9000")
        val actual = client.echoHeadersAsString(listOf(Pair("Some X Header" , "some x value"), Pair("Some Y Header", "some y value")))

        assertEquals("Some X Header: some x value\nSome Y Header: some y value", actual)
    }

    // test that client.echo_headers returns in json when json param specified
    @Test
    fun `Test that client function echoHeadersAsJson returns a json of request headers as response body`() {
        val client = HelloWorldClient("http://localhost:9000")
        val actual: JsonNode = client.echoHeadersAsJson(listOf(Pair("Some X Header" , "some x value"), Pair("Some Y Header", "some y value")))

//        assert(actual)
//        val objectMapper = ObjectMapper()
//        val jsonObject = objectMapper(actual, Map::class.java)
        assertNotNull(actual)

        // There must be a better way of testing this? How can I assert the response body is a json object?
        //
    }

//    @Test
//    fun `Test that client function addPrefix adds the prefix to the appropriate response header`() {
//        val client = HelloWorldClient("http://localhost:9000")
//        val actualResponse: Response = client.addPrefix("X-Echo-")
//
//        assertThat(actualResponse ).hasHeader("X-Echo")
//    }








    // test that client.echo_headers returns correct value when additional prefix header ?as_response_headers_with_prefix=  used as parameter.
}