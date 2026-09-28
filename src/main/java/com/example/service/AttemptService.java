package com.example.service;

import com.example.model.Attempt;
import com.example.model.AttemptAnswer;
import com.example.model.Question;
import com.example.model.TestPaper;
import com.example.repo.AttemptAnswerRepository;
import com.example.repo.AttemptRepository;
import com.example.repo.QuestionRepository;
import com.example.repo.TestPaperRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttemptService {

        private final AttemptRepository attemptRepository;
        private final AttemptAnswerRepository attemptAnswerRepository;
        private final TestPaperRepository testPaperRepository;
        private final QuestionRepository questionRepository;

        public AttemptService(
                        AttemptRepository attemptRepository,
                        AttemptAnswerRepository attemptAnswerRepository,
                        TestPaperRepository testPaperRepository,
                        QuestionRepository questionRepository) {

                this.attemptRepository = attemptRepository;
                this.attemptAnswerRepository = attemptAnswerRepository;
                this.testPaperRepository = testPaperRepository;
                this.questionRepository = questionRepository;
        }

        // ==========================================
        // SUBMIT ATTEMPT
        // ==========================================

        public Attempt submitAttempt(
                        Long testPaperId,
                        String studentName,
                        List<AnswerRequest> answers) {

                // ------------------------------------------
                // 1. Check student name
                // ------------------------------------------

                if (studentName == null || studentName.trim().isEmpty()) {
                        throw new RuntimeException(
                                        "Student name is required.");
                }

                // ------------------------------------------
                // 2. Find test paper
                // ------------------------------------------

                TestPaper testPaper = testPaperRepository
                                .findById(testPaperId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Test paper not found with ID: "
                                                                + testPaperId));

                // ------------------------------------------
                // 3. Check answers
                // ------------------------------------------

                if (answers == null || answers.isEmpty()) {
                        throw new RuntimeException(
                                        "At least one answer is required.");
                }

                // ------------------------------------------
                // 4. Create Attempt
                // ------------------------------------------

                Attempt attempt = new Attempt();

                attempt.setStudentName(studentName);
                attempt.setTestPaper(testPaper);
                attempt.setSubmittedAt(LocalDateTime.now());

                attempt.setTotalMarks(
                                testPaper.getTotalQuestions());

                attempt.setScore(0);

                // ------------------------------------------
                // 5. IMPORTANT:
                // Save Attempt first
                // This generates the Attempt ID
                // ------------------------------------------

                attempt = attemptRepository.save(attempt);

                int score = 0;

                // ------------------------------------------
                // 6. Check each answer
                // ------------------------------------------

                for (AnswerRequest answerRequest : answers) {

                        // Check question ID
                        if (answerRequest.getQuestionId() == null) {
                                throw new RuntimeException(
                                                "Question ID is required.");
                        }

                        // Find question
                        Question question = questionRepository
                                        .findById(answerRequest.getQuestionId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Question not found with ID: "
                                                                        + answerRequest.getQuestionId()));

                        // ------------------------------------------
                        // 7. Make sure question belongs to test paper
                        // ------------------------------------------

                        boolean questionInPaper = testPaper.getQuestions()
                                        .stream()
                                        .anyMatch(q -> q.getId()
                                                        .equals(question.getId()));

                        if (!questionInPaper) {
                                throw new RuntimeException(
                                                "Question "
                                                                + question.getId()
                                                                + " does not belong to this test paper.");
                        }

                        // ------------------------------------------
                        // 8. Create AttemptAnswer
                        // ------------------------------------------

                        AttemptAnswer attemptAnswer = new AttemptAnswer();

                        attemptAnswer.setAttempt(attempt);
                        attemptAnswer.setQuestion(question);

                        attemptAnswer.setSelectedAnswer(
                                        answerRequest.getSelectedAnswer());

                        // ------------------------------------------
                        // 9. Auto grading
                        // ------------------------------------------

                        boolean correct = false;

                        if (answerRequest.getSelectedAnswer() != null
                                        && question.getCorrectAnswer() != null) {

                                correct = question.getCorrectAnswer()
                                                .equalsIgnoreCase(
                                                                answerRequest
                                                                                .getSelectedAnswer());
                        }

                        attemptAnswer.setCorrect(correct);

                        if (correct) {
                                score++;
                        }

                        // ------------------------------------------
                        // 10. Save answer
                        // ------------------------------------------

                        attemptAnswerRepository.save(attemptAnswer);
                }

                // ------------------------------------------
                // 11. Set final score
                // ------------------------------------------

                attempt.setScore(score);

                // ------------------------------------------
                // 12. Save final Attempt
                // ------------------------------------------

                return attemptRepository.save(attempt);
        }

        // ==========================================
        // GET ATTEMPT BY ID
        // ==========================================

        public Attempt getAttemptById(Long id) {

                return attemptRepository
                                .findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Attempt not found with ID: " + id));
        }

        // ==========================================
        // GET ALL ATTEMPTS
        // ==========================================

        public List<Attempt> getAllAttempts() {

                return attemptRepository.findAll();
        }

        // ==========================================
        // GET ATTEMPTS FOR TEST PAPER
        // ==========================================

        public List<Attempt> getAttemptsByTestPaper(
                        Long testPaperId) {

                if (!testPaperRepository.existsById(testPaperId)) {

                        throw new RuntimeException(
                                        "Test paper not found with ID: "
                                                        + testPaperId);
                }

                return attemptRepository
                                .findByTestPaperId(testPaperId);
        }
}
