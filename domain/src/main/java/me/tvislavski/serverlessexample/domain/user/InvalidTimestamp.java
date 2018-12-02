package me.tvislavski.serverlessexample.domain.user;

import me.tvislavski.serverlessexample.domain.Error;

import java.util.Date;

public class InvalidTimestamp extends Error {

    public InvalidTimestamp(Date timestamp) {
        super("Invalid timestamp " + timestamp);
    }
}
