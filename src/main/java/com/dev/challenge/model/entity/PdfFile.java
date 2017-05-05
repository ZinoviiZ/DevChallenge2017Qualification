package com.dev.challenge.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pdf_file")
public class PdfFile {

    @Id
    private String id;

    private String fileName;
}
