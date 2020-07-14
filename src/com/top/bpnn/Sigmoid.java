package com.top.bpnn;

import java.io.Serializable;

public class Sigmoid implements ActivationFunction, Serializable {
    @Override
    public double computeValue(double val) {
        return 1 / (1 + Math.exp(-val));
    }

    @Override
    public double computeDerivative(double val) {
        return computeValue(val) * (1 - computeValue(val));
    }
}
