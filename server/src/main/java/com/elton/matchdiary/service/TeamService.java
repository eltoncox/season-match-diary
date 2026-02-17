package com.elton.matchdiary.service;

import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.model.Team;
import com.elton.matchdiary.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Optional<TeamResponseDTO> getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(this::toDTO);
    }

    private TeamResponseDTO toDTO(Team team) {
        return new TeamResponseDTO(
                team.getId(),
                team.getName(),
                team.getState(),
                team.getPhotoUrl()
        );
    }
}
