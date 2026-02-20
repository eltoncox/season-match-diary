package com.elton.matchdiary.service;

import com.elton.matchdiary.dto.summary.SummaryResponseDTO;
import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.model.Match;
import com.elton.matchdiary.model.Team;
import com.elton.matchdiary.repository.MatchRepository;
import com.elton.matchdiary.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SummaryService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public SummaryService(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    public SummaryResponseDTO getSummary() {

        long matchesQuantity = matchRepository.countMatches();

        if (matchesQuantity == 0) {
            return new SummaryResponseDTO(0, 0, 0, null, 0);
        }

        long winsQuantity = matchRepository.countVictories();

        int winRatePercent = calculatePointsWinRatePercent();

        TeamResponseDTO mostSupportedTeam = matchRepository.findMostSupportedTeamId()
                .flatMap(teamRepository::findById)
                .map(this::toTeamResponseDTO)
                .orElse(null);

        long daysWithoutMatch = matchRepository.findLastMatchDate()
                .map(lastDate -> ChronoUnit.DAYS.between(lastDate, LocalDate.now()))
                .orElse(0L);

        return new SummaryResponseDTO(
                matchesQuantity,
                winsQuantity,
                winRatePercent,
                mostSupportedTeam,
                daysWithoutMatch
        );
    }

    /**
     * Regra igual ao professor:
     * - vitória do time apoiado: +3
     * - empate: +1
     * - total possível por partida: 3
     * Percentual = (pontos conquistados / pontos possíveis) * 100
     */
    private int calculatePointsWinRatePercent() {

        List<Match> matches = matchRepository.findAll();

        long wonPoints = 0;
        long totalPoints = 0;

        for (Match match : matches) {
            totalPoints += 3;

            Integer scoreTeamOne = match.getScoreTeamOne();
            Integer scoreTeamTwo = match.getScoreTeamTWO();

            // empate
            if (scoreTeamOne != null && scoreTeamTwo != null && scoreTeamOne.equals(scoreTeamTwo)) {
                wonPoints += 1;
                continue;
            }

            // determina vencedor
            Long winnerTeamId = null;
            if (scoreTeamOne != null && scoreTeamTwo != null) {
                if (scoreTeamOne > scoreTeamTwo) {
                    winnerTeamId = match.getTeamOne() != null ? match.getTeamOne().getId() : null;
                } else if (scoreTeamTwo > scoreTeamOne) {
                    winnerTeamId = match.getTeamTWO() != null ? match.getTeamTWO().getId() : null;
                }
            }

            // se o time apoiado venceu, +3
            if (winnerTeamId != null
                    && match.getSupportedTeam() != null
                    && match.getSupportedTeam().getId().equals(winnerTeamId)) {
                wonPoints += 3;
            }
        }

        if (totalPoints == 0) return 0;

        return (int) Math.round((wonPoints * 100.0) / totalPoints);
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