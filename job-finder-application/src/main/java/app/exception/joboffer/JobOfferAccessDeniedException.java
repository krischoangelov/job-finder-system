package app.exception.joboffer;

import app.exception.ApplicationException;

public class JobOfferAccessDeniedException extends ApplicationException {
    public JobOfferAccessDeniedException(String id) {
        super("User with id: " + id + "has no authorization to configure this job offer",
                "403",
                "You are not allowed to configure this job offer");
    }
}
