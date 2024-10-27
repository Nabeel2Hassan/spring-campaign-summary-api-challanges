package sk.uteg.springdatatest.db.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sk.uteg.springdatatest.db.model.Answer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, UUID> {

    @Query("SELECT AVG(a.ratingValue) FROM Answer a WHERE a.question.id = :questionId")
    BigDecimal calculateAverageRating(UUID questionId);

    @Query("SELECT o.id, COUNT(a) FROM Answer a JOIN a.selectedOptions o WHERE a.question.id = :questionId GROUP BY o.id")
    List<Object[]> countOptionOccurrencesByQuestion(UUID questionId);

}
