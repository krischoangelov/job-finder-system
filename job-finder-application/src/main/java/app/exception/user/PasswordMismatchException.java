package app.exception.user;

import app.exception.ApplicationException;

public class PasswordMismatchException extends ApplicationException {
    public PasswordMismatchException() {
        super(
                "Password and confirmPassword do not match",
                "400",
                "Passwords do not match"
        );
    }
}
