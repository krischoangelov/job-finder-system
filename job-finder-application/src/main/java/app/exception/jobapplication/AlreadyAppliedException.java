package app.exception.jobapplication;

import app.exception.ApplicationException;

public class AlreadyAppliedException extends ApplicationException {
    public AlreadyAppliedException(String id) {
        super("User with id: " + id + " has already applied for this job", "400", "You have already applied for this job");
    }
}
