package com.example.mynotes.utils;


import org.junit.Assert;
import org.junit.Test;

public class ValidatorsTest {

    @Test
    public void validateCorrectPassword() {
        String password = "ad@45#A4B@";
        String name = "Gopal";
        boolean val = Validators.validatePassword(password, name, null);
        Assert.assertTrue(val);
    }

    @Test
    public void validateWrongPassword() {
        String password = "Ad@45#gopalB@";
        String name = "Gopal";
        boolean val = Validators.validatePassword(password, name, null);
        Assert.assertFalse(val);
    }

    @Test
    public void validateCorrectPasswordMobileNumber() {
        String phoneNumber = "9971705774";
        boolean val = Validators.validateMobileNumber(phoneNumber);
        Assert.assertTrue(val);
    }

    @Test
    public void validateWrongPasswordMobileNumber() {
        String phoneNumber = "971705774";
        boolean val = Validators.validateMobileNumber(phoneNumber);
        Assert.assertFalse(val);
    }

    @Test
    public void validateEmailId() {
        String email = "gopalsays@gmail.com";
        boolean val = Validators.validateEmailId(email);
        Assert.assertTrue(val);
    }
}