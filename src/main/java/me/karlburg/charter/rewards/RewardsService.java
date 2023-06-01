package me.karlburg.charter.rewards;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    private static final SimpleDateFormat MONTH_NAME = new SimpleDateFormat("MMM");

    private final ScorecardRepository repository;
    public RewardsService(ScorecardRepository repository) {
        this.repository = repository;
    }

    public List<RewardDetails> getRewardAccounts() {
        return repository.findAllDistinctAccountNumber()
                .stream().map(this::getRewardBreakdowns).toList();
    }

    /**
     * Pulls all the scored points information for the account supplied.
     * <p>
     * This information is broken down into separate months showing the points
     * accrued for each section.
     * TODO: split this out to do better, dislike how this is laid-out (future)
     *
     * @param accountNumber account number to breakdown rewards
     * @return details of rewards breakdown
     */
    public RewardDetails getRewardBreakdowns(String accountNumber) {
        var scorecards = repository.findAllByAccountNumber(accountNumber);
        var totalPoints = scorecards.stream().mapToInt(Scorecard::getScore).sum();
        var breakdown = scorecards.stream().collect(Collectors.groupingBy(s -> {
            var cal = Calendar.getInstance();
            cal.setTime(s.getTimestamp());
            return new RewardMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        })).entrySet().stream().map(s -> {
            var monthlyTotal = s.getValue().stream().mapToInt(Scorecard::getScore).sum();
            return new RewardDetails.TimeRange(s.getKey().getYear(), s.getKey().getMonth(), monthlyTotal);
        }).toList();

        return new RewardDetails(accountNumber, totalPoints, breakdown);
    }

    public record RewardMonth(Integer year, Integer month) {

        public String getMonth() {
            var cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, month);
            return MONTH_NAME.format(cal.getTime());
        }

        public String getYear() {
            return String.valueOf(year);
        }
    }
}