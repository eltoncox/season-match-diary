package com.elton.matchdiary.service;

import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.model.Team;
import com.elton.matchdiary.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamResponseDTO> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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


    public TeamResponseDTO createTeam(Team team) {
        Team savedTeam = teamRepository.save(team);
        return toDTO(savedTeam);
    }

    public Optional<TeamResponseDTO> updateTeam(Long id, Team team) {

        return teamRepository.findById(id)
                .map(existingTeam -> {
                    existingTeam.setName(team.getName());
                    existingTeam.setState(team.getState());
                    existingTeam.setPhotoUrl(team.getPhotoUrl());

                    Team updated = teamRepository.save(existingTeam);
                    return toDTO(updated);
                });
    }

    public boolean deleteTeam(Long id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
