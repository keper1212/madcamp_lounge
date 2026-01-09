package com.example.madcamp_lounge.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordHashGenerator {
    private PasswordHashGenerator() {
    }

    public static void main(String[] args) {
        if (args.length != 1 || args[0].isBlank()) {
            System.out.println("Usage: java PasswordHashGenerator \"plainPassword\"");
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode(args[0]));
    }
}
