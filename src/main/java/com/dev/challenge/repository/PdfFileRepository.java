package com.dev.challenge.repository;

import com.dev.challenge.model.entity.PdfFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfFileRepository extends MongoRepository<PdfFile, String> {

    PdfFile findByFileName(String fileName);
}
