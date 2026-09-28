package com.example.service;

import com.example.model.Difficulty;
import com.example.model.Question;
import com.example.model.Unit;
import com.example.repo.QuestionRepository;
import com.example.repo.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UnitRepository unitRepository;

    // Constructor Injection
    public QuestionService(
            QuestionRepository questionRepository,
            UnitRepository unitRepository) {

        this.questionRepository = questionRepository;
        this.unitRepository = unitRepository;
    }

    // =========================
    // ADD QUESTION
    // =========================
    public Question addQuestion(Question question) {

        // Validate question text
        if (question.getQuestionText() == null ||
                question.getQuestionText().trim().isEmpty()) {

            throw new RuntimeException(
                    "Question text is required."
            );
        }

        // Validate topic
        if (question.getTopic() == null ||
                question.getTopic().trim().isEmpty()) {

            throw new RuntimeException(
                    "Topic is required."
            );
        }

        // Validate difficulty
        if (question.getDifficulty() == null) {

            throw new RuntimeException(
                    "Difficulty is required."
            );
        }

        // Validate question type
        if (question.getQuestionType() == null) {

            throw new RuntimeException(
                    "Question type is required."
            );
        }

        // Validate Unit
        if (question.getUnit() == null ||
                question.getUnit().getId() == null) {

            throw new RuntimeException(
                    "Unit is required for a question."
            );
        }

        // Check whether Unit exists
        Long unitId = question.getUnit().getId();

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unit not found with ID: " + unitId
                        ));

        // Attach the existing Unit
        question.setUnit(unit);

        // Save question
        return questionRepository.save(question);
    }


    // =========================
    // GET ALL QUESTIONS
    // =========================
    public List<Question> getAllQuestions() {

        return questionRepository.findAll();
    }


    // =========================
    // GET QUESTION BY ID
    // =========================
    public Question getQuestionById(Long id) {

        return questionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Question not found with ID: " + id
                        ));
    }


    // =========================
    // GET QUESTIONS BY DIFFICULTY
    // =========================
    public List<Question> getQuestionsByDifficulty(
            Difficulty difficulty) {

        if (difficulty == null) {

            throw new RuntimeException(
                    "Difficulty is required."
            );
        }

        return questionRepository
                .findByDifficulty(difficulty);
    }


    // =========================
    // GET QUESTIONS BY UNIT
    // =========================
    public List<Question> getQuestionsByUnit(
            Long unitId) {

        // Check Unit exists
        if (!unitRepository.existsById(unitId)) {

            throw new RuntimeException(
                    "Unit not found with ID: " + unitId
            );
        }

        return questionRepository
                .findByUnitId(unitId);
    }


    // =========================
    // GET QUESTIONS BY UNIT
    // AND DIFFICULTY
    // =========================
    public List<Question> getQuestionsByUnitAndDifficulty(
            Long unitId,
            Difficulty difficulty) {

        // Check Unit exists
        if (!unitRepository.existsById(unitId)) {

            throw new RuntimeException(
                    "Unit not found with ID: " + unitId
            );
        }

        // Check difficulty
        if (difficulty == null) {

            throw new RuntimeException(
                    "Difficulty is required."
            );
        }

        return questionRepository
                .findByUnitIdAndDifficulty(
                        unitId,
                        difficulty
                );
    }


    // =========================
    // UPDATE QUESTION
    // =========================
    public Question updateQuestion(
            Long id,
            Question updatedQuestion) {

        // Find existing question
        Question existingQuestion =
                questionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Question not found with ID: " + id
                                ));

        // Validate question text
        if (updatedQuestion.getQuestionText() == null ||
                updatedQuestion.getQuestionText().trim().isEmpty()) {

            throw new RuntimeException(
                    "Question text is required."
            );
        }

        // Validate topic
        if (updatedQuestion.getTopic() == null ||
                updatedQuestion.getTopic().trim().isEmpty()) {

            throw new RuntimeException(
                    "Topic is required."
            );
        }

        // Validate difficulty
        if (updatedQuestion.getDifficulty() == null) {

            throw new RuntimeException(
                    "Difficulty is required."
            );
        }

        // Validate question type
        if (updatedQuestion.getQuestionType() == null) {

            throw new RuntimeException(
                    "Question type is required."
            );
        }

        // Validate Unit
        if (updatedQuestion.getUnit() == null ||
                updatedQuestion.getUnit().getId() == null) {

            throw new RuntimeException(
                    "Unit is required."
            );
        }

        // Find the Unit
        Long unitId = updatedQuestion
                .getUnit()
                .getId();

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unit not found with ID: " + unitId
                        ));

        // Update fields
        existingQuestion.setQuestionText(
                updatedQuestion.getQuestionText()
        );

        existingQuestion.setTopic(
                updatedQuestion.getTopic()
        );

        existingQuestion.setDifficulty(
                updatedQuestion.getDifficulty()
        );

        existingQuestion.setQuestionType(
                updatedQuestion.getQuestionType()
        );

        existingQuestion.setOptionA(
                updatedQuestion.getOptionA()
        );

        existingQuestion.setOptionB(
                updatedQuestion.getOptionB()
        );

        existingQuestion.setOptionC(
                updatedQuestion.getOptionC()
        );

        existingQuestion.setOptionD(
                updatedQuestion.getOptionD()
        );

        existingQuestion.setCorrectAnswer(
                updatedQuestion.getCorrectAnswer()
        );

        existingQuestion.setUnit(unit);

        // Save updated question
        return questionRepository.save(
                existingQuestion
        );
    }


    // =========================
    // DELETE QUESTION
    // =========================
    public void deleteQuestion(Long id) {

        if (!questionRepository.existsById(id)) {

            throw new RuntimeException(
                    "Cannot delete. Question not found with ID: " + id
            );
        }

        questionRepository.deleteById(id);
    }
    
    // ==========================================
    // QUESTION USAGE FREQUENCY
    // ==========================================
    public List<Object[]> getQuestionUsageFrequency() {

        return questionRepository
                .findQuestionUsageFrequency();
    }
}
