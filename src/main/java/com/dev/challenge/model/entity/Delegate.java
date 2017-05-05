package com.dev.challenge.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "delegate")
public class Delegate {

    @Id
    private String id;
    private String name;
    private List<String> forVotingIds;
    private List<String> againstVotingIds;
    private List<String> abstainVotingIds;
    private List<String> passVotingIds;
    private List<String> absentVotingIds;


    public Delegate(String id, String name) {

        this.id = id;
        this.name = name;
        this.forVotingIds = new ArrayList<>();
        this.againstVotingIds = new ArrayList<>();
        this.abstainVotingIds = new ArrayList<>();
        this.passVotingIds = new ArrayList<>();
        this.absentVotingIds = new ArrayList<>();
    }
}
