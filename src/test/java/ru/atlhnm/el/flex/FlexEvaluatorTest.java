package ru.atlhnm.el.flex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlexEvaluatorTest {
    private FlexEvaluator flexEvaluator;

    @BeforeEach
    void setUp() {
        flexEvaluator = new FlexEvaluator();
    }

    @Test
    @DisplayName("-1.123")
    void minusOneCommaOneTwoThree() {
        var result = flexEvaluator.evaluate("-1.123");
        assertEquals(-1.123, result.get());
    }

    @Test
    @DisplayName("2 + 2")
    void twoPlusTwo() {
        var result = flexEvaluator.evaluate("2 + 2");
        assertEquals(4, result.get());
    }

    @Test
    @DisplayName("2 + -2")
    void twoPlusMinusTwo() {
        var result = flexEvaluator.evaluate("2 + -2");
        assertEquals(0, result.get());
    }

    @Test
    @DisplayName("2 - 2")
    void twoMinusTwo() {
        var result = flexEvaluator.evaluate("2 - 2");
        assertEquals(0, result.get());
    }

    @Test
    @DisplayName("2 - 2 + 2")
    void twoMinusTwoPlusTwo() {
        var result = flexEvaluator.evaluate("2 - 2 + 2");
        assertEquals(2, result.get());
    }

    @Test
    @DisplayName("-2 - -2 + -2")
    void minusTwoMinusMinusTwoPlusMinusTwo() {
        var result = flexEvaluator.evaluate("-2 - -2 + -2");
        assertEquals(-2, result.get());
    }

    @Test
    @DisplayName("2 * -2")
    void twoTimesMinusTwo() {
        var result = flexEvaluator.evaluate("2 * -2");
        assertEquals(-4, result.get());
    }

    @Test
    @DisplayName("2 / 2")
    void twoByTwo() {
        var result = flexEvaluator.evaluate("2 / 2");
        assertEquals(1, result.get());
    }

    @Test
    @DisplayName("2 * 2")
    void twoTimesTwo() {
        var result = flexEvaluator.evaluate("2 * 2");
        assertEquals(4, result.get());
    }

    @Test
    @DisplayName("2 * 2 + 4 * 0")
    void o2x2p4x0() {
        var result = flexEvaluator.evaluate("2 * 2 + 4 * 0");
        assertEquals(4, result.get());
    }

    @Test
    @DisplayName("4 - 4 / 2 + 2 * 2 - 8 / 4")
    void sfsdfsdfsdg() {
        var result = flexEvaluator.evaluate("4 - 4 / 2 + 2 * 2 - 8 / 4");
        assertEquals(4, result.get());
    }

    @Test
    @DisplayName("2 * 2 / 4 * 2")
    void dsfsddsdsf() {
        var result = flexEvaluator.evaluate("2 * 2 / 4 * 2");
        assertEquals(2, result.get());
    }

    @Test
    @DisplayName("2 + (2 + 2) / 8")
    void baseBracketTest() {
        var result = flexEvaluator.evaluate("2 * (2 + 2) / 8");
        assertEquals(1, result.get());
    }

    @Test
    @DisplayName("-(2 + 2)")
    void gsdfsdfsddf() {
        var result = flexEvaluator.evaluate("-(2 + 2)");
        assertEquals(-4, result.get());
    }

    @Test
    @DisplayName("(2 / (2 + 3.33) * 4) - -6")
    void exampleFromCodewars() {
        var result = flexEvaluator.evaluate("(5 / (2 + 3) * 4) - -6");
        assertEquals(10, result.get());
    }

}