package com.dev.challenge.model.builder;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.common.LinkBuilder;
import com.dev.challenge.model.entity.Delegate;
import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.entity.Voting;
import com.dev.challenge.model.response.VotingResponse;
import com.dev.challenge.service.DelegateService;
import com.dev.challenge.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by zinoviyzubko on 07.05.17.
 */
@Component
public class VotingResponseBuilder {

    @Autowired private LinkBuilder linkBuilder;
    @Autowired private SessionService sessionService;
    @Autowired private DelegateService delegateService;

    public VotingResponse build(Voting voting) throws DelegateNofFoundException, SessionNotFoundsException, VotingNotFoundException {

        VotingResponse votingResponse = new VotingResponse();
        votingResponse.setVotingGoal(voting.getGoal());
        votingResponse.setResult(voting.getResult().getValue());
        setSessionUrl(votingResponse, voting.getId());
        setVotes(votingResponse, voting);
        return votingResponse;
    }

    private void setSessionUrl(VotingResponse response, String votingId) throws DelegateNofFoundException, SessionNotFoundsException, VotingNotFoundException {
        Session session = sessionService.getSessionByVotingId(votingId);
        response.add(linkBuilder.buildGetSessionLink(votingId, session.getSessionName()));
    }

    private void setVotes(VotingResponse response, Voting voting) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

        response.setVotes(new ArrayList<>());
        for(Voting.Vote vote : voting.getVotes()) {
            Delegate delegate = delegateService.getDelegateById(vote.getDelegateId());
            VotingResponse.Vote voteResponse = new VotingResponse.Vote();
            voteResponse.add(linkBuilder.buildGetDelegateLink(delegate.getId(), delegate.getName().replaceAll("_", " ")));
            voteResponse.setVote(vote.getVoteValue().getValue());
            response.getVotes().add(voteResponse);
        }
    }
}
