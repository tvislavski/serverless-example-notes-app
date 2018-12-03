package me.tvislavski.serverlessexample.service;

import io.vavr.control.Either;
import me.tvislavski.serverlessexample.domain.Error;
import me.tvislavski.serverlessexample.domain.user.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private Email validEmail = Email.from("test@example.com").get();
    private Note validNote = Note.builder().withText("Some text").build().get();

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setUp() {
        userService = UserService.from(userRepository).get();
        Mockito.when(userRepository.save(Mockito.any()))
                .thenAnswer(invocationOnMock -> Either.<Error, User>right((User) invocationOnMock.getArguments()[0]));
    }

    @Test
    public void testGetUser() {
        Mockito.when(userRepository.getUserBy(Mockito.any())).thenReturn(Either.right(User.from(validEmail).get()));
        assertThat(userService.getUserBy(validEmail)).isRight();
    }

    @Test
    public void testGetNullUser() {
        assertThat(userService.getUserBy(null)).isLeft();
    }

    @Test
    public void testUpdateUserWithNullEmail() {
        assertThat(userService.updateUserWithNote(null, validNote)).isLeft();
    }

    @Test
    public void testUpdateUserWithNullNote() {
        assertThat(userService.updateUserWithNote(validEmail, null)).isLeft();
    }

    @Test
    public void testUpdateUser() {
        var user = User.from(validEmail, List.of(validNote)).get();
        Mockito.when(userRepository.getUserBy(Mockito.any())).thenReturn(Either.right(user));
        var result = userService.updateUserWithNote(validEmail, Note.builder().withText("Bla bla").build().get());
        assertThat(result).isRight();
        Assert.assertEquals(2, result.get().getNotes().size());
    }

    @Test
    public void testUpdateNewUser() {
        Mockito.when(userRepository.getUserBy(validEmail)).thenReturn(Either.left(new UserNotFound(validEmail)));
        var result = userService.updateUserWithNote(validEmail, validNote);
        assertThat(result).isRight();
        Assert.assertEquals(validEmail, result.get().getEmail());
        Assert.assertEquals(1, result.get().getNotes().size());
    }

    @Test
    public void testUpdateUserWithOldNote() {
        var user = User.from(validEmail, List.of(validNote)).get();
        Mockito.when(userRepository.getUserBy(Mockito.any())).thenReturn(Either.right(user));
        var result = userService.updateUserWithNote(validEmail, Note.builder()
                .withText("Bla bla")
                .withDateCreated(validNote.getCreated())
                .build().get());
        assertThat(result).isRight();
        Assert.assertEquals(1, result.get().getNotes().size());
    }
}
