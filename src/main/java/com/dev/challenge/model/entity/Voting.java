package com.dev.challenge.model.entity;

import com.dev.challenge.model.enums.VoteValue;
import com.dev.challenge.model.enums.VotingResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "voting")
public class Voting {

    @Id
    private String id;
    private String goal;
    private List<Vote> votes = new ArrayList<>();
    private Integer countFor;
    private Integer countAgainst;
    private Integer countAbstain;
    private Integer countPass;
    private Integer countAbsent;
    private VotingResult result;

    @Data
    @AllArgsConstructor
    public static class Vote {

        private String delegateId;
        private VoteValue voteValue;
    }
}
