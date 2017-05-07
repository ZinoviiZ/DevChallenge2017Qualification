package com.dev.challenge.service;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.builder.LinkBuilder;
import com.dev.challenge.model.entity.Session;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.model.response.SessionResponse;
import com.dev.challenge.model.response.SessionsResponse;
import com.dev.challenge.repository.SessionRepository;
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
public class SessionServiceTest {

    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionService sessionService;
    @MockBean private LinkBuilder linkBuilder;

    private final String sessionLinkTest = "http://test.session.link.com";
    private final String sessionId = "07ca43a9";
    private final String sessionVotingId = "4d6dfbf7";
    private final String sessionCityCouncil = "Броварська міська рада";
    private final String sessionAssembly = "Броварської міської ради VІІ скликання";
    private final String sessionName = "16-а позачергова сесія";
    private final String sessionDate = "04.08.16";

    @Before
    public void setup() throws DelegateNofFoundException, SessionNotFoundsException, VotingNotFoundException {

        given(linkBuilder.
                buildGetSessionLink(any(String.class), any(String.class))
            ).willReturn(
                new Link(sessionLinkTest));
        given(linkBuilder.
                buildGetVotingLink(any(String.class))
        ).willReturn(
                new Link(sessionLinkTest));
        given(linkBuilder.
                buildGetDelegateLink(any(String.class), any(String.class))
        ).willReturn(
                new Link(sessionLinkTest));
    }

    @Test
    public void testGetAllSessions() {

        try {
            MessageResponse<SessionsResponse> response = sessionService.getAllSessions();
            Assert.assertNotNull(response.getData());
            Assert.assertNotEquals(response.getData().getLinks().size(), 0);
            Assert.assertEquals(response.getData().getLinks().get(0).getHref(), sessionLinkTest);
        } catch (SessionNotFoundsException e) {
        } catch (DelegateNofFoundException e) {
        } catch (VotingNotFoundException e) {
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void testGetSessionById() {

        try{
            MessageResponse<SessionResponse> response = sessionService.getSessionById(sessionId);
            Assert.assertEquals(response.getData().getCityCouncil(), sessionCityCouncil);
            Assert.assertEquals(response.getData().getAssembly(), sessionAssembly);
            Assert.assertEquals(response.getData().getSessionName(), sessionName);
            Assert.assertEquals(response.getData().getSessionDate(), sessionDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert false;
        }
        try {
            sessionService.getSessionById("not_exist_id");
            assert false;
        } catch (SessionNotFoundsException ex) {
        } catch (Exception ex) {
            assert false;
        }
    }

    @Test
    public void testGetSession() {

        Session session = sessionService.getSession(sessionCityCouncil, sessionAssembly, sessionName, sessionDate);
        Assert.assertEquals(sessionId, session.getId());
    }

    @Test
    public void testSaveSession() {

        String testCityCouncil = "Test_CityCouncil";
        String testAssembly = "Test_Assembly";
        String testSessionName = "Test_Session_Name";
        String testSessionDate = "Test_Session_Date";
        Session session = sessionService.saveSession(testCityCouncil, testAssembly, testSessionName, testSessionDate);
        session = sessionRepository.findOne(session.getId());
        Assert.assertEquals(session.getCityCouncil(), testCityCouncil);
        Assert.assertEquals(session.getAssembly(), testAssembly);
        Assert.assertEquals(session.getSessionName(), testSessionName);
        Assert.assertEquals(session.getSessionDate(), testSessionDate);
        sessionRepository.delete(session.getId());
    }

    @Test
    public void testGetSessionByVotingId() throws DelegateNofFoundException {

        Session session1 = sessionRepository.findOne(sessionId);
        Session session2 = sessionService.getSessionByVotingId(sessionVotingId);
        Assert.assertEquals(session1, session2);
    }
}
