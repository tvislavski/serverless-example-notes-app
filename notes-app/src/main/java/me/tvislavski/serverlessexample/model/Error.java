package me.tvislavski.serverlessexample.model;

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
