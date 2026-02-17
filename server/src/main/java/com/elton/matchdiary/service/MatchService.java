package com.elton.matchdiary.service;

import com.elton.matchdiary.dto.match.MatchRequestDTO;
import com.elton.matchdiary.dto.match.MatchResponseDTO;
import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.model.Match;
import com.elton.matchdiary.model.Team;
import com.elton.matchdiary.repository.MatchRepository;
import com.elton.matchdiary.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    // GET ALL (retorna objeto completo)
    public List<MatchResponseDTO> getAllMatches() {
        return matchRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // GET BY ID (retorna objeto completo)
    public Optional<MatchResponseDTO> getMatchById(Long id) {
        return matchRepository.findById(id).map(this::toResponseDTO);
    }

    // CREATE (recebe IDs, retorna objeto completo)
    public MatchResponseDTO registerMatch(MatchRequestDTO dto) {
        Match match = new Match();
        applyDtoToEntity(dto, match);

        Match saved = matchRepository.save(match);
        return toResponseDTO(saved);
    }

    // UPDATE (recebe IDs, retorna objeto completo)
    public Optional<MatchResponseDTO> updateMatch(Long id, MatchRequestDTO dto) {
        return matchRepository.findById(id)
                .map(existing -> {
                    applyDtoToEntity(dto, existing);
                    Match updated = matchRepository.save(existing);
                    return toResponseDTO(updated);
                });
    }

    // DELETE
    public boolean deleteMatch(Long id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ==========================
    // Helpers (DTO <-> Entity)
    // ==========================

    private MatchResponseDTO toResponseDTO(Match match) {
        return new MatchResponseDTO(
                match.getId(),
                match.getDate(),
                match.getScoreTeamOne(),
                match.getScoreTeamTWO(),
                toTeamResponseDTO(match.getTeamOne()),
                toTeamResponseDTO(match.getTeamTWO()),
                toTeamResponseDTO(match.getSupportedTeam())
        );
    }

    private TeamResponseDTO toTeamResponseDTO(Team team) {
        if (team == null) return null;
        return new TeamResponseDTO(
                team.getId(),
                team.getName(),
                team.getState(),
                team.getPhotoUrl()
        );
    }

    private void applyDtoToEntity(MatchRequestDTO dto, Match match) {
        match.setDate(dto.date());
        match.setScoreTeamOne(dto.scoreTeamOne());
        match.setScoreTeamTWO(dto.scoreTeamTwo());

        match.setTeamOne(resolveTeamOrNull(dto.teamOneId()));
        match.setTeamTWO(resolveTeamOrNull(dto.teamTwoId()));
        match.setSupportedTeam(resolveTeamOrNull(dto.supportedTeamId()));
    }

    private Team resolveTeamOrNull(Long teamId) {
        if (teamId == null) return null;
        return teamRepository.findById(teamId).orElse(null);
    }
}
