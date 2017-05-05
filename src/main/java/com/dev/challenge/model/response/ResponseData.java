package com.dev.challenge.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ResponseData extends ResourceSupport {
}
