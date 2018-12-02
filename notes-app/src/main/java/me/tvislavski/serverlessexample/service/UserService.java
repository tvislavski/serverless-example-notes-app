package me.tvislavski.serverlessexample.service;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tvislavski.serverlessexample.domain.ArgumentEmpty;
import me.tvislavski.serverlessexample.domain.Error;
import me.tvislavski.serverlessexample.domain.user.Email;
import me.tvislavski.serverlessexample.domain.user.Note;
import me.tvislavski.serverlessexample.domain.user.User;
import me.tvislavski.serverlessexample.domain.user.UserRepository;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class UserService {

    private UserRepository userRepository;

    public Either<Error, User> getUserBy(Email email) {
        log.info("Loading user with email {}", email);
        if (email == null) return Either.left(new ArgumentEmpty("UserService.email"));

        return userRepository.getUserBy(email)
                .peek(user -> log.info("Successfully loaded user: {}", user))
                .peekLeft(error -> log.info("Error loading user with email {}: {}", email, error));
    }

    public Either<Error, User> updateUserWithNote(Email userEmail, Note note) {
        log.info("Updating user with email {} with note {}", userEmail, note);
        if (userEmail == null) return Either.left(new ArgumentEmpty("UserService.userEmail"));
        if (note == null) return Either.left(new ArgumentEmpty("UserService.note"));


    }

    public static Either<Error, UserService> from(UserRepository userRepository) {
        if (userRepository == null) return Either.left(new ArgumentEmpty("UserService.userRepository"));
        return Either.right(new UserService(userRepository));
    }
}
