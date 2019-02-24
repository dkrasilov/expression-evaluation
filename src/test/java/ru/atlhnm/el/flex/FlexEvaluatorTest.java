package ru.atlhnm.el.flex;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

}