package com.dev.challenge.aop;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.common.Error;
import com.dev.challenge.model.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = {"com.dev.challenge.rest"})
public class ExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public @ResponseBody MessageResponse handleException(HttpServletRequest req, Exception ex) {
        logger.error("error: ", ex);
        return new MessageResponse(Error.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(SessionNotFoundsException.class)
    public @ResponseBody MessageResponse handleSessionNotFoundException(HttpServletRequest req, SessionNotFoundsException ex) {
        logger.error("error: ", ex);
        return new MessageResponse(Error.SESSION_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(VotingNotFoundException.class)
    public @ResponseBody MessageResponse handleVotingNotFoundException(HttpServletRequest req, VotingNotFoundException ex) {
        logger.error("error: ", ex);
        return new MessageResponse(Error.VOTING_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(DelegateNofFoundException.class)
    public @ResponseBody MessageResponse handleDelegateNotFoundException(HttpServletRequest req, DelegateNofFoundException ex) {
        logger.error("error: ", ex);
        return new MessageResponse(Error.DELEGATE_NOT_FOUND);
    }
}
