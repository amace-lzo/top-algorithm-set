package com.top.bpnn;

import com.top.matrix.Matrix;

import java.io.Serializable;

public class BPModel implements Serializable {
    //BP神经网络权值与阈值
    private Matrix weightIJ;
    private Matrix b1;
    private Matrix weightJP;
    private Matrix b2;
    /*用于反归一化*/
    private Matrix inputMax;
    private Matrix inputMin;
    private Matrix outputMax;
    private Matrix outputMin;
    /*BP神经网络训练参数*/
    private BPParameter bpParameter;
    /*BP神经网络训练情况*/
    private double error;
    private int times;

    public Matrix getWeightIJ() {
        return weightIJ;
    }

    public void setWeightIJ(Matrix weightIJ) {
        this.weightIJ = weightIJ;
    }

    public Matrix getB1() {
        return b1;
    }

    public void setB1(Matrix b1) {
        this.b1 = b1;
    }

    public Matrix getWeightJP() {
        return weightJP;
    }

    public void setWeightJP(Matrix weightJP) {
        this.weightJP = weightJP;
    }

    public Matrix getB2() {
        return b2;
    }

    public void setB2(Matrix b2) {
        this.b2 = b2;
    }

    public Matrix getInputMax() {
        return inputMax;
    }

    public void setInputMax(Matrix inputMax) {
        this.inputMax = inputMax;
    }

    public Matrix getInputMin() {
        return inputMin;
    }

    public void setInputMin(Matrix inputMin) {
        this.inputMin = inputMin;
    }

    public Matrix getOutputMax() {
        return outputMax;
    }

    public void setOutputMax(Matrix outputMax) {
        this.outputMax = outputMax;
    }

    public Matrix getOutputMin() {
        return outputMin;
    }

    public void setOutputMin(Matrix outputMin) {
        this.outputMin = outputMin;
    }

    public BPParameter getBpParameter() {
        return bpParameter;
    }

    public void setBpParameter(BPParameter bpParameter) {
        this.bpParameter = bpParameter;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
