package com.dev.challenge.exception;

import com.dev.challenge.model.common.Error;

public class SessionNotFoundsException extends ParentException {

    public SessionNotFoundsException() {
        this.errorCode = Error.SESSION_NOT_FOUND.getCode();
        this.errorMessage = Error.SESSION_NOT_FOUND.getErrorMessage();
    }
}
