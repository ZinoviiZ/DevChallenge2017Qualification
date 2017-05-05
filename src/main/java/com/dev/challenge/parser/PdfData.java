package com.dev.challenge.parser;

import com.dev.challenge.model.enums.VoteValue;
import com.dev.challenge.model.enums.VotingResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PdfData {

    private String cityCouncil;
    private String sessionName;
    private String assembly;
    private String sessionDate;
    private String agenda;
    private String votingNumber;
    private String votingGoal;
    private List<Vote> votes;
    private Integer countFor;
    private Integer countAgainst;
    private Integer countAbstain;
    private Integer countPass;
    private Integer countAbsent;
    private VotingResult result;

    @Data
    @AllArgsConstructor
    public static class Vote {

        private String delegateName;
        private VoteValue voteValue;
    }
}
