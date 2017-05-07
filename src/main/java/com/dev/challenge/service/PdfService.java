package com.dev.challenge.service;

import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.entity.Voting;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.parser.ParseWorker;
import com.dev.challenge.parser.PdfData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class PdfService extends ParentService {

    @Autowired private SessionService sessionService;
    @Autowired private VotingService votingService;

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
        savePdf(dataList);
        return new MessageResponse();
    }

    @Transactional
    private void savePdf(List<PdfData> pdfDatas) {

        for (PdfData pdfData : pdfDatas) {
            Session session = sessionService.getSession(pdfData.getCityCouncil(), pdfData.getAssembly(), pdfData.getSessionName(), pdfData.getSessionDate());
            Session.VotingNumber votingNumber = sessionService.getSessionVotingNumber(session, pdfData);
            Voting voting = votingService.saveVoting(pdfData.getVotingGoal(), pdfData.getVotes(), pdfData.getResult());
            votingNumber.getVotingIds().add(voting.getId());
            sessionService.saveSession(session);
        }
    }
}