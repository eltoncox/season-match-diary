package com.elton.matchdiary.controller;

import com.elton.matchdiary.dto.summary.SummaryResponseDTO;
import com.elton.matchdiary.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/all")
    public ResponseEntity<SummaryResponseDTO> getSummary() {
        return ResponseEntity.ok(summaryService.getSummary());
    }
}
