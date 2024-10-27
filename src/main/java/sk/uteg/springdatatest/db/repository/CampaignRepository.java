package sk.uteg.springdatatest.db.repository;

import org.springframework.data.repository.CrudRepository;
import sk.uteg.springdatatest.db.model.Campaign;

import java.util.UUID;
public interface CampaignRepository extends CrudRepository<Campaign, UUID> {

}
