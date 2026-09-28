package com.example.controller;

import com.example.model.TestPaper;
import com.example.service.TestPaperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testpapers")
public class TestPaperController {

    private final TestPaperService testPaperService;

    public TestPaperController(TestPaperService testPaperService) {
        this.testPaperService = testPaperService;
    }

    // ==========================================
    // GENERATE TEST PAPER
    // ==========================================
    @PostMapping("/generate")
    public TestPaper generateTestPaper(
            @RequestParam String title,
            @RequestParam int totalQuestions,
            @RequestParam int easyPercentage,
            @RequestParam int mediumPercentage,
            @RequestParam int hardPercentage) {

        return testPaperService.generateTestPaper(
                title,
                totalQuestions,
                easyPercentage,
                mediumPercentage,
                hardPercentage
        );
    }

    // ==========================================
    // GET ALL TEST PAPERS
    // ==========================================
    @GetMapping("/get")
    public List<TestPaper> getAllTestPapers() {
        return testPaperService.getAllTestPapers();
    }

    // ==========================================
    // GET TEST PAPER BY ID
    // ==========================================
    @GetMapping("/get/{id}")
    public TestPaper getTestPaper(@PathVariable Long id) {
        return testPaperService.getTestPaperById(id);
    }

    // ==========================================
    // DELETE TEST PAPER
    // ==========================================
    @DeleteMapping("/delete/{id}")
    public String deleteTestPaper(@PathVariable Long id) {

        testPaperService.deleteTestPaper(id);

        return "Test paper deleted successfully";
    }
}
