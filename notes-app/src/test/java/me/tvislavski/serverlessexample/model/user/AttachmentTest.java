package me.tvislavski.serverlessexample.model.user;

import org.junit.Test;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class AttachmentTest {

    @Test
    public void testValidAttachmentCreation() {
        assertThat(Attachment.from(TestObjects.imageBytes)).isRight();
    }

    @Test
    public void testInvalidAttachmentCreation() {
        assertThat(Attachment.from(null)).isLeft();
    }
}
