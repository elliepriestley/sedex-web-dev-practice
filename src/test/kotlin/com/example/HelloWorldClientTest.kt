package com.example

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class HelloWorldClientTest {
    // Tests pass only when server is running... is this a case for mocking?


    // test that client.hello returns Hello without name param
    @Test
    fun `Test that client function hello returns Hi when there is no name param or language param provided`() {
        val client = HelloWorldClient(baseURL = "http://localhost:3000")
        assertEquals("Hi", client.sayHello())
    }



    // test that client.hello returns Hello with different name params
    @Test
    fun `Test that client function hello returns Hi with correct name when name parameter is provided`() {
        val client = HelloWorldClient("http://localhost:3000")
        assertEquals("Hi Ellie", client.sayHello(name="Ellie"))
        assertEquals("Hi Alice", client.sayHello(name="Alice"))
        assertEquals("Hi Leah", client.sayHello(name="Leah"))
    }


    // test that client.hello returns Hello in appropriate languages without name param
    @Test
    fun `Test that client function hello returns a greeting in appropriate language, without name parameter`() {
        val client = HelloWorldClient("http://localhost:3000")
        assertEquals("Bonjour", client.sayHello(language="fr-FR"))
        assertEquals("Salve",  client.sayHello(language="it-IT"))
        assertEquals("Hello", client.sayHello(language="en-US"))
        assertEquals("G'day", client.sayHello(language="en-AU"))
        assertEquals( "Alright?", client.sayHello(language="en-GB"))
    }

    // test that client.hello returns Hello in appropriate language with name param
    @Test
    fun `Test that client function hello returns hello in appropriate language, with correct name param`() {
        val client = HelloWorldClient("http://localhost:3000")
        assertEquals("Bonjour Ellie", client.sayHello(name="Ellie", language="fr-FR"))
        assertEquals("Salve Leah",  client.sayHello(name="Leah", language="it-IT"))
        assertEquals("Hello, Alice", client.sayHello(name="Alice", language="en-US"))
        assertEquals("G'day Frank", client.sayHello(name="Frank", language="en-AU"))
        assertEquals( "Alright, Jules?", client.sayHello(name="Jules", language="en-GB"))
    }

    // test that client.echo_headers returns list of request headers as response body
    @Test
    fun `Test that client function echoHeaders returns a string of request headers as response body`() {
        val client = HelloWorldClient("http://localhost:3000")
        val headers = listOf(Pair("Some-X-Header", "some x value"), Pair("Some-Y-Header", "some y value"))

        val actual = client.echoHeaders(headers)
        val expected = "Some-y-header: some y value\nSome-x-header: some x value"

        assertTrue(actual.contains(expected))
    }


    // test that client.echo_headers returns in json when json param specified
    @Test
    fun `Test that client function echoHeaders returns a string version of json object of request headers as response body`() {
        val client = HelloWorldClient("http://localhost:3000")
        val headersAcceptJson = listOf(Pair("X-Header", "x value"), Pair("Y-Header", "y value"), Pair("Accept", "application/json"))

        val actual = client.echoHeaders(headersAcceptJson)
        val expected = "{\"headers\":{\"Accept\":\"application/json\",\"Host\":\"localhost:3000\",\"User-agent\":\"Java-http-client/17.0.9\",\"Y-header\":\"y value\",\"X-header\":\"x value\",\"Content-length\":\"0\"}}"

        assertEquals(expected, actual)

    }

    @Test
    fun `Test that client function echoHeaders, when called with prefix param, adds the prefix to all response headers and returns them as string`() {
        val client = HelloWorldClient("http://localhost:3000")
        val headers = listOf(Pair("X-Header", "x value"), Pair("Y-Header", "y value"))

        val actual = client.echoHeaders(headers, "PREFIX-")

        assertTrue(actual.contains(("(prefix-x-header, x value), (prefix-y-header, y value)")))


    }


}