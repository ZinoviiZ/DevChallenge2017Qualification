package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.common.RestUrl;
import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.model.response.SessionResponse;
import com.dev.challenge.model.response.SessionsResponse;
import com.dev.challenge.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dev.challenge.model.response.SessionResponse.VotingNumber;

@Service
public class SessionService {

    @Autowired private SessionRepository sessionRepository;

    public MessageResponse getAllSessions() throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {

        List<Session> sessions = sessionRepository.findAll();
        if (sessions == null || sessions.isEmpty()) throw new SessionNotFoundsException();
        SessionsResponse sessionsResponse = new SessionsResponse();
        for (Session session : sessions) {
            sessionsResponse.add(RestUrl.buildGetSessionLink(session.getId(), session.getSessionName()));
        }
        return new MessageResponse(sessionsResponse);
    }
    public MessageResponse getSessionById(String id) throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {

        Session session = sessionRepository.findOne(id);
        if (session == null) throw new SessionNotFoundsException();
        SessionResponse sessionResponse = buildSessionResponse(session);
        return new MessageResponse(sessionResponse);
    }

    private SessionResponse buildSessionResponse(Session session) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

        SessionResponse sessionResponse = new SessionResponse();
        sessionResponse.setAssembly(session.getAssembly());
        sessionResponse.setCityCouncil(session.getCityCouncil());
        sessionResponse.setSessionName(session.getSessionName());
        sessionResponse.setSessionDate(session.getSessionDate());
        setVotingNumbers(sessionResponse, session);
        return sessionResponse;
    }

    private void setVotingNumbers(SessionResponse sessionResponse, Session session) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

        sessionResponse.setVotingNumbers(new ArrayList<>());
        for (Session.VotingNumber votingNumber : session.getVotingNumbers()) {
            VotingNumber responseVotingNumber = new VotingNumber();
            responseVotingNumber.setVotingNumber(votingNumber.getVotingNumber());
            for (String votingId : votingNumber.getVotingIds()) {
                responseVotingNumber.add(RestUrl.buildGetVotingLink(votingId));
            }
            sessionResponse.getVotingNumbers().add(responseVotingNumber);
        }
    }

    Session getSessionByVotingId(String votingId) throws DelegateNofFoundException {

        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            for (Session.VotingNumber votingNumber : session.getVotingNumbers()) {
                if (votingNumber.getVotingIds().contains(votingId)) return session;
            }
        }
        throw new DelegateNofFoundException();
    }
}
