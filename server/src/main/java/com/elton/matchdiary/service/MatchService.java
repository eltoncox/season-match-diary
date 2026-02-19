package com.elton.matchdiary.service;

import com.elton.matchdiary.dto.match.MatchRequestDTO;
import com.elton.matchdiary.dto.match.MatchResponseDTO;
import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.exception.BusinessException;
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
        validateMatchRules(dto);

        Match match = new Match();
        applyDtoToEntity(dto, match);

        Match saved = matchRepository.save(match);
        return toResponseDTO(saved);
    }

    // UPDATE (recebe IDs, retorna objeto completo)
    public Optional<MatchResponseDTO> updateMatch(Long id, MatchRequestDTO dto) {
        validateMatchRules(dto);

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
    // Validations
    // ==========================

    private void validateMatchRules(MatchRequestDTO dto) {
        Long teamOneId = dto.teamOneId();
        Long teamTwoId = dto.teamTwoId();

        // obrigatórios
        if (teamOneId == null) {
            throw new BusinessException("teamOneId é obrigatório.");
        }
        if (teamTwoId == null) {
            throw new BusinessException("teamTwoId é obrigatório.");
        }

        // regra principal: não pode jogar contra si mesmo
        if (teamOneId.equals(teamTwoId)) {
            throw new BusinessException("teamOneId e teamTwoId não podem ser iguais (partida contra o mesmo time).");
        }

        // supportedTeam deve ser um dos dois times (se informado)
        Long supportedId = dto.supportedTeamId();
        if (supportedId != null && !supportedId.equals(teamOneId) && !supportedId.equals(teamTwoId)) {
            throw new BusinessException("supportedTeamId deve ser igual a teamOneId ou teamTwoId.");
        }

        // placar não pode ser negativo
        if (dto.scoreTeamOne() != null && dto.scoreTeamOne() < 0) {
            throw new BusinessException("scoreTeamOne não pode ser negativo.");
        }
        if (dto.scoreTeamTwo() != null && dto.scoreTeamTwo() < 0) {
            throw new BusinessException("scoreTeamTwo não pode ser negativo.");
        }
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

        match.setTeamOne(resolveTeamRequired(dto.teamOneId(), "teamOneId"));
        match.setTeamTWO(resolveTeamRequired(dto.teamTwoId(), "teamTwoId"));
        match.setSupportedTeam(resolveTeamOptional(dto.supportedTeamId(), "supportedTeamId"));
    }

    private Team resolveTeamRequired(Long teamId, String fieldName) {
        if (teamId == null) {
            throw new BusinessException(fieldName + " é obrigatório.");
        }
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("Time não encontrado para " + fieldName + ": " + teamId));
    }

    private Team resolveTeamOptional(Long teamId, String fieldName) {
        if (teamId == null) return null;
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException("Time não encontrado para " + fieldName + ": " + teamId));
    }
}
