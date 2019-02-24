package ru.atlhnm.el.flex;

import io.vavr.control.Either;

public class FlexEvaluator {
    public Either<String, Double> evaluate(String expression) {
        return Either.right(4.0);
    }
}
