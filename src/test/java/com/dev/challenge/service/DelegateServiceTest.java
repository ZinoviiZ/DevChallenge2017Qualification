package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.builder.LinkBuilder;
import com.dev.challenge.model.entity.Delegate;
import com.dev.challenge.model.response.DelegateResponse;
import com.dev.challenge.model.response.DelegatesResponse;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.repository.DelegateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DelegateServiceTest {

    @Autowired private DelegateRepository delegateRepository;
    @Autowired private DelegateService delegateService;
    @MockBean private LinkBuilder linkBuilder;

    private final String delegateLinkTest = "http://test.delegate.link.com";
    private final String delegateId = "bb4f62fb";
    private final String delegateName = "МЕЛЬНИК ОКСАНА МИКОЛАЇВНА";

    @Before
    public void setup() throws DelegateNofFoundException, SessionNotFoundsException, VotingNotFoundException {

        given(linkBuilder.
                buildGetSessionLink(any(String.class), any(String.class))
        ).willReturn(
                new Link(delegateLinkTest));
        given(linkBuilder.
                buildGetVotingLink(any(String.class))
        ).willReturn(
                new Link(delegateLinkTest));
        given(linkBuilder.
                buildGetDelegateLink(any(String.class), any(String.class))
        ).willReturn(
                new Link(delegateLinkTest));
    }

    @Test
    public void testGetDelegateResponseById() throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {

        Delegate delegate = delegateRepository.findOne(delegateId);
        MessageResponse<DelegateResponse> response = delegateService.getDelegateResponseById(delegateId);
        DelegateResponse dResponse = response.getData();
        Double interest = dResponse.getInterestFor() + dResponse.getInterestAbstain() + dResponse.getInterestAbsent() + dResponse.getInterestAgainst() + dResponse.getInterestPass();
        Assert.assertEquals(interest, new Double(1.0));
        Assert.assertEquals(dResponse.getName(), delegate.getName().replace("_", " "));
    }

    @Test
    public void testGetAllDelegates() throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {

        long delegateCount = delegateRepository.count();
        MessageResponse<DelegatesResponse> response = delegateService.getAllDelegates();
        Assert.assertEquals(delegateCount, response.getData().getLinks().size());
    }

    @Test
    public void testGetDelegateByName() {

        Delegate delegate = delegateService.getDelegateByName(delegateName);
        Assert.assertNotNull(delegate);
        Assert.assertEquals(delegate.getId(), delegateId);
    }

}
