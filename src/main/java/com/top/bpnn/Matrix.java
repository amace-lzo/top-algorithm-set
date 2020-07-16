package com.top.bpnn;

import java.io.Serializable;

public class Matrix implements Serializable {
    private double[][] matrix;
    private int matrixColNums;
    private int matrixRowNums;

    /**
     * 构造一个空矩阵
     */
    public Matrix() {
        this.matrix = null;
        this.matrixColNums = 0;
        this.matrixRowNums = 0;
    }

    /**
     * 构造一个matrix矩阵
     * @param matrix
     */
    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        this.matrixRowNums = matrix.length;
        this.matrixColNums = matrix[0].length;
    }

    /**
     * 构造一个rowNums行colNums列值为0的矩阵
     * @param rowNums
     * @param colNums
     */
    public Matrix(int rowNums,int colNums) {
        double[][] matrix = new double[rowNums][colNums];
        for (int i = 0; i < rowNums; i++) {
            for (int j = 0; j < colNums; j++) {
                matrix[i][j] = 0;
            }
        }
        this.matrix = matrix;
        this.matrixRowNums = rowNums;
        this.matrixColNums = colNums;
    }

    /**
     * 构造一个rowNums行colNums列值为val的矩阵
     * @param val
     * @param rowNums
     * @param colNums
     */
    public Matrix(double val,int rowNums,int colNums) {
        double[][] matrix = new double[rowNums][colNums];
        for (int i = 0; i < rowNums; i++) {
            for (int j = 0; j < colNums; j++) {
                matrix[i][j] = val;
            }
        }
        this.matrix = matrix;
        this.matrixRowNums = rowNums;
        this.matrixColNums = colNums;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
        this.matrixRowNums = matrix.length;
        this.matrixColNums = matrix[0].length;
    }

    public int getMatrixColNums() {
        return matrixColNums;
    }

    public int getMatrixRowNums() {
        return matrixRowNums;
    }

    /**
     * 获取矩阵指定位置的值
     *
     * @param x
     * @param y
     * @return
     */
    public double getValOfIdx(int x, int y) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (x > matrixRowNums - 1) {
            throw new Exception("索引x越界");
        }
        if (y > matrixColNums - 1) {
            throw new Exception("索引y越界");
        }
        return matrix[x][y];
    }

    /**
     * 获取矩阵指定行
     *
     * @param x
     * @return
     */
    public Matrix getRowOfIdx(int x) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (x > matrixRowNums - 1) {
            throw new Exception("索引x越界");
        }
        double[][] result = new double[1][matrixColNums];
        result[0] = matrix[x];
        return new Matrix(result);
    }

    /**
     * 获取矩阵指定列
     *
     * @param y
     * @return
     */
    public Matrix getColOfIdx(int y) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (y > matrixColNums - 1) {
            throw new Exception("索引y越界");
        }
        double[][] result = new double[matrixRowNums][1];
        for (int i = 0; i < matrixRowNums; i++) {
            result[i][1] = matrix[i][y];
        }
        return new Matrix(result);
    }

    /**
     * 矩阵乘矩阵
     *
     * @param a
     * @return
     * @throws Exception
     */
    public Matrix multiple(Matrix a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (a.getMatrix() == null) {
            throw new Exception("参数矩阵为空");
        }
        if (matrixColNums != a.getMatrixRowNums()) {
            throw new Exception("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowNums][a.getMatrixColNums()];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < a.getMatrixColNums(); j++) {
                for (int k = 0; k < matrixColNums; k++) {
                    result[i][j] = result[i][j] + matrix[i][k] * a.getMatrix()[k][j];
                }
            }
        }
        return new Matrix(result);
    }

    /**
     * 二维数组乘一个数字
     *
     * @param a
     * @return
     */
    public Matrix multiple(double a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] * a;
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵点乘
     *
     * @param a
     * @return
     */
    public Matrix pointMultiple(Matrix a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (a.getMatrix() == null) {
            throw new Exception("参数矩阵为空");
        }
        if (matrixRowNums != a.getMatrixRowNums() && matrixColNums != a.getMatrixColNums()) {
            throw new Exception("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] * a.getMatrix()[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵加法
     *
     * @param a
     * @return
     */
    public Matrix plus(Matrix a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (a.getMatrix() == null) {
            throw new Exception("参数矩阵为空");
        }
        if (matrixRowNums != a.getMatrixRowNums() && matrixColNums != a.getMatrixColNums()) {
            throw new Exception("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] + a.getMatrix()[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵减法
     *
     * @param a
     * @return
     */
    public Matrix subtract(Matrix a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (a.getMatrix() == null) {
            throw new Exception("参数矩阵为空");
        }
        if (matrixRowNums != a.getMatrixRowNums() && matrixColNums != a.getMatrixColNums()) {
            throw new Exception("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] - a.getMatrix()[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵行求和
     *
     * @return
     */
    public Matrix sumRow() throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[matrixRowNums][1];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][1] += matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵列求和
     *
     * @return
     */
    public Matrix sumCol() throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[1][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[0][i] += matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵所有元素求和
     *
     * @return
     */
    public double sumAll() throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double result = 0;
        for (double[] doubles : matrix) {
            for (int j = 0; j < matrixColNums; j++) {
                result += doubles[j];
            }
        }
        return result;
    }

    /**
     * 矩阵所有元素求平方
     *
     * @return
     */
    public Matrix square() throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] * matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵转置
     *
     * @return
     */
    public Matrix transpose() throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[matrixColNums][matrixRowNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\r\n");
        for (int i = 0; i < matrixRowNums; i++) {
            stringBuilder.append("# ");
            for (int j = 0; j < matrixColNums; j++) {
                stringBuilder.append(matrix[i][j]).append("\t ");
            }
            stringBuilder.append("#\r\n");
        }
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }
}
