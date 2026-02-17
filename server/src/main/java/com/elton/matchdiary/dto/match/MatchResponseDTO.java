package com.elton.matchdiary.dto.match;

import com.elton.matchdiary.dto.team.TeamResponseDTO;

import java.util.Date;

public record MatchResponseDTO(
        Long id,
        Date date,
        Integer scoreTeamOne,
        Integer scoreTeamTwo,
        TeamResponseDTO teamOne,
        TeamResponseDTO teamTwo,
        TeamResponseDTO supportedTeam
) {}
