package com.dev.challenge.exception;

import com.dev.challenge.model.common.Error;

public class PdfParseException extends ParentException {

    public PdfParseException() {
        this.errorCode = Error.PARSE_PDF_ERROR.getCode();
        this.errorMessage = Error.PARSE_PDF_ERROR.getErrorMessage();
    }
}
