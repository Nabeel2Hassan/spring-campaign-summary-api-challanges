package sk.uteg.springdatatest.api;

import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.api.model.OptionSummary;
import sk.uteg.springdatatest.api.model.QuestionSummary;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.db.model.QuestionType;
import sk.uteg.springdatatest.db.repository.CampaignRepository;
import sk.uteg.springdatatest.db.repository.FeedbackRepository;
import sk.uteg.springdatatest.db.repository.AnswerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final FeedbackRepository feedbackRepository;
    private final AnswerRepository answerRepository;

    public CampaignService(CampaignRepository campaignRepository, FeedbackRepository feedbackRepository, AnswerRepository answerRepository) {
        this.campaignRepository = campaignRepository;
        this.feedbackRepository = feedbackRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional(readOnly = true)
    public Optional<CampaignSummary> getCampaignSummary(UUID campaignId) {
        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (campaignOpt.isEmpty()) return Optional.empty();

        Campaign campaign = campaignOpt.get();
        long totalFeedbacks = feedbackRepository.countByCampaignId(campaignId);

        List<QuestionSummary> questionSummaries = campaign.getQuestions().stream().map(question -> {
            QuestionSummary questionSummary = new QuestionSummary();
            questionSummary.setName(question.getText());
            questionSummary.setType(question.getType());

            if (question.getType() == QuestionType.RATING) {
                questionSummary.setRatingAverage(
                    Optional.ofNullable(answerRepository.calculateAverageRating(question.getId()))
                            .orElse(BigDecimal.ZERO)
                );
                questionSummary.setOptionSummaries(Collections.emptyList());
            } else {
                // Get option counts from the repository
                List<Object[]> optionCountsResults = answerRepository.countOptionOccurrencesByQuestion(question.getId());

                // Create a map from the results for easy lookup
                Map<UUID, Integer> optionCounts = optionCountsResults.stream()
                        .collect(Collectors.toMap(row -> (UUID) row[0], row -> ((Number) row[1]).intValue()));

                // Map options to OptionSummary including those with zero occurrences
                List<OptionSummary> optionSummaries = question.getOptions().stream()
                        .map(option -> new OptionSummary(option.getText(), optionCounts.getOrDefault(option.getId(), 0)))
                        .collect(Collectors.toList());

                questionSummary.setOptionSummaries(optionSummaries);
                questionSummary.setRatingAverage(BigDecimal.ZERO);
            }

            return questionSummary;
        }).collect(Collectors.toList());

        CampaignSummary campaignSummary = new CampaignSummary();
        campaignSummary.setTotalFeedbacks(totalFeedbacks);
        campaignSummary.setQuestionSummaries(questionSummaries);

        return Optional.of(campaignSummary);
    }

}
