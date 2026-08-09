package app.exception.joboffer;

import app.exception.ApplicationException;

import java.util.UUID;

public class JobOfferNotFound extends ApplicationException {

    public JobOfferNotFound(UUID jobOfferId) {
        super("Job offer with id" + jobOfferId + " was not found.",
                "404",
                "Job offer not found");
    }
}
