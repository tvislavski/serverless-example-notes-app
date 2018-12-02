package me.tvislavski.serverlessexample.domain.user;

import me.tvislavski.serverlessexample.domain.Error;

public class InvalidEmail extends Error {

    public InvalidEmail(String email) {
        super("Invalid email " + email);
    }
}
