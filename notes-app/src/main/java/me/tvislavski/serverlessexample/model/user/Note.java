package me.tvislavski.serverlessexample.model.user;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.*;
import me.tvislavski.serverlessexample.model.ArgumentEmpty;
import me.tvislavski.serverlessexample.model.Error;

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

    public static Builder builder() { return new Builder(); }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        private Option<Date> created = Option.none();
        private Option<String> text = Option.none();
        private List<Attachment> attachments = new LinkedList<>();

        public Builder withDateCreated(Date created) {
            this.created = Option.of(created);
            return this;
        }

        public Builder withText(String text) {
            this.text = Option.of(text);
            return this;
        }

        public Builder withAttachments(List<Attachment> attachments) {
            if (attachments != null) {
                this.attachments.clear();
                this.attachments.addAll(attachments);
            }
            return this;
        }

        public Either<Error, Note> build() {
            var now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
            if (created.isEmpty()) created = Option.of(now);

            if (text.isEmpty() || text.get().isEmpty()) return Either.left(new ArgumentEmpty("Note.text"));
            if (attachments.stream().anyMatch(Objects::isNull))
                return Either.left(new ArgumentEmpty("Note.attachments"));
            if (created.get().after(now)) return Either.left(new InvalidTimestamp(created.get()));

            return Either.right(new Note(created.get(), text.get(), attachments));
        }
    }
}
