package me.tvislavski.serverlessexample.domain;

public class Error {

    private String message;

    public Error(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
