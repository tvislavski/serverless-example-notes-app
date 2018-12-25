package me.tvislavski.serverlessexample.model;

public class ArgumentEmpty extends Error {

    public ArgumentEmpty(String argumentName) {
        super("Empty argument " + argumentName);
    }
}
