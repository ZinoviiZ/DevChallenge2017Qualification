package com.dev.challenge.repository;

import com.dev.challenge.model.entity.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

    Session findByCityCouncilAndAssemblyAndSessionName(String cityCouncil, String assembly, String sessionName);
}
