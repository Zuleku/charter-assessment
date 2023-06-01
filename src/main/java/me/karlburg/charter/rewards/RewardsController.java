package me.karlburg.charter.rewards;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/rewards")
public class RewardsController {

    private final RewardsService rewardsService;
    private final ScoringService scoringService;
    public RewardsController(RewardsService rewardsService,
                             ScoringService scoringService) {
        this.rewardsService = rewardsService;
        this.scoringService = scoringService;
    }

    @GetMapping
    public Object getRewardDetails() {
        return rewardsService.getRewardAccounts();
    }

    @GetMapping("/{accountNumber}")
    public Object getRewardDetailsByAccount(@PathVariable String accountNumber) {
        return rewardsService.getRewardBreakdowns(accountNumber);
    }

    @PostMapping("/transactions")
    public void parseTransactions(@RequestBody List<Transaction> transactions) {
        transactions.forEach(scoringService::scoreTransaction);
    }
}