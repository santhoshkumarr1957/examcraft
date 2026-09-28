package com.example.service;

import com.example.model.Difficulty;
import com.example.model.Question;
import com.example.model.TestPaper;
import com.example.repo.QuestionRepository;
import com.example.repo.TestPaperRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TestPaperService {

    private final TestPaperRepository testPaperRepository;
    private final QuestionRepository questionRepository;

    public TestPaperService(
            TestPaperRepository testPaperRepository,
            QuestionRepository questionRepository) {

        this.testPaperRepository = testPaperRepository;
        this.questionRepository = questionRepository;
    }

    // ==========================================
    // GENERATE TEST PAPER
    // ==========================================
    public TestPaper generateTestPaper(
            String title,
            int totalQuestions,
            int easyPercentage,
            int mediumPercentage,
            int hardPercentage) {

        // ------------------------------------------
        // 1. Validate total questions
        // ------------------------------------------

        if (totalQuestions <= 0) {
            throw new RuntimeException(
                    "Total questions must be greater than 0."
            );
        }


        // ------------------------------------------
        // 2. Validate difficulty percentages
        // ------------------------------------------

        if (easyPercentage < 0 ||
                mediumPercentage < 0 ||
                hardPercentage < 0) {

            throw new RuntimeException(
                    "Difficulty percentages cannot be negative."
            );
        }


        // ------------------------------------------
        // 3. Check percentages total 100
        // ------------------------------------------

        int percentageTotal =
                easyPercentage
                        + mediumPercentage
                        + hardPercentage;

        if (percentageTotal != 100) {

            throw new RuntimeException(
                    "Difficulty percentages must total 100%."
            );
        }


        // ------------------------------------------
        // 4. Calculate required questions
        // ------------------------------------------

        int easyCount =
                (totalQuestions * easyPercentage) / 100;

        int mediumCount =
                (totalQuestions * mediumPercentage) / 100;

        int hardCount =
                totalQuestions
                        - easyCount
                        - mediumCount;


        // ------------------------------------------
        // 5. Get questions from database
        // ------------------------------------------

        List<Question> easyQuestions =
                questionRepository
                        .findByDifficulty(Difficulty.EASY);

        List<Question> mediumQuestions =
                questionRepository
                        .findByDifficulty(Difficulty.MEDIUM);

        List<Question> hardQuestions =
                questionRepository
                        .findByDifficulty(Difficulty.HARD);


        // ------------------------------------------
        // 6. Check availability
        // ------------------------------------------

        if (easyQuestions.size() < easyCount) {

            throw new RuntimeException(
                    "Not enough EASY questions available. " +
                    "Required: " + easyCount +
                    ", Available: " + easyQuestions.size()
            );
        }


        if (mediumQuestions.size() < mediumCount) {

            throw new RuntimeException(
                    "Not enough MEDIUM questions available. " +
                    "Required: " + mediumCount +
                    ", Available: " + mediumQuestions.size()
            );
        }


        if (hardQuestions.size() < hardCount) {

            throw new RuntimeException(
                    "Not enough HARD questions available. " +
                    "Required: " + hardCount +
                    ", Available: " + hardQuestions.size()
            );
        }


        // ------------------------------------------
        // 7. Randomize questions
        // ------------------------------------------

        Collections.shuffle(easyQuestions);
        Collections.shuffle(mediumQuestions);
        Collections.shuffle(hardQuestions);


        // ------------------------------------------
        // 8. Select required questions
        // ------------------------------------------

        List<Question> selectedQuestions =
                new ArrayList<>();

        selectedQuestions.addAll(
                easyQuestions.subList(
                        0,
                        easyCount
                )
        );

        selectedQuestions.addAll(
                mediumQuestions.subList(
                        0,
                        mediumCount
                )
        );

        selectedQuestions.addAll(
                hardQuestions.subList(
                        0,
                        hardCount
                )
        );


        // ------------------------------------------
        // 9. Final duplicate check
        // ------------------------------------------

        long uniqueQuestionCount =
                selectedQuestions
                        .stream()
                        .map(Question::getId)
                        .distinct()
                        .count();

        if (uniqueQuestionCount
                != selectedQuestions.size()) {

            throw new RuntimeException(
                    "Duplicate question detected while generating the test paper."
            );
        }


        // ------------------------------------------
        // 10. Create TestPaper
        // ------------------------------------------

        TestPaper testPaper =
                new TestPaper();

        testPaper.setTitle(title);

        testPaper.setTotalQuestions(
                totalQuestions
        );

        testPaper.setEasyCount(
                easyCount
        );

        testPaper.setMediumCount(
                mediumCount
        );

        testPaper.setHardCount(
                hardCount
        );

        testPaper.setQuestions(
                selectedQuestions
        );


        // ------------------------------------------
        // 11. Save TestPaper
        // ------------------------------------------

        return testPaperRepository.save(testPaper);
    }


    // ==========================================
    // GET ALL TEST PAPERS
    // ==========================================
    public List<TestPaper> getAllTestPapers() {

        return testPaperRepository.findAll();
    }


    // ==========================================
    // GET TEST PAPER BY ID
    // ==========================================
    public TestPaper getTestPaperById(Long id) {

        return testPaperRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Test paper not found with ID: " + id
                        ));
    }


    // ==========================================
    // DELETE TEST PAPER
    // ==========================================
    public void deleteTestPaper(Long id) {

        if (!testPaperRepository.existsById(id)) {

            throw new RuntimeException(
                    "Test paper not found with ID: " + id
            );
        }

        testPaperRepository.deleteById(id);
    }
}
