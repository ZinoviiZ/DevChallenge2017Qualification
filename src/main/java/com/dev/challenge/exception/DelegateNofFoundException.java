package com.dev.challenge.exception;

import com.dev.challenge.model.common.Error;

public class DelegateNofFoundException extends ParentException {

    public DelegateNofFoundException() {
        this.errorCode = Error.DELEGATE_NOT_FOUND.getCode();
        this.errorMessage = Error.DELEGATE_NOT_FOUND.getErrorMessage();
    }
}
