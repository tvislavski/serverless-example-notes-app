package me.tvislavski.serverlessexample.model.user;

import me.tvislavski.serverlessexample.model.Error;

import java.util.Date;

public class InvalidTimestamp extends Error {

    public InvalidTimestamp(Date timestamp) {
        super("Invalid timestamp " + timestamp);
    }
}
