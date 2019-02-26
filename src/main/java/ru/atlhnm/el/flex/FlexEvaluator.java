package ru.atlhnm.el.flex;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.TreeSet;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.stream.Stream;

class FlexEvaluator {
    Try<Double> evaluate(String expression) {
        return Try.of(() -> evalNested(expression));
    }

    private Double evalNested(String baseExpressionString) {
        final var expression = unwrapBrackets(baseExpressionString);

        return tryEvalOperation(expression, Operation.PLUS)
                .orElse(() -> tryEvalOperation(expression, Operation.MINUS))
                .orElse(() -> {
                    //if we here there is no operations to do except [*/] and no nested values
                    final var operations = findOperationsOrdered(expression, Operation.DIVIDE, Operation.MULTIPLY);
                    if (operations.isEmpty())
                        return Option.none();

                    final var operands = List.of(expression.split("[*/]")).map(Double::parseDouble);
                    final Double finalResult = operands.zip(operations.prepend(Operation.MULTIPLY))
                            .foldLeft(1.0, (result, operationNextOperandTuple) -> {
                                final Double operand = operationNextOperandTuple._1;
                                switch (operationNextOperandTuple._2) {
                                    case DIVIDE:
                                        return result / operand;
                                    case MULTIPLY:
                                        return result * operand;
                                    default:
                                        throw new IllegalStateException();
                                }
                            });
                    return Option.of(finalResult);
                })
                .getOrElse(() -> Double.parseDouble(expression));
    }

    private String unwrapBrackets(String expr) {
        final int openBracketIndex = expr.indexOf("(");
        if (openBracketIndex == -1)
            return expr;

        int openCloseBalance = 1;
        for (int i = openBracketIndex + 1; i < expr.length(); i++) {
            if (expr.charAt(i) == ')')
                openCloseBalance--;
            if (expr.charAt(i) == '(')
                openCloseBalance++;

            if (openCloseBalance == 0) {
                final String expressionInBrackets = expr.substring(openBracketIndex + 1, i);
                return expr.substring(0, openBracketIndex) + evalNested(expressionInBrackets) +
                        expr.substring(i + 1);
            }
        }
        throw new IllegalStateException();
    }

    private List<Operation> findOperationsOrdered(String s, Operation... operations) {
        final var indexes = Stream.of(operations)
                .flatMap(operation ->
                        Stream.iterate(s.indexOf(operation.c),
                                i -> i != -1,
                                index -> s.indexOf(operation.c, index + 1))
                                .map(i -> new Tuple2<>(i, operation)));
        return List.ofAll(indexes).sortBy(tuple -> tuple._1).map(tuple -> tuple._2);
    }

    private Option<Double> tryEvalOperation(String expr, Operation operation) {
        return Option.of(expr)
                .flatMap(s -> {
                    switch (operation) {
                        case MINUS:
                            var minusPosition = expr.indexOf(operation.c);
                            while (minusPosition != -1) {
                                final String leftPart = expr.substring(0, minusPosition).trim();
                                if (!leftPart.isBlank()) {
                                    char lastChar = leftPart.charAt(leftPart.length() - 1);
                                    var ops = TreeSet.of(Operation.values()).map(operation1 -> operation1.c);
                                    if (!ops.contains(lastChar))
                                        return Option.of(minusPosition);
                                }
                                minusPosition = expr.indexOf(operation.c, minusPosition + 1);
                            }
                            return Option.none();
                        case PLUS:
                        case MULTIPLY:
                        case DIVIDE:
                            return Option.of(expr.indexOf(operation.c)).filter(i -> i != -1);
                    }
                    throw new IllegalArgumentException(String.format("No such operation %s", operation));
                })
                .map(pos -> {
                    var x = expr.substring(0, pos);
                    var y = expr.substring(pos + 1);
                    return operation.eval(evalNested(x), evalNested(y));
                });
    }

    private enum Operation {
        PLUS('+'), MINUS('-'), MULTIPLY('*'), DIVIDE('/');
        private final char c;

        Operation(char c) {
            this.c = c;
        }

        double eval(double x, double y) {
            switch (this) {
                case PLUS:
                    return x + y;
                case MINUS:
                    return x - y;
                case MULTIPLY:
                    return x * y;
                case DIVIDE:
                    return y == 0.0D ? Double.NaN : x / y;
            }
            throw new IllegalStateException();
        }
    }
}
