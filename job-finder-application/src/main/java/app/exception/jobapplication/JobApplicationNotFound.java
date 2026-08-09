package app.exception.jobapplication;

import app.exception.ApplicationException;

import java.util.UUID;

public class JobApplicationNotFound extends ApplicationException {
    public JobApplicationNotFound(UUID jobAppId) {
        super("Job application with id" + jobAppId + " was not found.",
                "404",
                "Job application not found");
    }
}
