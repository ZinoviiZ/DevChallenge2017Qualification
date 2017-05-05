package com.dev.challenge.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DelegateResponse extends ResponseData {

    private String name;
    private Integer countVotingParticipate;
    private Double interestFor;
    private Double interestAgainst;
    private Double interestAbstain;
    private Double interestPass;
    private Double interestAbsent;
    private List<InfluenceZone> influenceZones;

    @Data
    public static class InfluenceZone extends ResourceSupport {

        private Double influence;
    }
}
