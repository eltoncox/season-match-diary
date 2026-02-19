package com.elton.matchdiary.dto.summary;

import com.elton.matchdiary.dto.team.TeamResponseDTO;

public record SummaryResponseDTO(
        long totalMatches,
        long victories,
        int winRatePercent,
        TeamResponseDTO mostSupportedTeam,
        long daysWithoutMatch
) {}
