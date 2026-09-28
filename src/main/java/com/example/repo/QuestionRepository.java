package com.example.repo;

import com.example.model.Question;
import com.example.model.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByDifficulty(Difficulty difficulty);

    List<Question> findByUnitId(Long unitId);

    List<Question> findByUnitIdAndDifficulty(
            Long unitId,
            Difficulty difficulty);

    @Query("""
                SELECT q, COUNT(tp)
                FROM Question q
                LEFT JOIN TestPaper tp
                ON q MEMBER OF tp.questions
                GROUP BY q
            """)
    List<Object[]> findQuestionUsageFrequency();
}
