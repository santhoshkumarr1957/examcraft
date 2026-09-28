package com.example.controller;

import com.example.model.Attempt;
import com.example.service.AnswerRequest;
import com.example.service.AttemptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attempts")
@CrossOrigin(origins = "*")
public class AttemptController {

    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    // ==========================================
    // SUBMIT ATTEMPT
    // ==========================================
    @PostMapping("/submit")
    public Attempt submitAttempt(
            @RequestParam Long testPaperId,
            @RequestParam String studentName,
            @RequestBody List<AnswerRequest> answers) {

        return attemptService.submitAttempt(
                testPaperId,
                studentName,
                answers);
    }

    // ==========================================
    // GET ATTEMPT BY ID
    // ==========================================
    @GetMapping("/get/{id}")
    public Attempt getAttempt(@PathVariable Long id) {

        return attemptService.getAttemptById(id);
    }

    // ==========================================
    // GET ALL ATTEMPTS
    // ==========================================
    @GetMapping("/get")
    public List<Attempt> getAllAttempts() {

        return attemptService.getAllAttempts();
    }

    // ==========================================
    // GET ATTEMPTS BY TEST PAPER
    // ==========================================
    @GetMapping("/get/testpaper/{testPaperId}")
    public List<Attempt> getAttemptsByTestPaper(
            @PathVariable Long testPaperId) {

        return attemptService
                .getAttemptsByTestPaper(testPaperId);
    }
}
