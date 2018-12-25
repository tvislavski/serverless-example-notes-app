package me.tvislavski.serverlessexample.model.user;

import me.tvislavski.serverlessexample.model.Error;

public class InvalidEmail extends Error {

    public InvalidEmail(String email) {
        super("Invalid email " + email);
    }
}
