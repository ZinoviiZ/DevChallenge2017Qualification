package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.builder.VotingResponseBuilder;
import com.dev.challenge.model.entity.Delegate;
import com.dev.challenge.model.entity.Voting;
import com.dev.challenge.model.enums.VoteValue;
import com.dev.challenge.model.enums.VotingResult;
import com.dev.challenge.model.response.AllVotingResponse;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.model.response.VotingResponse;
import com.dev.challenge.parser.PdfData;
import com.dev.challenge.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotingService extends ParentService {

    @Autowired private VotingRepository votingRepository;
    @Autowired private DelegateService delegateService;
    @Autowired private VotingResponseBuilder votingResponseBuilder;

    public MessageResponse<AllVotingResponse> getVotingPackage(int index, int packageSize) throws VotingNotFoundException, DelegateNofFoundException, SessionNotFoundsException {

        List<Voting> votings = findVotingPackage(index, packageSize);
        AllVotingResponse response = new AllVotingResponse();
        for (Voting voting : votings) {
            response.add(linkBuilder.buildGetVotingLink(voting.getId()));
        }
        return new MessageResponse(response);
    }

    private List<Voting> findVotingPackage(int index, int packageSize) throws VotingNotFoundException {
        if (index < 0 || packageSize <= 0) throw new VotingNotFoundException();
        double count = votingRepository.count();
        int packages = (int) Math.ceil(count / packageSize);
        Page<Voting> votings = votingRepository.findAll(new PageRequest(index, packageSize));
        if (votings == null || votings.getContent().isEmpty()) throw new VotingNotFoundException();
        return votings.getContent();
    }

    public MessageResponse<VotingResponse> getVotingById(String id) throws VotingNotFoundException, DelegateNofFoundException, SessionNotFoundsException {

        Voting voting = votingRepository.findOne(id);
        if (voting == null) throw new VotingNotFoundException();
        VotingResponse response = votingResponseBuilder.build(voting);
        return new MessageResponse(response);
    }

    public void saveVoting(Voting voting) {
        votingRepository.save(voting);
    }

    public Voting saveVoting(String votingGoal, List<PdfData.Vote> votes, VotingResult votingResult) {

        Voting voting = new Voting();
        voting.setId(generator.generateId());
        voting.setGoal(votingGoal);
        List<Delegate> delegates = new ArrayList<>();
        for (PdfData.Vote dataVote : votes) {
            Delegate delegate = delegateService.getDelegateByName(dataVote.getDelegateName());
            VoteValue voteValue = dataVote.getVoteValue();
            Voting.Vote vote = new Voting.Vote(delegate.getId(), voteValue);
            delegateService.vote(voting.getId(), delegate, voteValue);
            voting.getVotes().add(vote);
            delegates.add(delegate);
        }
        voting.setResult(votingResult);
        delegateService.saveDelegates(delegates);
        votingRepository.save(voting);
        return voting;
    }
}
