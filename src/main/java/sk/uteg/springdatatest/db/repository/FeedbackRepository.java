package sk.uteg.springdatatest.db.repository;

import org.springframework.data.repository.CrudRepository;
import sk.uteg.springdatatest.db.model.Feedback;

import java.util.UUID;

public interface FeedbackRepository extends CrudRepository<Feedback, UUID> {

    long countByCampaignId(UUID campaignId);
}
