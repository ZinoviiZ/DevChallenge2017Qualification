package com.dev.challenge.exception;

import lombok.Getter;

@Getter
public abstract class ParentException extends Exception {

    Integer errorCode;
    String errorMessage;
}
