package com.dev.challenge;

import com.dev.challenge.model.common.Error;
import com.dev.challenge.model.common.StatusResponse;
import com.dev.challenge.model.response.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatusResponseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String id = "not_exist_id";

    @Test
    public void testGetSessionDoesntExist() {
        MessageResponse<SessionResponse> response = restTemplate.getForObject("/rest/sessions/" + id, MessageResponse.class);
        Assert.assertEquals(response.getStatus().getStatusCode(), StatusResponse.ERROR.getValue());
        Assert.assertEquals(response.getStatus().getErrorCode(), Error.SESSION_NOT_FOUND.getCode());
        Assert.assertEquals(response.getStatus().getErrorMessage(), Error.SESSION_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void testGetVotingDoesntExist() {
        MessageResponse<SessionResponse> response = restTemplate.getForObject("/rest/voting/" + id, MessageResponse.class);
        Assert.assertEquals(response.getStatus().getStatusCode(), StatusResponse.ERROR.getValue());
        Assert.assertEquals(response.getStatus().getErrorCode(), Error.VOTING_NOT_FOUND.getCode());
        Assert.assertEquals(response.getStatus().getErrorMessage(), Error.VOTING_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void testGetDelegateDoesntExist() {
        MessageResponse<SessionResponse> response = restTemplate.getForObject("/rest/delegates/" + id, MessageResponse.class);
        Assert.assertEquals(response.getStatus().getStatusCode(), StatusResponse.ERROR.getValue());
        Assert.assertEquals(response.getStatus().getErrorCode(), Error.DELEGATE_NOT_FOUND.getCode());
        Assert.assertEquals(response.getStatus().getErrorMessage(), Error.DELEGATE_NOT_FOUND.getErrorMessage());
    }

}
