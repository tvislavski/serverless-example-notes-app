package me.tvislavski.serverlessexample.model.user;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static me.tvislavski.serverlessexample.model.user.TestObjects.imageBytes;
import static me.tvislavski.serverlessexample.model.user.TestObjects.noteText;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class NoteTest {

    @Test
    public void testValidNoteCreation() {
        assertThat(Note.builder().withText(noteText).build()).isRight();
    }

    @Test
    public void testValidNoteWithAttachmentCreation() {
        assertThat(Note.builder()
                .withText(noteText)
                .withAttachments(List.of(Attachment.from(imageBytes).get()))
                .build()).isRight();
    }

    @Test
    public void testValidNoteWithCreatedDateCreation() {
        assertThat(Note.builder()
                .withText(noteText)
                .withDateCreated(Calendar.getInstance().getTime())
                .build()).isRight();
    }

    @Test
    public void testInvalidNoteWithCreatedDateCreation() {
        var timestamp = Calendar.getInstance();
        timestamp.add(Calendar.HOUR, 1);
        assertThat(Note.builder()
                .withText(noteText)
                .withDateCreated(timestamp.getTime())
                .build()).isLeft();
    }

    @Test
    public void testNoteWithNullAttachmentsCreation() {
        assertThat(Note.builder().withText(noteText).withAttachments(null).build()).isRight();
    }

    @Test
    public void testNoteWithNullAttachmentCreation() {
        var attachments = new LinkedList<Attachment>();
        attachments.add(null);
        assertThat(Note.builder().withText(noteText).withAttachments(attachments).build()).isLeft();
    }

    @Test
    public void testNoteEquality() {
        var note1 = Note.builder().withText(noteText).withAttachments(List.of(Attachment.from(imageBytes).get())).build().get();
        var note2 = Note.builder().withText(noteText).withAttachments(List.of(Attachment.from(imageBytes).get())).build().get();
        Assert.assertEquals(note1, note2);
    }

    @Test
    public void testNoteInequality() {
        var note1 = Note.builder().withText(noteText).withAttachments(List.of(Attachment.from(imageBytes).get())).build().get();
        var note2 = Note.builder().withText(noteText).build().get();
        Assert.assertNotEquals(note1, note2);
    }
}
