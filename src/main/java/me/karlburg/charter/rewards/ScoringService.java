package me.karlburg.charter.rewards;

import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    /* Framework Injected Components */
    private final ScorecardRepository repository;
    public ScoringService(ScorecardRepository repository) {
        this.repository = repository;
    }

    /**
     * Default scoring system used for all transaction amounts that do not
     * match any specific program.
     *
     * @param amount transaction amount to score
     * @return score points accrued from the reward program
     */
    public Integer scoreDefaultProgram(Float amount) {
        int transAmount = (int)Math.floor(amount);
        if(transAmount < 50) { return 0; }

        int tier1 = transAmount - 50 - Math.max(0, (transAmount - 100));
        int tier2 = Math.max(0, (transAmount - 100)) * 2;

        return tier1 + tier2;
    }

    /**
     * Scores the transaction based on available data and current programs.
     * <p>
     * The scoring system is designed to award points based on the amount of
     * currency used. Each program is responsible for determining the rewards
     * allowed and gained through various means.
     * <p>
     * Default program contains a two tier approach that will award the
     * transaction one point per dollar spent between $50 and $100, and
     * increase the points to two per each dollar spent above $100.
     *
     * @param transaction data transaction to score
     */
    public void scoreTransaction(Transaction transaction) {
        var pointsScored = scoreDefaultProgram(transaction.amount());
        var scorecard = new Scorecard(transaction.accountNumber(),
                pointsScored, transaction.timestamp());
        repository.save(scorecard);
    }
}