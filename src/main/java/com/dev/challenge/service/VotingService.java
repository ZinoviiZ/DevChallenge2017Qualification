package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.entity.Delegate;
import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.entity.Voting;
import com.dev.challenge.model.response.AllVotingResponse;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.model.response.VotingResponse;
import com.dev.challenge.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dev.challenge.model.common.RestUrl.*;
import static com.dev.challenge.model.response.VotingResponse.Vote;

@Service
public class VotingService {

    @Autowired private VotingRepository votingRepository;
    @Autowired private SessionService sessionService;
    @Autowired private DelegateService delegateService;

    public MessageResponse getAllVoting() throws VotingNotFoundException, DelegateNofFoundException, SessionNotFoundsException {

        List<Voting> votings = votingRepository.findAll();
        if (votings == null || votings.isEmpty()) throw new VotingNotFoundException();
        AllVotingResponse response = new AllVotingResponse();
        for (Voting voting : votings) {
            response.add(buildGetVotingLink(voting.getId()));
        }
        return new MessageResponse(response);
    }

    public MessageResponse getVotingById(String id) throws VotingNotFoundException, DelegateNofFoundException, SessionNotFoundsException {

        Voting voting = votingRepository.findOne(id);
        if (voting == null) throw new VotingNotFoundException();
        VotingResponse response = buildVotingResponse(voting);
        return new MessageResponse(response);
    }

    private VotingResponse buildVotingResponse(Voting voting) throws DelegateNofFoundException, SessionNotFoundsException, VotingNotFoundException {

        VotingResponse votingResponse = new VotingResponse();
        votingResponse.setResult(voting.getResult().getValue());
        setSessionUrl(votingResponse, voting.getId());
        setVotes(votingResponse, voting);
        return votingResponse;
    }

    private void setSessionUrl(VotingResponse response, String votingId) throws DelegateNofFoundException, SessionNotFoundsException, VotingNotFoundException {
        Session session = sessionService.getSessionByVotingId(votingId);
        response.add(buildGetSessionLink(votingId, session.getSessionName()));
    }

    private void setVotes(VotingResponse response, Voting voting) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

        response.setVotes(new ArrayList<>());
        for(Voting.Vote vote : voting.getVotes()) {
            Delegate delegate = delegateService.getDelegateById(vote.getDelegateId());
            Vote voteResponse = new Vote();
            voteResponse.add(buildGetDelegateLink(delegate.getId(), delegate.getName().replaceAll("_", " ")));
            voteResponse.setVote(vote.getVoteValue().getValue());
            response.getVotes().add(voteResponse);
        }
    }
}
