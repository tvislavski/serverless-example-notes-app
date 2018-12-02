package me.tvislavski.serverlessexample.domain.user;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static me.tvislavski.serverlessexample.domain.user.TestObjects.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class UserTest {

    @Test
    public void testValidUserCreation() {
        assertThat(User.from(Email.from(email).get())).isRight();
    }

    @Test
    public void testUserWithNullEmailCreation() {
        assertThat(User.from(null)).isLeft();
    }

    @Test
    public void testValidUserWithNoteCreation() {
        assertThat(User.from(
                Email.from(email).get(),
                List.of(Note.builder().withText(noteText).build().get()))
        ).isRight();
    }

    @Test
    public void testValidUserWithNullNotesCreation() {
        assertThat(User.from(
                Email.from(email).get(),
                null)
        ).isLeft();
    }

    @Test
    public void testValidUserWithNullNoteCreation() {
        var notes = new LinkedList<Note>();
        notes.add(null);
        assertThat(User.from(
                Email.from(email).get(),
                notes)
        ).isLeft();
    }

    @Test
    public void testUserEqualityByEmail() {
        var user1 = User.from(Email.from(email).get()).get();
        var user2 = User.from(Email.from(email).get(),
                List.of(Note.builder().withText(noteText).build().get())).get();
        Assert.assertEquals(user1, user2);
    }

    @Test
    public void testUpdateUserWithNullNote() {
        var user = User.from(Email.from(email).get()).get();
        user.updateWithNote(null);
        Assert.assertEquals(0, user.getNotes().size());
    }

    @Test
    public void testUpdateUserWithNewNote() {
        var user = User.from(Email.from(email).get()).get();
        user.updateWithNote(Note.builder().withText(noteText).build().get());
        Assert.assertEquals(1, user.getNotes().size());
    }

    @Test
    public void testUpdateUserWithOldNote() {
        var note = Note.builder().withText(noteText).build().get();
        var user = User.from(Email.from(email).get(), List.of(note)).get();
        user.updateWithNote(note);

        note = Note.builder().withText("New text").withDateCreated(note.getCreated()).build().get();
        user.updateWithNote(note);

        Assert.assertEquals(1, user.getNotes().size());
        Assert.assertEquals(note, user.getNotes().get(0));
    }
}
