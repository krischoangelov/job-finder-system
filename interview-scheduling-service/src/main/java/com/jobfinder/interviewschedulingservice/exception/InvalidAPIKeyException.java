package com.jobfinder.interviewschedulingservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidAPIKeyException extends APIException {

    private HttpStatus httpStatus;

    public InvalidAPIKeyException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
