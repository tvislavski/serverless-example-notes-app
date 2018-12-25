package me.tvislavski.serverlessexample.adapters.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import me.tvislavski.serverlessexample.adapters.s3.S3Adapter;
import me.tvislavski.serverlessexample.domain.Error;
import me.tvislavski.serverlessexample.domain.user.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.vavr.control.Option.of;
import static java.util.stream.Collectors.toList;

@Slf4j
public class DynamoDbUserRepository implements UserRepository {

    private DynamoDBMapper mapper;
    private S3Adapter<Attachment> attachmentS3Adapter;

    public DynamoDbUserRepository(DynamoDBMapper mapper, S3Adapter<Attachment> attachmentS3Adapter) {
        this.mapper = mapper;
        this.attachmentS3Adapter = attachmentS3Adapter;
    }

    @Override
    public Either<Error, User> getUserBy(Email email) {
        return Try.of(() -> of(mapper.load(DynamoDbUser.class, email.asString()))).toEither()
                .mapLeft(throwable -> {
                    log.error("{}", throwable);
                    return new Error("DynamoDb error");
                })
                .flatMap(userOption -> userOption.toEither(new UserNotFound(email)))
                .flatMap(this::toDomain);
    }

    @Override
    public Either<Error, User> save(User user) {
        return fromDomain(user).flatMap(dbUser -> Try.of(() -> {
            mapper.save(dbUser);
            return user;
        }).toEither().mapLeft(throwable -> {
            log.error("{}", throwable);
            return new Error("DynamoDb error");
        }));
    }

    private Either<Error, User> toDomain(DynamoDbUser user) {
        var domainMail = Email.from(user.getEmail());
        var notesEither = of(user.getNotes()).map(dynamoDbNotes -> dynamoDbNotes.stream().map(this::toDomain)
                .collect(Collectors.toList())).getOrElse(List.of());
        var domainNotes = notesEither.stream().anyMatch(Either::isLeft) ?
                Either.<Error, List<Note>>left(notesEither.stream().filter(Either::isLeft).findFirst().get().getLeft()) :
                Either.<Error, List<Note>>right(notesEither.stream().map(Either::get).collect(Collectors.toList()));
        return Email.from(user.getEmail()).flatMap(email -> domainNotes)
                .flatMap(notes -> User.from(domainMail.get(), domainNotes.get()));
    }

    private Either<Error, Note> toDomain(DynamoDbNote note) {
        List<Either<Error, Attachment>> attachmentEithers = note.getAttachmentsS3ARNs().stream()
                .map(attachmentS3Adapter::load).collect(Collectors.toList());
        return attachmentEithers.stream().anyMatch(Either::isLeft) ?
                Either.left(attachmentEithers.stream().filter(Either::isLeft).findFirst().get().getLeft()) :
                Note.builder()
                        .withText(note.getText())
                        .withDateCreated(note.getCreated())
                        .withAttachments(attachmentEithers.stream().map(Either::get).collect(toList()))
                        .build();
    }

    private Either<Error, DynamoDbUser> fromDomain(User user) {
        DynamoDbUser dynamoDbUser = new DynamoDbUser();
        dynamoDbUser.setEmail(user.getEmail().asString());
        List<Either<Error, DynamoDbNote>> noteEithers = user.getNotes().stream()
                .map(note -> fromDomain(note, emailWithoutAt(user.getEmail()))).collect(toList());
        if (noteEithers.stream().anyMatch(Either::isLeft))
            return Either.left(noteEithers.stream().filter(Either::isLeft).findFirst().get().getLeft());
        dynamoDbUser.setNotes(noteEithers.stream().map(Either::get).collect(toList()));
        return Either.right(dynamoDbUser);
    }

    private Either<Error, DynamoDbNote> fromDomain(Note note, String subfolder) {
        List<Either<Error, String>> s3ARNs = note.getAttachments().stream()
                .map(attachment -> attachmentS3Adapter.upload(attachment, of(subfolder)))
                .collect(Collectors.toList());
        if (s3ARNs.stream().anyMatch(Either::isLeft))
            return Either.left(s3ARNs.stream().filter(Either::isLeft).findFirst().get().getLeft());
        DynamoDbNote dynamoDbNote = new DynamoDbNote();
        dynamoDbNote.setCreated(note.getCreated());
        dynamoDbNote.setText(note.getText());
        dynamoDbNote.setAttachmentsS3ARNs(s3ARNs.stream().map(Either::get).collect(toList()));
        return Either.right(dynamoDbNote);
    }

    private String emailWithoutAt(Email email) {
        return email.asString().replaceAll("@", "-");
    }
}
