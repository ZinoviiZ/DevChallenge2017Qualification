package com.dev.challenge.rest;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.service.VotingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
@RequestMapping(value = "/rest")
@Api(value = "API for working with voting.",
        description = "This API provides the capability to search delegates", produces = "application/json")
public class VotingController {

    @Autowired private VotingService votingService;

    @ApiOperation(value = "getting references on all voting", produces = "application/json")
    @RequestMapping(value = "/voting", method = GET)
    public MessageResponse getVoting() throws VotingNotFoundException, DelegateNofFoundException, SessionNotFoundsException {
        return votingService.getAllVoting();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Voting's id",
                    dataType = "String", paramType = "path")})
    @RequestMapping(value = "/voting/{id}", method = GET)
    public MessageResponse getVoting(@PathVariable String id) throws VotingNotFoundException, DelegateNofFoundException, SessionNotFoundsException {
        return votingService.getVotingById(id);
    }
}
