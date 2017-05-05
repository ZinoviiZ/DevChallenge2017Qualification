package com.dev.challenge.rest;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/rest")
@Api(value = "API for working with sessions.",
        description = "This API provides the capability to search sessions", produces = "application/json")
public class SessionController {

    @Autowired private SessionService sessionService;

    @ApiOperation(value = "getting references on sessions", produces = "application/json")
    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public MessageResponse getSessions() throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {
        return sessionService.getAllSessions();
    }

    @ApiOperation(value = "getting full information about session by id", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Session's id",
                    dataType = "String", paramType = "path")})
    @RequestMapping(value = "/sessions/{id}", method = RequestMethod.GET)
    public MessageResponse getSessionById(@PathVariable String id) throws SessionNotFoundsException, DelegateNofFoundException, VotingNotFoundException {
        return sessionService.getSessionById(id);
    }
}
