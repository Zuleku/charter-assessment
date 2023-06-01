package me.karlburg.charter.rewards;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScorecardRepository extends CrudRepository<Scorecard, Long> {

    List<Scorecard> findAllByAccountNumber(String accountNumber);

    @Query("SELECT DISTINCT x.accountNumber FROM score x")
    List<String> findAllDistinctAccountNumber();
}