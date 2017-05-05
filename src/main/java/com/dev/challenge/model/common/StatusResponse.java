package com.dev.challenge.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum StatusResponse {

    SUCCESS(0),
    ERROR(1);

    private Integer value;
}
