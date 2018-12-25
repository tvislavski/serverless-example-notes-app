package me.tvislavski.serverlessexample.ports.repository;

import io.vavr.control.Either;
import me.tvislavski.serverlessexample.model.Error;
import me.tvislavski.serverlessexample.model.user.Email;
import me.tvislavski.serverlessexample.model.user.User;

public interface UserRepository {

    Either<Error, User> getUserBy(Email email);

    Either<Error, User> save(User user);
}
