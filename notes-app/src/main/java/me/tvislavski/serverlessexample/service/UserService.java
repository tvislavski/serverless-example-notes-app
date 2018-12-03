package me.tvislavski.serverlessexample.service;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tvislavski.serverlessexample.domain.ArgumentEmpty;
import me.tvislavski.serverlessexample.domain.Error;
import me.tvislavski.serverlessexample.domain.user.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class UserService {

    private UserRepository userRepository;

    public Either<Error, User> getUserBy(Email email) {
        log.info("Loading user with email {}", email);
        if (email == null) return Either.left(new ArgumentEmpty("UserService.email"));

        return userRepository.getUserBy(email)
                .peek(user -> log.info("Successfully loaded user: {}", user))
                .peekLeft(error -> log.warn("Error loading user with email {}: {}", email, error));
    }

    public Either<Error, User> updateUserWithNote(Email userEmail, Note note) {
        log.info("Updating user with email {} with note {}", userEmail, note);
        if (userEmail == null) return Either.left(new ArgumentEmpty("UserService.userEmail"));
        if (note == null) return Either.left(new ArgumentEmpty("UserService.note"));

        return getUserFromRepositoryOrCreateNew(userEmail)
                .map(user -> user.updateWithNote(note))
                .flatMap(userRepository::save)
                .peek(user -> log.info("Updated user: {}", user))
                .peekLeft(error -> log.warn("Error while updating user {}: {}", userEmail, error));
    }

    private Either<Error, User> getUserFromRepositoryOrCreateNew(Email userEmail) {
        var userEither = getUserBy(userEmail);
        if (userEither.isLeft() && userEither.getLeft() instanceof UserNotFound) {
            return User.from(userEmail);
        }
        return userEither;
    }

    public static Either<Error, UserService> from(UserRepository userRepository) {
        if (userRepository == null) return Either.left(new ArgumentEmpty("UserService.userRepository"));
        return Either.right(new UserService(userRepository));
    }
}
