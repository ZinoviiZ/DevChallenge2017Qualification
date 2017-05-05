package com.dev.challenge.repository;

import com.dev.challenge.model.entity.Voting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends MongoRepository<Voting, String> {
}
