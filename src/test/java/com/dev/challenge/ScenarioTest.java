package com.dev.challenge;

import com.dev.challenge.model.common.StatusResponse;
import com.dev.challenge.model.response.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ScenarioTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void fullScenario() throws IOException {

        MessageResponse<SessionsResponse> sessionsResponse = restTemplate.getForObject("/rest/sessions", MessageResponse.class);
        Assert.assertEquals(sessionsResponse.getStatus().getStatusCode(), StatusResponse.SUCCESS.getValue());
        Assert.assertNotNull(sessionsResponse.getData());
        String sessionUrl = sessionsResponse.getData().getLinks().get(0).getHref();

        MessageResponse<SessionResponse> sessionResponse = restTemplate.getForObject(sessionUrl, MessageResponse.class);
        Assert.assertEquals(sessionResponse.getStatus().getStatusCode(), StatusResponse.SUCCESS.getValue());
        Assert.assertNotNull(sessionResponse.getData());
        String votingUrl = sessionResponse.getData().getVotingNumbers().get(0).getLinks().get(0).getHref();

        MessageResponse<VotingResponse> votingResponse = restTemplate.getForObject(votingUrl, MessageResponse.class);
        Assert.assertEquals(votingResponse.getStatus().getStatusCode(), StatusResponse.SUCCESS.getValue());
        Assert.assertNotNull(votingResponse.getData());
        String delegateUrl = votingResponse.getData().getVotes().get(0).getLinks().get(0).getHref();

        MessageResponse<DelegateResponse> delegateResponse = restTemplate.getForObject(delegateUrl, MessageResponse.class);
        Assert.assertEquals(delegateResponse.getStatus().getStatusCode(), StatusResponse.SUCCESS.getValue());
        Assert.assertNotNull(delegateResponse.getData());
    }
}
