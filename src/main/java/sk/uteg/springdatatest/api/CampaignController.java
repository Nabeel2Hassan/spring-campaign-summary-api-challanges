package sk.uteg.springdatatest.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.api.CampaignService;

import java.util.UUID;

@RestController("campaign")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }
    @GetMapping("/summary/{uuid}")
    public ResponseEntity<CampaignSummary> getSummary(@PathVariable UUID uuid) {
        return campaignService.getCampaignSummary(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
