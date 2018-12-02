package me.tvislavski.serverlessexample.domain.user;

import java.io.IOException;

public class TestObjects {

    public static byte[] imageBytes;

    static {
        try {
            imageBytes = TestObjects.class.getResourceAsStream("/smile.png").readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String noteText = "Test note";

    public static String email = "test@example.com";
}
