package com.example.wsamad4

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginActivityTest {
    private lateinit var scenario: ActivityScenario<LoginActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(LoginActivity::class.java)
    }


    @Test
    fun test_isActivityInView() {
        onView(withId(R.id.loginScreen)).check(matches(isDisplayed()))
    }

    @Test
    fun test_if_both_edit_text_are_empty() {
        onView(withId(R.id.btnSignInLogin)).perform(scrollTo(), click())
        onView(withId(R.id.txtAlert)).check(matches(withText("Any field can't be empty")))
    }

    @Test
    fun test_if_email_edit_text_is_empty() {
        onView(withId(R.id.edtPassword)).perform(typeText("1234"))
        onView(withId(R.id.btnSignInLogin)).perform(scrollTo(), click())
        onView(withId(R.id.txtAlert)).check(matches(withText("Any field can't be empty")))
    }

    @Test
    fun test_if_password_edit_text_is_empty() {
        onView(withId(R.id.edtLogin)).perform(typeText("healthy@wsa.com"))
        onView(withId(R.id.btnSignInLogin)).perform(scrollTo(), click())
        onView(withId(R.id.txtAlert)).check(matches(withText("Any field can't be empty")))
    }

    @Test
    fun test_if_email_is_different_with_the_regex() {
        onView(withId(R.id.edtLogin)).perform(typeText("healthy@gmail.com"))
        onView(withId(R.id.edtPassword)).perform(scrollTo(), typeText("1234"))
        onView(withId(R.id.btnSignInLogin)).perform(scrollTo(), click())
        onView(withId(R.id.txtAlert)).check(matches(withText("The Email Must have an email format")))
    }

//    @Test
//    fun test_if_edit_texts_are_correct_but_wrong_credentials() {
//        onView(withId(R.id.edtLogin)).perform(typeText("healthy@wsa.com"))
//        onView(withId(R.id.edtPassword)).perform(scrollTo(), typeText("12345"))
//        onView(withId(R.id.btnSignInLogin)).perform(scrollTo(), click())
////        onView(withId(R.id.txtAlert)).check(matches(withText("Wrong Credentials!")))
//    }
//
//    @Test
//    fun test_if_edit_texts_are_correct() {
//        onView(withId(R.id.edtLogin)).perform(typeText("healthy@wsa.com"))
//        onView(withId(R.id.edtPassword)).perform(scrollTo(), typeText("1234"))
//        onView(withId(R.id.btnSignInLogin)).perform(scrollTo(), click())
//        onView(withId(R.id.homeScreen)).check(matches(isDisplayed()))
//    }

}