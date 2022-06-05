package com.example.mynotes.utils;

import androidx.annotation.Keep;

import com.lambdapioneer.argon2kt.Argon2Kt;
import com.lambdapioneer.argon2kt.Argon2KtResult;
import com.lambdapioneer.argon2kt.Argon2Mode;

import java.security.SecureRandom;

public class PasswordHash {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    @Keep
    public static String hashPassword(String password, String salt){
        final Argon2Kt argon2Kt = new Argon2Kt();
        final Argon2KtResult result = argon2Kt.hash(Argon2Mode.ARGON2_I, password.getBytes(),salt.getBytes());
        return result.encodedOutputAsString();
    }

    public static boolean verifyHashPassword(String password,String encodedOutput,String salt){
        final Argon2Kt argon2Kt = new Argon2Kt();
        return argon2Kt.verify(Argon2Mode.ARGON2_I, encodedOutput, password.getBytes());
    }

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }
}
