package org.example;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.Random;

public class MonteCarloIntegration {

    public static double MCResult(String funcExpression, String regionExpression, double[][] bounds, int N) {
        int dimensions = bounds.length;
        double volume = 1.0;

        for (double[] bound : bounds) {
            volume *= (bound[1] - bound[0]);
        }

        String[] regions = splitRegions(regionExpression);
        int ri = 0;
        Expression funcExpr = new ExpressionBuilder(funcExpression).variables(generateVariables(dimensions)).build();
        Expression[] regionsEx = new Expression[regions.length];
        for (String region : regions) {
            Expression regionEx = new ExpressionBuilder(region)
                    .variables(generateVariables(dimensions))
                    .operator(Operators.gt)
                    .operator(Operators.mt)
                    .operator(Operators.mteq)
                    .operator(Operators.gteq)
                    .build();
            regionsEx[ri] = regionEx;
            ri++;
        }

        double sum = countIntegral(funcExpr, regionsEx, N, bounds, ri);
        return volume * sum / N;
    }

    private static boolean checkRegions(Expression[] regionsEx, int ri) {
        for (int i = 0; i < ri; i++) {
            if (regionsEx[i].evaluate() <= 0) {
                return false;
            }
        }
        return true;
    }

    private static String[] generateVariables(int dimensions) {
        String[] vars = new String[dimensions];
        for (int i = 0; i < dimensions; i++) {
            vars[i] = "x" + i;
        }
        return vars;
    }

    private static String[] splitRegions(String regionExpression) {
        String nd = "&";
        int count = regionExpression.length() - regionExpression.replace(nd, "").length();

        String[] regions = new String[count + 1];
        if (count != 0) {
            regions = regionExpression.split(nd);
        } else {
            regions[0] = regionExpression;
        }
        return regions;
    }

    private static double countIntegral(Expression funcExpr, Expression[] regionsEx, int N, double[][] bounds, int ri) {
        double sum = 0.0;
        int insideCount = 0;
        Random rand = new Random();
        int dimensions = bounds.length;

        for (int i = 0; i < N; i++) {
            double[] randomPoint = new double[dimensions];
            for (int d = 0; d < dimensions; d++) {
                randomPoint[d] = bounds[d][0] + (bounds[d][1] - bounds[d][0]) * rand.nextDouble();
                funcExpr.setVariable("x" + d, randomPoint[d]);
                for (int j = 0; j < ri; j++) {
                    regionsEx[j].setVariable("x" + d, randomPoint[d]);
                }
            }
            if (checkRegions(regionsEx, ri)) {
                sum += funcExpr.evaluate();
                insideCount++;
            }
        }

        if (insideCount == 0) {
            throw new IllegalArgumentException("Все точки вне области. Проверьте границы или область интегрирования.");
        }
        return sum;
    }
}
