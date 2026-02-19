package com.elton.matchdiary.service;

import com.elton.matchdiary.dto.summary.SummaryResponseDTO;
import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.model.Team;
import com.elton.matchdiary.repository.MatchRepository;
import com.elton.matchdiary.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SummaryService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public SummaryService(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    public SummaryResponseDTO getSummary() {

        long totalMatches = matchRepository.countMatches();

        if (totalMatches == 0) {
            return new SummaryResponseDTO(0, 0, 0, null, 0);
        }

        long victories = matchRepository.countVictories();

        int winRatePercent = (int) Math.round((victories * 100.0) / totalMatches);

        TeamResponseDTO mostSupportedTeam = matchRepository.findMostSupportedTeamId()
                .flatMap(teamRepository::findById)
                .map(this::toTeamResponseDTO)
                .orElse(null);

        long daysWithoutMatch = matchRepository.findLastMatchDate()
                .map(lastDate -> ChronoUnit.DAYS.between(lastDate, LocalDate.now()))
                .orElse(0L);

        return new SummaryResponseDTO(
                totalMatches,
                victories,
                winRatePercent,
                mostSupportedTeam,
                daysWithoutMatch
        );
    }

    private TeamResponseDTO toTeamResponseDTO(Team team) {
        return new TeamResponseDTO(
                team.getId(),
                team.getName(),
                team.getState(),
                team.getPhotoUrl()
        );
    }
}
