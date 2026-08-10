package com.jobfinder.interviewschedulingservice.exception;

import org.springframework.http.HttpStatus;

public class InterviewNotFoundException extends APIException {
    public InterviewNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
