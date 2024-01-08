package com.example

import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class HelloWorldClientTest {

    // test that client.hello returns Hello without name param
    @Test
    fun `Test that client function hello returns Hi when there is no name param or language param provided`() {
        val client = HelloWorldClient(baseURL = "http://localhost:9000")
        assertEquals(client.hello().bodyString(), "Hi" )

    }

    // test that client.hello returns Hello with different name params
    @Test
    fun `Test that client function hello returns Hi name when name parameter is provided`() {
        val client = HelloWorldClient("http://localhost:9000")
        assertEquals(client.hello(name="Ellie").bodyString(), "Hi Ellie")
        assertEquals(client.hello(name="Alice").bodyString(), "Hi Alice")
        assertEquals(client.hello(name="Leah").bodyString(), "Hi Leah")

    }

    // test that client.hello returns Hello in appropriate languages without name param

    // test that client.hello returns Hello in appropriate language with name param




    // test that client.echo_headers returns list of request headers as response body

    // test that client.echo_headers returns in json when json param specified

    // test that client.echo_headers returns correct value when additional prefix header ?as_response_headers_with_prefix=  used as parameter.
}