package me.karlburg.charter.rewards;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record Transaction(
        @JsonProperty("transaction_id") String transactionID,
        @JsonProperty("account_number") String accountNumber,
        @JsonProperty("timestamp") Date timestamp,
        @JsonProperty("description") String description,
        @JsonProperty("amount") Float amount
) {}