package com.elton.matchdiary.controller;

import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.model.Team;
import com.elton.matchdiary.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/all")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }


    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id) {

        return teamService.getTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TeamResponseDTO> addTeam(@RequestBody Team team) {

        TeamResponseDTO createdTeam = teamService.createTeam(team);

        return ResponseEntity
                .status(201)
                .body(createdTeam);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable Long id,
            @RequestBody Team team) {

        return teamService.updateTeam(id, team)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {

        if (teamService.deleteTeam(id)) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.notFound().build(); // 404
    }

}
