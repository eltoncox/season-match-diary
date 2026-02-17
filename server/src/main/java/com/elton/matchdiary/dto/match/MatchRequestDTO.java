package com.elton.matchdiary.dto.match;

import java.util.Date;

public record MatchRequestDTO(
        Date date,
        Integer scoreTeamOne,
        Integer scoreTeamTwo,
        Long teamOneId,
        Long teamTwoId,
        Long supportedTeamId
) {}
