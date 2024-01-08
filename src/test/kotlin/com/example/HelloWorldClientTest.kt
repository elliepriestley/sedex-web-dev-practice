package com.example

import com.natpryce.hamkrest.assertion.assertThat
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

    // test that client.echo_headers returns in json when json param specified

    // test that client.echo_headers returns correct value when additional prefix header ?as_response_headers_with_prefix=  used as parameter.
}