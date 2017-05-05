package com.dev.challenge.rest;

import com.dev.challenge.exception.DelegateNofFoundException;
import com.dev.challenge.exception.SessionNotFoundsException;
import com.dev.challenge.exception.VotingNotFoundException;
import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.service.DelegateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
@RequestMapping("/rest")
@Api(value = "API for working with delegates.",
        description = "This API provides the capability to search delegates", produces = "application/json")
public class DelegateController {

    @Autowired private DelegateService delegateService;

    @ApiOperation(value = "getting full information about delegate by id", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Delegate's id",
                    dataType = "String", paramType = "path")})
    @RequestMapping(value = "/delegates/{id}", method = GET)
    public MessageResponse getDelegateById(@PathVariable String id) throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {
        return delegateService.getDelegateResponseById(id);
    }

    @ApiOperation(value = "getting references on delegates", produces = "application/json")
    @RequestMapping(value = "/delegates", method = GET)
    public MessageResponse getDelegates() throws DelegateNofFoundException, VotingNotFoundException, SessionNotFoundsException {
        return delegateService.getAllDelegates();
    }
}
