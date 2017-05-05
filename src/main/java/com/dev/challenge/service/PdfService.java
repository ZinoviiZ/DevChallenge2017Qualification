package com.dev.challenge.service;

import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.parser.ParseWorker;
import com.dev.challenge.parser.PdfData;
import com.dev.challenge.util.DataBaseSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class PdfService {

    @Autowired private DataBaseSaver dataBaseSaver;

    public MessageResponse parseAndSavePdf(List<MultipartFile> multipartFiles) throws IOException, ExecutionException, InterruptedException {

        List<PdfData> dataList = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<Future<List<PdfData>>> futures = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            Future<List<PdfData>> future = executor.submit(new ParseWorker(multipartFile));
            futures.add(future);
        }
        for (Future<List<PdfData>> processedData : futures) {
            dataList.addAll(processedData.get());
        }
        dataBaseSaver.buildSessions(dataList);
        return new MessageResponse();
    }
}