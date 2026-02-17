package com.elton.matchdiary.controller;

import com.elton.matchdiary.dto.team.TeamResponseDTO;
import com.elton.matchdiary.model.Team;
import com.elton.matchdiary.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/all")
    public String getAllTeams() {
        return "Teste";
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id) {

        return teamService.getTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




    @PostMapping()
    public String addTeam(@RequestBody Team team) {
        return "";
    }

    @PutMapping
    public String updateTeam(@RequestBody Team team) {
        return "";
    }

    @DeleteMapping("{id}")
    public String deleteTeam(@RequestParam("id") Long id) {
        return "";
    }
}
