package com.dev.challenge.model.response;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
public class SessionResponse extends ResponseData {

    private String cityCouncil;
    private String assembly;
    private String sessionName;
    private String sessionDate;
    private List<VotingNumber> votingNumbers;

    @Data
    public static class VotingNumber extends ResourceSupport {

        private String votingNumber;
    }
}
