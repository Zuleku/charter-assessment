package me.karlburg.charter.rewards;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity(name = "score")
@Table(name = "scores")
public class Scorecard {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;
    public Long getIdentifier() { return identifier; }

    @Column(name = "account_number")
    private String accountNumber;
    public String getAccountNumber() { return accountNumber; }

    @Column(name = "score")
    private Integer score;
    public Integer getScore() { return score; }

    @Column(name = "timestamp")
    private Date timestamp;
    public Date getTimestamp() { return timestamp; }

    /* JPA-specification required constructor */
    protected Scorecard() {}

    public Scorecard(String accountNumber, Integer score, Date timestamp) {
        this.accountNumber = accountNumber;
        this.score = score;
        this.timestamp = timestamp;
    }
}