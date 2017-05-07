package com.dev.challenge.parser;

import com.dev.challenge.exception.PdfParseException;
import com.dev.challenge.model.enums.VotingResult;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParserTest {

    private final String testPdfFilePath = "/pdf/test/test.pdf";
    @Autowired
    private Parser parser;

    @Test
    public void testParseContent() throws IOException, PdfParseException {

        File file = new ClassPathResource(testPdfFilePath).getFile();
        PDDocument pdDocument = PDDocument.load(file);
        PDFTextStripper textStripper = new PDFTextStripper();
        String content = textStripper.getText(pdDocument);
        PdfData data = parser.parseContent(content);
        Assert.assertEquals(data.getCityCouncil(), "Броварська міська рада");
        Assert.assertEquals(data.getSessionName(), "21 позачергова сесія");
        Assert.assertEquals(data.getAssembly(), "VІІ скликання");
        Assert.assertEquals(data.getSessionDate(), "11.11.16");
        Assert.assertEquals(data.getAgenda(), "Про затвердження порядку денного");
        Assert.assertEquals(data.getVotingNumber(), "бн");
        Assert.assertEquals(data.getVotes().size(), 37);
        Assert.assertEquals(data.getResult(), VotingResult.Approve);
        pdDocument.close();
    }
}
