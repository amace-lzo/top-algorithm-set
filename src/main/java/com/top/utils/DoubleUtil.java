package com.top.utils;

/**
 * @program: top-algorithm-set
 * @description: DoubleTool
 * @author: Mr.Zhao
 * @create: 2020-11-12 21:54
 **/
public class DoubleUtil {

    private static final Double MAX_ERROR = 0.0001;

    public static boolean equals(Double a, Double b) {
        return Math.abs(a - b)< MAX_ERROR;
    }

    public static boolean equals(Double a, Double b,Double maxError) {
        return Math.abs(a - b)< maxError;
    }
}
