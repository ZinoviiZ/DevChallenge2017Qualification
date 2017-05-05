package com.dev.challenge.util;

import com.dev.challenge.model.entity.Delegate;
import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.entity.Session.VotingNumber;
import com.dev.challenge.model.entity.Voting;
import com.dev.challenge.model.entity.Voting.Vote;
import com.dev.challenge.model.enums.VoteValue;
import com.dev.challenge.parser.PdfData;
import com.dev.challenge.repository.DelegateRepository;
import com.dev.challenge.repository.SessionRepository;
import com.dev.challenge.repository.VotingRepository;
import com.dev.challenge.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataBaseSaver {

    @Autowired private Generator generator;
    @Autowired private DelegateRepository delegateRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private VotingRepository votingRepository;

    @Transactional
    public void buildSessions(List<PdfData> pdfDatas) {

        for (PdfData pdfData : pdfDatas) {
            Session session = getSession(pdfData.getCityCouncil(), pdfData.getAssembly(), pdfData.getSessionName(), pdfData.getSessionDate());
            VotingNumber votingNumber = getSessionVotingNumber(session, pdfData);
            Voting voting = buildVoting(pdfData);
            votingNumber.getVotingIds().add(voting.getId());
            votingRepository.save(voting);
            sessionRepository.save(session);
        }
    }

    private Session getSession(String cityCouncil, String assembly, String sessionName, String sessionDate) {
        Session session = sessionRepository.findByCityCouncilAndAssemblyAndSessionName(cityCouncil, assembly, sessionName);
        if (session == null) {
            session = new Session();
            session.setId(generator.generateId());
            session.setCityCouncil(cityCouncil);
            session.setAssembly(assembly);
            session.setSessionName(sessionName);
            session.setSessionDate(sessionDate);
            sessionRepository.save(session);
        }
        return session;
    }

    private VotingNumber getSessionVotingNumber(Session session, PdfData pdfData) {
        for (VotingNumber votingNumber : session.getVotingNumbers()) {
            if (votingNumber.getVotingNumber().equals(pdfData.getVotingNumber()))
                return votingNumber;
        }
        VotingNumber votingNumber = new VotingNumber();
        votingNumber.setId(generator.generateId());
        votingNumber.setVotingNumber(pdfData.getVotingNumber());
        session.getVotingNumbers().add(votingNumber);
        return votingNumber;
    }

    private Voting buildVoting(PdfData pdfData) {
        Voting voting = new Voting();
        voting.setId(generator.generateId());
        voting.setGoal(pdfData.getVotingGoal());
        List<Delegate> delegates = new ArrayList<>();
        for (PdfData.Vote dataVote : pdfData.getVotes()) {
            Delegate delegate = buildDelegate(dataVote.getDelegateName());
            VoteValue voteValue = dataVote.getVoteValue();
            Vote vote = new Vote(delegate.getId(), voteValue);
            switch (voteValue) {
                case For:
                    delegate.getForVotingIds().add(voting.getId());
                    break;
                case Absent:
                    delegate.getAbsentVotingIds().add(voting.getId());
                    break;
                case Abstain:
                    delegate.getAbstainVotingIds().add(voting.getId());
                    break;
                case Against:
                    delegate.getAgainstVotingIds().add(voting.getId());
                    break;
                case Pass:
                    delegate.getPassVotingIds().add(voting.getId());
                    break;
            }
            voting.getVotes().add(vote);
            delegates.add(delegate);
        }
        voting.setResult(pdfData.getResult());
        voting.setCountFor(pdfData.getCountFor());
        voting.setCountPass(pdfData.getCountPass());
        voting.setCountAbsent(pdfData.getCountAbsent());
        voting.setCountAbstain(pdfData.getCountAbstain());
        voting.setCountAgainst(pdfData.getCountAgainst());
        delegateRepository.save(delegates);
        return voting;
    }

    private Delegate buildDelegate(String name) {
        name = name.replaceAll(" ", "_").toUpperCase();
        Delegate delegate = delegateRepository.findByName(name);
        if (delegate == null) {
            delegate = new Delegate(generator.generateId(), name);
            delegateRepository.save(delegate);
        }
        return delegate;
    }
}
