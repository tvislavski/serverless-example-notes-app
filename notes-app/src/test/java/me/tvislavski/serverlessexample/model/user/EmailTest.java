package me.tvislavski.serverlessexample.model.user;

import org.junit.Test;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class EmailTest {

    @Test
    public void testValidEmailCreation() {
        assertThat(Email.from(TestObjects.email)).isRight();
    }

    @Test
    public void testNullEmailCreation() {
        assertThat(Email.from(null)).isLeft();
    }

    @Test
    public void testEmptyEmailCreation() {
        assertThat(Email.from("")).isLeft();
    }

    @Test
    public void testInvalidEmailCreation() {
        assertThat(Email.from("test")).isLeft();
    }
}
