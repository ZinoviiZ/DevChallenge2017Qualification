package com.dev.challenge.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "session")
public class Session {

    @Id
    private String id;
    private String cityCouncil;
    private String assembly;
    private String sessionName;
    private String sessionDate;
    private List<VotingNumber> votingNumbers = new ArrayList<>();

    @Data
    public static class VotingNumber {

        private String id;
        private String votingNumber;
        private List<String> votingIds = new ArrayList<>();
    }
}
