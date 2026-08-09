package app.exception.jobapplication;

import app.exception.ApplicationException;

public class CandidateAccessRequiredException extends ApplicationException {
    public CandidateAccessRequiredException(String id) {
        super("User with id: " + id + " is not a candidate", "400", "Only candidates can apply for jobs");
    }
}
