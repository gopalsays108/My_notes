package com.example.mynotes;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.mynotes.utils.Validators;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.mynotes", appContext.getPackageName());
    }

    @Test
    public void testPassword() {
        String password = "ad@45#A4B@";
        String name = "Gopal";
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        boolean val = Validators.validatePassword(password, name, appContext);
        assertTrue(val);
    }
}