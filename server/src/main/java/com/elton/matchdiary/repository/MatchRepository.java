package com.elton.matchdiary.repository;

import com.elton.matchdiary.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // ===============================
    // CONTAGENS
    // ===============================

    // total de partidas
    @Query("select count(m) from Match m")
    long countMatches();

    // vitórias (quando o time apoiado venceu)
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


    // ===============================
    // CONSULTAS ESPECIAIS
    // ===============================

    // time mais apoiado
    @Query(value = """
        SELECT team_supported_id
        FROM matches
        WHERE team_supported_id IS NOT NULL
        GROUP BY team_supported_id
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """, nativeQuery = true)
    Optional<Long> findMostSupportedTeamId();


    // última data registrada (usado no SummaryService)
    @Query("select max(m.date) from Match m")
    Optional<LocalDate> findLastMatchDate();


    // ===============================
    // NOVO A) Listar partidas mais recentes primeiro
    // ===============================

    // Spring Data gera automaticamente ORDER BY date DESC
    List<Match> findAllByOrderByDateDesc();


    // ===============================
    // NOVO B) Dias sem partida (cálculo direto no PostgreSQL)
    // ===============================

    @Query(value = """
        SELECT COALESCE(
            CAST(EXTRACT(EPOCH FROM AGE(CURRENT_DATE, MAX(date))) / 86400 AS BIGINT),
            0
        )
        FROM matches
    """, nativeQuery = true)
    long getDaysWithoutWatching();
}