package com.dev.challenge.parser;

import com.dev.challenge.exception.PdfParseException;
import com.dev.challenge.model.common.Error;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ParseWorker implements Callable<List<PdfData>> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MultipartFile pdfFile;

    public ParseWorker(MultipartFile file) throws IOException {
        this.pdfFile = file;
    }

    @Override
    public List<PdfData> call() {

        List<PdfData> dataList = new ArrayList<>();
        Splitter splitter = new Splitter();
        Parser parser = new Parser();
        try(PDDocument pdDocument = PDDocument.load(pdfFile.getInputStream())) {
            String content = new String();
            PDFTextStripper textStripper = new PDFTextStripper();
            for (PDDocument document : splitter.split(pdDocument)) {
                content = content + textStripper.getText(document);
                if (isOnePageContaineFullVoting(content))
                    continue;
                PdfData data = parser.parseContent(content);
                dataList.add(data);
                content = new String();
                document.close();
            }
        }
        catch (PdfParseException ex) {
            logger.error("errorCode - " + ex.getErrorCode() + ": " + ex.getErrorMessage(), ex);
        }
        catch (IOException ex) {
            logger.error("errorCode - " + Error.OPEN_PDF_ERROR.getCode() + ": " + Error.OPEN_PDF_ERROR.getErrorMessage() , ex);
        }
        return dataList;
    }

    private boolean isOnePageContaineFullVoting(String content) {
        if (content.contains("Рішення: ")) return true;
        return false;
    }
}
