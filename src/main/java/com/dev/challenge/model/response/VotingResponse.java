package com.dev.challenge.model.response;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
public class VotingResponse extends ResponseData {

    private List<Vote> votes;
    private String votingGoal;
    private String result;

    @Data
    public static class Vote extends ResourceSupport{

        private String vote;
    }
}
