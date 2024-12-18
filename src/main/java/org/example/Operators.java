package org.example;

import net.objecthunter.exp4j.operator.Operator;

public class Operators {

    public static final Operator gteq = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
        @Override
        public double apply(double[] values) {
            return values[0] >= values[1] ? 1d : 0d;
        }
    };

    public static final Operator mteq = new Operator("<=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
        @Override
        public double apply(double[] values) {
            return values[0] <= values[1] ? 1d : 0d;
        }
    };

    public static final Operator gt = new Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
        @Override
        public double apply(double[] values) {
            return values[0] > values[1] ? 1d : 0d;
        }
    };

    public static final Operator mt = new Operator("<", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
        @Override
        public double apply(double[] values) {
            return values[0] < values[1] ? 1d : 0d;
        }
    };
}
