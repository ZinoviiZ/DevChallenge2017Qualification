package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.builder.LinkBuilder;
import com.dev.challenge.model.entity.Voting;
import com.dev.challenge.model.enums.VotingResult;
import com.dev.challenge.model.response.AllVotingResponse;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.model.response.VotingResponse;
import com.dev.challenge.parser.PdfData;
import com.dev.challenge.repository.VotingRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VotingServiceTest {

    @Autowired private VotingRepository votingRepository;
    @Autowired private VotingService votingService;

    @MockBean
    private LinkBuilder linkBuilder;

    private final String votingLinkTest = "http://test.voting.link.com";
    private final String votingId = "b08f6f44";

    @Before
    public void setup() throws DelegateNofFoundException, SessionNotFoundsException, VotingNotFoundException {

        given(linkBuilder.
                buildGetSessionLink(any(String.class), any(String.class))
        ).willReturn(
                new Link(votingLinkTest));
        given(linkBuilder.
                buildGetVotingLink(any(String.class))
        ).willReturn(
                new Link(votingLinkTest));
        given(linkBuilder.
                buildGetDelegateLink(any(String.class), any(String.class))
        ).willReturn(
                new Link(votingLinkTest));
    }

    @Test
    public void testGetVotingPackage() throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {
        MessageResponse<AllVotingResponse> response;
        testVotingNotFoundException(-1, 1);
        testVotingNotFoundException(0, -1);
        testVotingNotFoundException(1, 0);
        testVotingNotFoundException(1, -1);
        int index = 2;
        int packageSize = 3;
        List<Voting> votings = votingRepository.findAll();
        List<Voting> votings1 = new ArrayList<>();
        for (int i = index * packageSize; i < (index + 1) * packageSize; i++) {
            votings1.add(votings.get(i));
        }
        response = votingService.getVotingPackage(index, packageSize);
        Assert.assertEquals(response.getData().getLinks().size(), votings1.size());
    }

    private void testVotingNotFoundException(int index, int packageSize) {
        try {
            votingService.getVotingPackage(index, packageSize);
            assert false;
        } catch (VotingNotFoundException ex) {
        } catch (Exception ex) {
            assert false;
        }
    }

    @Test
    public void testGetVotingById() throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {

        Voting voting = votingRepository.findOne(votingId);
        MessageResponse<VotingResponse> response = votingService.getVotingById(votingId);
        Assert.assertEquals(response.getData().getResult(), voting.getResult().getValue());
        Assert.assertEquals(response.getData().getVotingGoal(), voting.getGoal());
    }

    @Test
    public void testSaveVoting() {

        String votingGoal = "voting_test_goal";
        VotingResult result = VotingResult.Denied;
        Voting voting = votingService.saveVoting(votingGoal, new ArrayList<>(), result);
        Voting voting1 = votingRepository.findOne(voting.getId());
        Assert.assertEquals(voting.getId(), voting1.getId());
        Assert.assertEquals(voting.getGoal(), voting1.getGoal());
        Assert.assertEquals(voting.getResult(), voting1.getResult());
        votingRepository.delete(voting.getId());
    }

}
