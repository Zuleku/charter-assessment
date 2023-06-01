package me.karlburg.charter.rewards;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RewardDetails(
        @JsonProperty("account_number") String accountNumber,
        @JsonProperty("total_points") Integer totalPoints,
        @JsonProperty("monthly_breakdown") List<TimeRange> ranges) {

    public record TimeRange(
            @JsonProperty("year") String year,
            @JsonProperty("month") String month,
            @JsonProperty("points_accrued") Integer pointsAccrued) {}
}