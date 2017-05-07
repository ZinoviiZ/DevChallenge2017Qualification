package com.dev.challenge.model.builder;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.entity.Delegate;
import com.dev.challenge.model.response.DelegateResponse;
import com.dev.challenge.service.DelegateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DelegateResponseBuilder {

    @Autowired private LinkBuilder linkBuilder;
    @Autowired private DelegateService delegateService;

    public DelegateResponse build(Delegate delegate) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {
        DelegateResponse delegateResponse = new DelegateResponse();
        delegateResponse.setName(delegate.getName().replaceAll("_", " "));
        setStatistic(delegateResponse, delegate);
        setInfluenceZone(delegateResponse, delegate);
        return delegateResponse;
    }

    private void setStatistic(DelegateResponse response, Delegate delegate) {

        int countFor = delegate.getForVotingIds().size();
        int countAbstain = delegate.getAbstainVotingIds().size();
        int countAgains = delegate.getAgainstVotingIds().size();
        int countPass = delegate.getPassVotingIds().size();
        int countAbsent = delegate.getAbsentVotingIds().size();
        int countAll = countFor + countAbsent + countAbstain + countAgains + countPass;
        response.setCountVotingParticipate(countAll);
        response.setInterestFor(Math.round(((double) countFor / countAll) * 100.0) / 100.0);
        response.setInterestAbsent(Math.round(((double) countAbsent / countAll) * 100.0) / 100.0);
        response.setInterestAgainst(Math.round(((double) countAgains / countAll) * 100.0) / 100.0);
        response.setInterestPass(Math.round(((double) countPass / countAll) * 100.0) / 100.0);
        double abstainValue = 1.0 - response.getInterestAbsent() - response.getInterestFor() - response.getInterestPass() - response.getInterestAgainst();
        response.setInterestAbstain(Math.round(abstainValue * 100.0) / 100.0);
    }

    private void setInfluenceZone(DelegateResponse response, Delegate delegate) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {

        double max = 0;
        response.setInfluenceZones(new ArrayList<>());
        List<Delegate> others = delegateService.findOthersDelegates(delegate.getName());
        for (Delegate other : others) {
            DelegateResponse.InfluenceZone zone = new DelegateResponse.InfluenceZone();
            int influenceZoneFor = getIntersectionCount(delegate.getForVotingIds(), other.getForVotingIds());
            int influenceZoneAgainst = getIntersectionCount(delegate.getAgainstVotingIds(), other.getAgainstVotingIds());
            int influenceZoneAbstain = getIntersectionCount(delegate.getAbstainVotingIds(), other.getAbstainVotingIds());
            double influenceZone = influenceZoneAbstain + influenceZoneFor + influenceZoneAgainst;
            zone.add(linkBuilder.buildGetDelegateLink(other.getId(), other.getName().replaceAll("_", " ")));
            zone.setInfluence(influenceZone);
            response.getInfluenceZones().add(zone);
            if (influenceZone > max) max = influenceZone;
        }
        for (DelegateResponse.InfluenceZone zone : response.getInfluenceZones()) {
            zone.setInfluence(Math.round(zone.getInfluence() / max * 100) / 100.0);
        }
    }

    private int getIntersectionCount(List<String> list1, List<String> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList())
                .size();
    }
}
