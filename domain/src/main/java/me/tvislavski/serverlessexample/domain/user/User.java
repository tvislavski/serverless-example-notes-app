package me.tvislavski.serverlessexample.domain.user;

import io.vavr.control.Either;
import lombok.*;
import me.tvislavski.serverlessexample.domain.ArgumentEmpty;
import me.tvislavski.serverlessexample.domain.Error;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(of = "email")
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Getter
    private Email email;
    private List<Note> notes;

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public static Either<Error, User> from(Email email, List<Note> notes) {
        if (email == null) return Either.left(new ArgumentEmpty("User.email"));
        if (notes == null || notes.isEmpty()) return Either.left(new ArgumentEmpty("User.notes"));
        return Either.right(new User(email, notes));
    }
}
