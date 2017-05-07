package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.builder.SessionResponseBuilder;
import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.model.response.SessionResponse;
import com.dev.challenge.model.response.SessionsResponse;
import com.dev.challenge.parser.PdfData;
import com.dev.challenge.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService  extends ParentService {

    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionResponseBuilder sessionResponseBuilder;

    public MessageResponse getAllSessions() throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {

        List<Session> sessions = sessionRepository.findAll();
        if (sessions == null || sessions.isEmpty()) throw new SessionNotFoundsException();
        SessionsResponse sessionsResponse = new SessionsResponse();
        for (Session session : sessions) {
            sessionsResponse.add(linkBuilder.buildGetSessionLink(session.getId(), session.getSessionName()));
        }
        return new MessageResponse(sessionsResponse);
    }
    public MessageResponse getSessionById(String id) throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {

        Session session = sessionRepository.findOne(id);
        if (session == null) throw new SessionNotFoundsException();
        SessionResponse sessionResponse = sessionResponseBuilder.build(session);
        return new MessageResponse(sessionResponse);
    }

    public Session getSession(String cityCouncil, String assembly, String sessionName, String sessionDate) {

        Session session = sessionRepository.findByCityCouncilAndAssemblyAndSessionName(cityCouncil, assembly, sessionName);
        if (session == null) {
            session = saveSassion(cityCouncil, assembly, sessionName, sessionDate);
        }
        return session;
    }

    public Session saveSassion(String cityCouncil, String assembly, String sessionName, String sessionDate) {

        Session session = new Session();
        session.setId(generator.generateId());
        session.setCityCouncil(cityCouncil);
        session.setAssembly(assembly);
        session.setSessionName(sessionName);
        session.setSessionDate(sessionDate);
        sessionRepository.save(session);
        return session;
    }

    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

    public Session.VotingNumber getSessionVotingNumber(Session session, PdfData pdfData) {
        for (Session.VotingNumber votingNumber : session.getVotingNumbers()) {
            if (votingNumber.getVotingNumber().equals(pdfData.getVotingNumber()))
                return votingNumber;
        }
        Session.VotingNumber votingNumber = new Session.VotingNumber();
        votingNumber.setId(generator.generateId());
        votingNumber.setVotingNumber(pdfData.getVotingNumber());
        session.getVotingNumbers().add(votingNumber);
        return votingNumber;
    }

    public Session getSessionByVotingId(String votingId) throws DelegateNofFoundException {

        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            for (Session.VotingNumber votingNumber : session.getVotingNumbers()) {
                if (votingNumber.getVotingIds().contains(votingId)) return session;
            }
        }
        throw new DelegateNofFoundException();
    }
}
