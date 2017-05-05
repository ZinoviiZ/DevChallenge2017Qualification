package com.dev.challenge.model.common;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.rest.DelegateController;
import com.dev.challenge.rest.SessionController;
import com.dev.challenge.rest.VotingController;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class RestUrl {

    public static Link buildGetDelegateLink(String id, String delegateName) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {
        return linkTo(methodOn(DelegateController.class).getDelegateById(id)).withRel(delegateName);
    }

    public static Link buildGetVotingLink(String id) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {
        return linkTo(methodOn(VotingController.class).getVoting(id)).withSelfRel();
    }

    public static Link buildGetSessionLink(String id, String sessionName) throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {
        return linkTo(methodOn(SessionController.class).getSessionById(id)).withRel(sessionName);
    }

}
