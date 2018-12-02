package me.tvislavski.serverlessexample.domain.user;

import io.vavr.control.Either;
import lombok.*;
import me.tvislavski.serverlessexample.domain.ArgumentEmpty;
import me.tvislavski.serverlessexample.domain.Error;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Attachment {

    private byte[] data;

    public static Either<Error, Attachment> from(byte[] data) {
        if (data == null || data.length == 0) return Either.left(new ArgumentEmpty("Attachment.data"));
        return Either.right(new Attachment(data));
    }
}
