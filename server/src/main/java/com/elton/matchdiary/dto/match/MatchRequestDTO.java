package com.elton.matchdiary.dto.match;

import java.time.LocalDate;
import java.util.Date;

public record MatchRequestDTO(
        LocalDate date,
        Integer scoreTeamOne,
        Integer scoreTeamTwo,
        Long teamOneId,
        Long teamTwoId,
        Long supportedTeamId
) {}
