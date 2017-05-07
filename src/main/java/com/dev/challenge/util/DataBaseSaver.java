package com.dev.challenge.util;

import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.entity.Session.VotingNumber;
import com.dev.challenge.model.entity.Voting;
import com.dev.challenge.parser.PdfData;
import com.dev.challenge.service.SessionService;
import com.dev.challenge.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataBaseSaver {

    @Autowired private SessionService sessionService;
    @Autowired private VotingService votingService;

    @Transactional
    public void buildSessions(List<PdfData> pdfDatas) {

        for (PdfData pdfData : pdfDatas) {
            Session session = sessionService.getSession(pdfData.getCityCouncil(), pdfData.getAssembly(), pdfData.getSessionName(), pdfData.getSessionDate());
            VotingNumber votingNumber = sessionService.getSessionVotingNumber(session, pdfData);
            Voting voting = votingService.saveVoting(pdfData.getVotingGoal(), pdfData.getVotes(), pdfData.getResult());
            votingNumber.getVotingIds().add(voting.getId());
            votingService.saveVoting(voting);
            sessionService.saveSession(session);
        }
    }
}
