package com.dev.challenge.exception;

import com.dev.challenge.model.common.Error;

public class VotingNotFoundException extends ParentException {

    public VotingNotFoundException() {
        this.errorCode = Error.VOTING_NOT_FOUND.getCode();
        this.errorMessage = Error.VOTING_NOT_FOUND.getErrorMessage();
    }
}
