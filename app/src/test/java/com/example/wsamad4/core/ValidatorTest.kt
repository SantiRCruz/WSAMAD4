package com.example.wsamad4.core

import org.junit.Assert.*

import org.junit.Test

class ValidatorTest {

    @Test
    fun `validate email is empty and return false`() {
        val s = null
        val res = Validator.validateEmail(s)
        assertEquals(false,res)
    }

    @Test
    fun `validate email is different to the regex and return false`() {
        val s = "healthy@gmail.com"
        val res = Validator.validateEmail(s)
        assertEquals(false,res)
    }
    @Test
    fun `validate email is correct and return true`() {
        val s = "healthy@wsa.com"
        val res = Validator.validateEmail(s)
        assertEquals(true,res)
    }
    @Test
    fun `validate password is empty and return false`() {
        val s = ""
        val res = Validator.validatePassword(s)
        assertEquals(false,res)
    }
    @Test
    fun `validate password is null and return false`() {
        val s = null
        val res = Validator.validatePassword(s)
        assertEquals(false,res)
    }
    @Test
    fun `validate password is correct and return true`() {
        val s = "1234"
        val res = Validator.validatePassword(s)
        assertEquals(true,res)
    }
}