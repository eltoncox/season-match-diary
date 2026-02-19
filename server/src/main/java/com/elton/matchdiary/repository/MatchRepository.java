package com.elton.matchdiary.repository;

import com.elton.matchdiary.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // total de partidas
    @Query("select count(m) from Match m")
    long countMatches();

    // vitórias (considerando vitória quando o time apoiado venceu)
    @Query("""
        select count(m)
        from Match m
        where m.supportedTeam is not null
          and (
            (m.supportedTeam = m.teamOne and m.scoreTeamOne > m.scoreTeamTWO) or
            (m.supportedTeam = m.teamTWO and m.scoreTeamTWO > m.scoreTeamOne)
          )
    """)
    long countVictories();

    // time mais apoiado (retorna 0 ou 1 resultado)
    @Query(value = """
        SELECT team_supported_id
        FROM matches
        WHERE team_supported_id IS NOT NULL
        GROUP BY team_supported_id
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """, nativeQuery = true)
    Optional<Long> findMostSupportedTeamId();

    // última data registrada (LocalDate)
    @Query("select max(m.date) from Match m")
    Optional<LocalDate> findLastMatchDate();
}
