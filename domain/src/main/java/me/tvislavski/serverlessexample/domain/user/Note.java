package me.tvislavski.serverlessexample.domain.user;

import io.vavr.control.Either;
import lombok.*;
import me.tvislavski.serverlessexample.domain.ArgumentEmpty;
import me.tvislavski.serverlessexample.domain.Error;

import java.util.*;

@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Note {

    private Date created;
    private String text;
    private List<Attachment> attachments;

    public List<Attachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public static Either<Error, Note> from(String text) {
        return from(text, List.of());
    }

    public static Either<Error, Note> from(String text, List<Attachment> attachments) {
        if (text == null || text.isEmpty()) return Either.left(new ArgumentEmpty("Note.text"));
        if (attachments == null) return Either.left(new ArgumentEmpty("Note.attachments"));
        var created = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
        return Either.right(new Note(created, text, attachments));
    }
}
