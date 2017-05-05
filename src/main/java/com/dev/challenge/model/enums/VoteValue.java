package com.dev.challenge.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteValue {

    For("За"),
    Against("Проти"),
    Abstain("Утримався"),
    Pass("Не голосував"),
    Absent("Відсутній");

    private String value;

    public static VoteValue getVoteValue(String value) {

        for (VoteValue voteValue : VoteValue.values()) {
            if (voteValue.getValue().equals(value)) return voteValue;
        }
        return null;
    }
}
