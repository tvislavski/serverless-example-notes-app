package me.tvislavski.serverlessexample.domain.user;


import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.tvislavski.serverlessexample.domain.ArgumentEmpty;
import me.tvislavski.serverlessexample.domain.Error;

import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class Email {

    public static final String REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private String value;

    public String asString() {
        return value;
    }

    public static Either<Error, Email> from(String value) {
        if (value == null || value.isEmpty()) return Either.left(new ArgumentEmpty("Email.value"));
        if (!Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE).matcher(value).matches())
            return Either.left(new InvalidEmail(value));
        return Either.right(new Email(value));
    }
}
