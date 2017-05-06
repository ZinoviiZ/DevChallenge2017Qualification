package com.dev.challenge.parser;

import com.dev.challenge.exception.PdfParseException;
import com.dev.challenge.model.enums.VoteValue;
import com.dev.challenge.model.enums.VotingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Parser {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    PdfData parseContent(String content) throws PdfParseException {
        try {
            content = content.replace(String.valueOf((char) 160), String.valueOf((char) 32));
            String[] contentMas = content.split("\n");
            String cityCouncil = contentMas[1];
            String sessionName = getSessionName(contentMas[2]);
            String assembly = getAssembly(contentMas[2]);
            String sessionDate = getSessionDate(contentMas[2]).trim();
            String agenda = getAgenda(content).replaceAll("\n", "");
            String votingNumber = getVotingNumber(content);
            String votingGoal = getVotingGoal(votingNumber, content).trim();
            List<PdfData.Vote> votes = getVotes(content);
            Integer countFor = countDelegateFor(votes);
            Integer countAgainst = countDelegateAgainst(votes);
            Integer countAbstain = countDelegateAbstain(votes);
            Integer countPass = countDelegatePass(votes);
            Integer countAbsent = countDelegateAbsent(votes);
            VotingResult votingResult = getVotingResult(content);
            return new PdfData(cityCouncil, sessionName, assembly, sessionDate, agenda, votingNumber, votingGoal, votes,
                    countFor, countAgainst, countAbstain, countPass, countAbsent, votingResult);
        } catch (RuntimeException ex) {
            logger.error("error: ", ex);
            throw new PdfParseException();
        }
    }

    private String getSessionName(String line) {
        int startIndex = line.indexOf("сесія");
        if (startIndex < 0) {
            startIndex = line.indexOf("сесії");
        }
        assert startIndex >= 0;
        return line.substring(0, startIndex + 5).trim();
    }

    private String getAssembly(String line) {
        String stringFrom = "сесія";
        int startIndex = line.indexOf("сесія");
        if (startIndex < 0) {
            startIndex = line.indexOf("сесії");
        }
        startIndex += 5;
        return line.substring(startIndex, line.indexOf("від") - 1).trim();
    }

    private String getSessionDate(String line) {
        String stringFrom = "від";
        int indexFrom = line.indexOf(stringFrom) + stringFrom.length();
        return line.substring(indexFrom, line.length()).trim();
    }

    private String getAgenda(String content) {
        String stringFrom = "Результат поіменного голосування:\n";
        int indexFrom = content.indexOf(stringFrom) + stringFrom.length();
        int indexTo = content.indexOf("\n №:");
        return content.substring(indexFrom, indexTo).trim();
    }

    private String getVotingNumber(String content) {
        int index = content.indexOf("\n №:") + 5;
        return content.substring(index, content.indexOf(" ", index)).trim();
    }

    private String getVotingGoal(String votingNumber, String content) {
        String stringFrom = "\n №: " + votingNumber;
        int index = content.indexOf(stringFrom) + stringFrom.length() + 1;
        return content.substring(index, content.indexOf("\n", index)).trim();
    }

    private List<PdfData.Vote> getVotes(String content) {

        List<PdfData.Vote> votes = new ArrayList<>();
        String stringFrom = "Результат\nголосування";
        int indexFrom = content.lastIndexOf(stringFrom) + stringFrom.length();
        String delegatesText = content.substring(indexFrom, content.indexOf("ПІДСУМКИ ГОЛОСУВАННЯ"));
        delegatesText = delegatesText.replaceAll("\n", "");
        String[] delegates = delegatesText.split("[0-9]+");
        for (String delegate : delegates) {
            delegate = delegate.trim();
            if (delegate.isEmpty()) continue;
            String[] delegatePart = delegate.trim().split(" ");
            String delegateName = delegatePart[0] + " " + delegatePart[1] + " " + delegatePart[2];
            VoteValue voteValue = VoteValue.getVoteValue(delegate.substring(delegate.indexOf(delegateName) + delegateName.length() + 1, delegate.length()));
            votes.add(new PdfData.Vote(delegateName, voteValue));
        }
        return votes;
    }

    private Integer countDelegateFor(List<PdfData.Vote> votes) {
        int count = 0;
        for (PdfData.Vote vote : votes) {
            if (vote.getVoteValue() == VoteValue.For) count++;
        }
        return count;
    }

    private Integer countDelegateAgainst(List<PdfData.Vote> votes) {
        int count = 0;
        for (PdfData.Vote vote : votes) {
            if (vote.getVoteValue() == VoteValue.Against) count++;
        }
        return count;
    }

    private Integer countDelegateAbstain(List<PdfData.Vote> votes) {
        int count = 0;
        for (PdfData.Vote vote : votes) {
            if (vote.getVoteValue() == VoteValue.Abstain) count++;
        }
        return count;
    }

    private Integer countDelegatePass(List<PdfData.Vote> votes) {
        int count = 0;
        for (PdfData.Vote vote : votes) {
            if (vote.getVoteValue() == VoteValue.Pass) count++;
        }
        return count;
    }

    private Integer countDelegateAbsent(List<PdfData.Vote> votes) {
        int count = 0;
        for (PdfData.Vote vote : votes) {
            if (vote.getVoteValue() == VoteValue.Absent) count++;
        }
        return count;
    }

    private VotingResult getVotingResult(String content) {
        String stringFrom = "Рішення: ";
        int indexFrom = content.indexOf(stringFrom) + stringFrom.length();
        int indexTo = content.indexOf("\n(прийнято / не прийнято)");
        String stringResult = content.substring(indexFrom, indexTo);
        return VotingResult.getVoteResult(stringResult);
    }
}
