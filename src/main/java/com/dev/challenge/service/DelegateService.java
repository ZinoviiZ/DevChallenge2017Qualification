package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.builder.DelegateResponseBuilder;
import com.dev.challenge.model.entity.Delegate;
import com.dev.challenge.model.enums.VoteValue;
import com.dev.challenge.model.response.DelegateResponse;
import com.dev.challenge.model.response.DelegatesResponse;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.repository.DelegateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DelegateService extends ParentService {

    @Autowired private DelegateRepository delegateRepository;
    @Autowired private DelegateResponseBuilder delegateResponseBuilder;

    public MessageResponse getDelegateResponseById(String id) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

        Delegate delegate = delegateRepository.findOne(id);
        if (delegate == null) throw new DelegateNofFoundException();
        DelegateResponse responseData = delegateResponseBuilder.build(delegate);
        return new MessageResponse(responseData);
    }

    public MessageResponse getAllDelegates() throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

        List<Delegate> delegates = delegateRepository.findAll();
        if (delegates == null || delegates.isEmpty()) throw new DelegateNofFoundException();
        DelegatesResponse responseData = new DelegatesResponse();
        for (Delegate delegate : delegates) {
            responseData.add(linkBuilder.buildGetDelegateLink(delegate.getId(), delegate.getName().replaceAll("_", " ")));
        }
        return new MessageResponse(responseData);
    }

    public Delegate getDelegateById(String id) throws DelegateNofFoundException {
        Delegate delegate = delegateRepository.findOne(id);
        if(delegate == null) throw new DelegateNofFoundException();
        return delegate;
    }

    public Delegate getDelegateByName(String name) {
        name = name.replaceAll(" ", "_").toUpperCase();
        Delegate delegate = delegateRepository.findByName(name);
        if (delegate == null) {
            delegate = saveDelegate(name);
        }
        return delegate;
    }

    public List<Delegate> findOthersDelegates(String name) {

        return delegateRepository.findByNameNot(name);
    }

    public Delegate saveDelegate(String name) {

        Delegate delegate = new Delegate(generator.generateId(), name);
        delegateRepository.save(delegate);
        return delegate;
    }

    public void saveDelegates(List<Delegate> delegates) {
        delegateRepository.save(delegates);
    }

    public void vote(String votingId, Delegate delegate, VoteValue voteValue) {

        switch (voteValue) {
            case For:
                delegate.getForVotingIds().add(votingId);
                break;
            case Absent:
                delegate.getAbsentVotingIds().add(votingId);
                break;
            case Abstain:
                delegate.getAbstainVotingIds().add(votingId);
                break;
            case Against:
                delegate.getAgainstVotingIds().add(votingId);
                break;
            case Pass:
                delegate.getPassVotingIds().add(votingId);
                break;
        }
    }
}