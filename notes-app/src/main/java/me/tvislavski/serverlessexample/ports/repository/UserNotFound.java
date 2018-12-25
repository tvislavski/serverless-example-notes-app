package me.tvislavski.serverlessexample.ports.repository;

import me.tvislavski.serverlessexample.model.Error;
import me.tvislavski.serverlessexample.model.user.Email;

public class UserNotFound extends Error {

    public UserNotFound(Email email) {
        super("User " + email + " not found");
    }
}
