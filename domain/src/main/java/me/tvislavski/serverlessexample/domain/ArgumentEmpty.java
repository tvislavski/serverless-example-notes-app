package me.tvislavski.serverlessexample.domain;

public class ArgumentEmpty extends Error {

    public ArgumentEmpty(String argumentName) {
        super("Empty argument " + argumentName);
    }
}
