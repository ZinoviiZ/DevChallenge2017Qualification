package com.dev.challenge.rest;

import com.dev.challenge.model.response.MessageResponse;
import com.dev.challenge.service.PdfService;
import com.sun.jersey.multipart.FormDataParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Controller
@Api(value = "API for working with pdf files.",
        description = "This API provides the capability to upload pdf files.", produces = "application/json" , consumes = "multipart/form-data")
public class FileController {

    @Autowired private PdfService pdfService;

    @ApiOperation(value = "Load pdf files for parsing and saving in DB.")
    @RequestMapping(value = "/rest/pdf", method = RequestMethod.POST, produces = "application/json" , consumes = "multipart/form-data")
    public MessageResponse parsePdf(@FormDataParam(value = "Pdf file") MultipartFile file) throws InterruptedException, ExecutionException, IOException {
        return pdfService.parseAndSavePdf(Arrays.asList(file));
    }
}
