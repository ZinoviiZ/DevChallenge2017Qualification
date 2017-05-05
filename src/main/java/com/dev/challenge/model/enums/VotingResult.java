package com.dev.challenge.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VotingResult {

    Approve("Прийнято"),
    Denied("Не прийнято");

    private String value;

    public static VotingResult getVoteResult(String value) {

        for (VotingResult votingResult : VotingResult.values()) {
            if (votingResult.getValue().toLowerCase().equals(value.toLowerCase())) return votingResult;
        }
        return null;
    }
}
