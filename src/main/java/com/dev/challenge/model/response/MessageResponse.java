package com.dev.challenge.model.response;

import com.dev.challenge.model.common.Error;
import com.dev.challenge.model.common.StatusResponse;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse <T extends ResponseData> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
    private T data;

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
    private Status status;

    private String time;

    public MessageResponse() {
        this.status = new Status();
    }

    @JsonCreator
    public MessageResponse(@JsonProperty("data") T data) {

        this.status = new Status();
        this.data = data;
    }

    public MessageResponse(Error error) {
        this.status = new Status(error);
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Status {

        private Integer statusCode;
        private Integer errorCode;
        private String  errorMessage;

        public Status() {
            this.statusCode = StatusResponse.SUCCESS.getValue();
        }

        public Status(Error error) {

            this.statusCode = StatusResponse.ERROR.getValue();
            this.errorCode = error.getCode();
            this.errorMessage = error.getErrorMessage();
        }
    }
}
