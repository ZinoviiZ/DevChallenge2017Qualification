package com.dev.challenge.repository;

import com.dev.challenge.model.entity.Delegate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegateRepository extends MongoRepository<Delegate, String> {

    Delegate findByName(String name);
    List<Delegate> findByNameNot(String name);
}
