package ru.atlhnm.el.flex;

import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;

class FlexEvaluator {
    Try<Double> evaluate(String expression) {
        return Try.of(() -> eval(expression));

    }

    private Double eval(String expression) {
        final Option<Double> plusResult = findOperationPosition(Operation.PLUS, expression)
                .map(pos -> splitAt(expression, pos))
                .map(operands -> eval(operands._1) + eval(operands._2));

        final Option<Double> minusResult = plusResult.orElse(() ->
                findOperationPosition(Operation.MINUS, expression)
                        .map(pos -> splitAt(expression, pos))
                        .map(operands -> eval(operands._1) - eval(operands._2))
        );

        return minusResult.getOrElse(() -> Double.parseDouble(expression));
    }

    private Tuple2<String, String> splitAt(String expression, int pos) {
        var left = expression.substring(0, pos);
        var right = expression.substring(pos + 1);
        return new Tuple2<>(left, right);
    }

    private Option<Integer> findOperationPosition(Operation operation, String s) {
        switch (operation) {
            case PLUS:
                return Option.of(s.indexOf(Operation.PLUS.c)).filter(i -> i != -1);
            case MINUS:
                var minusPosition = s.indexOf(Operation.MINUS.c);
                while (minusPosition != -1) {
                    if (!s.substring(0, minusPosition).isBlank()) {
                        return Option.of(minusPosition);
                    }
                    minusPosition = s.indexOf(Operation.MINUS.c, minusPosition + 1);
                }
                return Option.none();
            case MULTIPLY:
                return Option.none();
            case DIVIDE:
                return Option.none();
        }
        throw new IllegalArgumentException(String.format("No such operation %s", operation));
    }

    private enum Operation {
        PLUS('+'), MINUS('-'), MULTIPLY('*'), DIVIDE('/');
        private final char c;

        Operation(char c) {
            this.c = c;
        }
    }
}
