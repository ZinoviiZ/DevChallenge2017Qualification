package com.dev.challenge.model.builder;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.response.SessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SessionResponseBuilder {

    @Autowired private LinkBuilder linkBuilder;
    public SessionResponse build(Session session) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

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
            SessionResponse.VotingNumber responseVotingNumber = new SessionResponse.VotingNumber();
            responseVotingNumber.setVotingNumber(votingNumber.getVotingNumber());
            for (String votingId : votingNumber.getVotingIds()) {
                responseVotingNumber.add(linkBuilder.buildGetVotingLink(votingId));
            }
            sessionResponse.getVotingNumbers().add(responseVotingNumber);
        }
    }
}
