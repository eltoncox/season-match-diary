package com.elton.matchdiary.dto.match;

import com.elton.matchdiary.dto.team.TeamResponseDTO;

import java.time.LocalDate;


public record MatchResponseDTO(
        Long id,
        LocalDate date,
        Integer scoreTeamOne,
        Integer scoreTeamTwo,
        TeamResponseDTO teamOne,
        TeamResponseDTO teamTwo,
        TeamResponseDTO supportedTeam
) {}
