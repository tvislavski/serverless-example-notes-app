package me.tvislavski.serverlessexample.domain.user;

import me.tvislavski.serverlessexample.domain.Error;

public class UserNotFound extends Error {

    public UserNotFound(Email email) {
        super("User " + email + " not found");
    }
}
