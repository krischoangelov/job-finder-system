package app.exception.jobapplication;

import app.exception.ApplicationException;

public class JobApplicationAccessDeniedException extends ApplicationException {

    public JobApplicationAccessDeniedException(String id) {
        super("User with id: " + id + "has no authorization to configure this job application",
                "403",
                "You are not allowed to configure this job application");
    }
}
