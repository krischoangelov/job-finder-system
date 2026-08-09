package app.exception.user;

import app.exception.ApplicationException;

public class UserAlreadyExistsException extends ApplicationException {

    public UserAlreadyExistsException(String username) {
        super(
                "User with username: " + username + " already exists!",
                "409",
                "User Already Exists"
        );
    }
}
