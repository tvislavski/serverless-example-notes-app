package me.tvislavski.serverlessexample.domain.user;

import io.vavr.control.Either;
import me.tvislavski.serverlessexample.domain.Error;

public interface UserRepository {

    Either<Error, User> getUserBy(Email email);

    Either<Error, User> save(User user);
}
