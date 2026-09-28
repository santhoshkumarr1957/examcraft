package com.example.controller;

import com.example.model.Difficulty;
import com.example.model.Question;
import com.example.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // =========================
    // ADD QUESTION
    // =========================
    @PostMapping("/add")
    public Question addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }


    // =========================
    // GET ALL QUESTIONS
    // =========================
    @GetMapping("/get")
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }


    // =========================
    // GET QUESTION BY ID
    // =========================
    @GetMapping("/get/{id}")
    public Question getQuestion(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }


    // =========================
    // GET BY DIFFICULTY
    // =========================
    @GetMapping("/get/difficulty/{difficulty}")
    public List<Question> getByDifficulty(
            @PathVariable Difficulty difficulty) {

        return questionService
                .getQuestionsByDifficulty(difficulty);
    }


    // =========================
    // GET BY UNIT
    // =========================
    @GetMapping("/get/unit/{unitId}")
    public List<Question> getByUnit(
            @PathVariable Long unitId) {

        return questionService
                .getQuestionsByUnit(unitId);
    }


    // =========================
    // GET BY UNIT + DIFFICULTY
    // =========================
    @GetMapping("/get/unit/{unitId}/difficulty/{difficulty}")
    public List<Question> getByUnitAndDifficulty(
            @PathVariable Long unitId,
            @PathVariable Difficulty difficulty) {

        return questionService
                .getQuestionsByUnitAndDifficulty(
                        unitId,
                        difficulty
                );
    }


    // =========================
    // UPDATE QUESTION
    // =========================
    @PutMapping("/update/{id}")
    public Question updateQuestion(
            @PathVariable Long id,
            @RequestBody Question question) {

        return questionService.updateQuestion(
                id,
                question
        );
    }


    // =========================
    // DELETE QUESTION
    // =========================
    @DeleteMapping("/delete/{id}")
    public String deleteQuestion(
            @PathVariable Long id) {

        questionService.deleteQuestion(id);

        return "Question deleted successfully";
    }
    
    // ==========================================
    // QUESTION USAGE FREQUENCY
    // ==========================================
    @GetMapping("/usage")
    public List<Object[]> getQuestionUsageFrequency() {

        return questionService
                .getQuestionUsageFrequency();
    }
}