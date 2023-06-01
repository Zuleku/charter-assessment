package me.karlburg.charter;
import me.karlburg.charter.rewards.ScoringService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class ScoringServiceTests {

    // no reason to mock a database repository as that is testing a separate
    // feature and using a well tested library
    private ScoringService service = new ScoringService(null);

    @ParameterizedTest
    @MethodSource("defaultProgramValues")
    @DisplayName("default program scoring should return correct amount")
    public void testScoreDefaultProgram(Float input, Integer expected) {
        var output = service.scoreDefaultProgram(input);
        Assertions.assertEquals(expected, output);
    }

    private static Stream<Arguments> defaultProgramValues() {
        return Stream.of(
                Arguments.of(62.99f, 12),
                Arguments.of(127.20f, 104),
                Arguments.of(23.14f, 0),
                Arguments.of(75.00f, 25)
        );
    }
}