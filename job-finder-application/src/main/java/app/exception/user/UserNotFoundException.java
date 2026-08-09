package app.exception.user;

import app.exception.ApplicationException;

import java.util.UUID;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException(UUID id) {
        super(
                "User with id" + id + " was not found.",
                "404",
                "User not found"
        );
    }
}
