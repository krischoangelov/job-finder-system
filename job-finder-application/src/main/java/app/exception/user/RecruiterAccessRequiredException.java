package app.exception.user;

import app.exception.ApplicationException;

public class RecruiterAccessRequiredException extends ApplicationException {

    public RecruiterAccessRequiredException(String id) {
        super("User with id: " + id + " is not a recruiter!", "400", "Only recruiters can view candidates");
    }
}
