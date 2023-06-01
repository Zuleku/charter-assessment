package me.karlburg.charter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Autowired
    private ObjectMapper mapper;

    @LocalServerPort
    private Integer serverPort;

    @Autowired
    private TestRestTemplate template;

    private List<String> scoredAccounts = new ArrayList<>();

    @Test
    @DisplayName("all three test accounts should be returned from endpoint")
    public void checkAllTransactions() throws Exception {
        postAccountTransactions("account_1234567890.json");
        postAccountTransactions("account_2468135790.json");
        postAccountTransactions("account_9876543210.json");

        var getEndpoint = makeEndpointURL("/rewards");
        var response = template.getForEntity(getEndpoint, List.class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());

        var responseBody = response.getBody();
        Assertions.assertEquals(3, responseBody.size());

        // arguably check for each individual account and each value returned,
        // although there would most likely be a better endpoint to check for
        // single individual account details
    }

    @Test
    @DisplayName("monthly amount for account should be correct")
    public void checkMonthlyPoints() throws Exception {
        postAccountTransactions("account_1234567890.json");
        postAccountTransactions("account_2468135790.json");
        postAccountTransactions("account_9876543210.json");

        var getEndpoint = makeEndpointURL("/rewards/1234567890");
        var response = template.getForEntity(getEndpoint, Map.class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());

        var responseBody = response.getBody();
        Assertions.assertEquals("1234567890", responseBody.get("account_number"));
        Assertions.assertEquals(1393, responseBody.get("total_points"));
        Assertions.assertNotNull(responseBody.get("monthly_breakdown"));

        var monthlyBreakdown = ((List<Map>)responseBody.get("monthly_breakdown")).get(0);
        Assertions.assertEquals("Jan", monthlyBreakdown.get("month"));
        Assertions.assertEquals(187, monthlyBreakdown.get("points_accrued"));
    }

    public final String getResource(String filename) throws IOException {
        var resource = new ClassPathResource(filename);
        return resource.getContentAsString(StandardCharsets.UTF_8);
    }

    public final String makeEndpointURL(String endpoint) {
        return "http://localhost:%d%s".formatted(serverPort, endpoint);
    }

    public void postAccountTransactions(String account) throws IOException {
        // this should be converted to a TestExecutionListener to implement
        // calling endpoint in a 'BeforeAll' scenario
        if(scoredAccounts.contains(account)) { return; }
        scoredAccounts.add(account);

        var postEndpoint = makeEndpointURL("/rewards/transactions");
        var transactions = getResource(account);
        var objTransactions = mapper.readValue(transactions, List.class);
        var response = template.postForEntity(postEndpoint, objTransactions, String.class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}