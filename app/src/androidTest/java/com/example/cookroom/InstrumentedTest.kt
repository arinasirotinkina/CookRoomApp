package com.example.cookroom

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.cookroom", appContext.packageName)
    }
}

internal class RegistrationActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule<RegistrationActivity>(
        RegistrationActivity::class.java
    )
    @Test
    @Throws(Exception::class)
    fun clickLoginButton_opensLoginUi() {
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
    }
}
