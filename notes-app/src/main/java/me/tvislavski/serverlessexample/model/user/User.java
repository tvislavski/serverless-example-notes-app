package me.tvislavski.serverlessexample.model.user;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.*;
import me.tvislavski.serverlessexample.model.ArgumentEmpty;
import me.tvislavski.serverlessexample.model.Error;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode(of = "email")
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Getter
    private Email email;
    private List<Note> notes;

    public List<Note> getNotes() {
        return notes.stream().sorted(Comparator.comparing(Note::getCreated)).collect(Collectors.toList());
    }

    public User updateWithNote(Note note) {
        if (note != null) {
            var existingNote = Option.ofOptional(notes.stream()
                    .filter(note1 -> note1.getCreated().equals(note.getCreated())).findFirst());
            if (existingNote.isDefined()) {
                notes.remove(existingNote.get());
            }
            notes.add(note);
        }
        return this;
    }

    public static Either<Error, User> from(Email email) {
        return from(email, new LinkedList<>());
    }

    public static Either<Error, User> from(Email email, List<Note> notes) {
        if (email == null) return Either.left(new ArgumentEmpty("User.email"));
        if (notes == null || notes.stream().anyMatch(Objects::isNull))
            return Either.left(new ArgumentEmpty("User.notes"));
        return Either.right(new User(email, new LinkedList<>(notes)));
    }
}
