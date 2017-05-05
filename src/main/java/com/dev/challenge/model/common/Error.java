package com.dev.challenge.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {

    INTERNAL_SERVER_ERROR(1, "Internal server error"),

    PARSE_PDF_ERROR(10, "Pdf parsing error. Pdf file Malformed"),

    OPEN_PDF_ERROR(11, "Pdf opening error"),

    SESSION_NOT_FOUND(20, "Session is not found"),

    VOTING_NOT_FOUND(30, "Voting is not found"),

    DELEGATE_NOT_FOUND(40, "Delegate is not found");

    private Integer code;
    private String errorMessage;
}
