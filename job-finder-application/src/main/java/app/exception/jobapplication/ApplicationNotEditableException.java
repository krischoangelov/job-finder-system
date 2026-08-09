package app.exception.jobapplication;

import app.exception.ApplicationException;

import java.util.UUID;

public class ApplicationNotEditableException extends ApplicationException {
    public ApplicationNotEditableException(UUID jobAppId) {
        super("Job offer with id: " + jobAppId + " does not have pending status" , "409", "Only pending applications can be edited");
    }
}
