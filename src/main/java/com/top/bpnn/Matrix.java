package com.top.bpnn;

import java.io.Serializable;
import java.util.Arrays;

public class Matrix implements Serializable {
    private double[][] matrix;
    //矩阵列数
    private int matrixColNums;
    //矩阵行数
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
     * 矩阵乘一个数字
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
     * 矩阵除一个数字
     * @param a
     * @return
     * @throws Exception
     */
    public Matrix divide(double a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] / a;
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
     * 矩阵加一个数字
     * @param a
     * @return
     * @throws Exception
     */
    public Matrix plus(double a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] + a;
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
     * 矩阵减一个数字
     * @param a
     * @return
     * @throws Exception
     */
    public Matrix subtract(double a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        double[][] result = new double[matrixRowNums][matrixColNums];
        for (int i = 0; i < matrixRowNums; i++) {
            for (int j = 0; j < matrixColNums; j++) {
                result[i][j] = matrix[i][j] - a;
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
                result[0][j] += matrix[i][j];
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

    /**
     * 截取矩阵
     * @param startRowIndex 开始行索引
     * @param rowNums   截取行数
     * @param startColIndex 开始列索引
     * @param colNums   截取列数
     * @return
     * @throws Exception
     */
    public Matrix subMatrix(int startRowIndex,int rowNums,int startColIndex,int colNums) throws Exception {
        if (startRowIndex + rowNums > matrixRowNums) {
            throw new Exception("行索引越界");
        }
        if (startColIndex + colNums> matrixColNums) {
            throw new Exception("列索引越界");
        }
        double[][] result = new double[rowNums][colNums];
        for (int i = startRowIndex; i < startRowIndex + rowNums; i++) {
            if (startColIndex + colNums - startColIndex >= 0)
                System.arraycopy(matrix[i], startColIndex, result[i - startRowIndex], 0, colNums);
        }
        return new Matrix(result);
    }

    /**
     * 矩阵合并
     * @param direction 合并方向，1为横向，2为竖向
     * @param a
     * @return
     * @throws Exception
     */
    public Matrix splice(int direction, Matrix a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if (a.getMatrix() == null) {
            throw new Exception("参数矩阵为空");
        }
        if(direction == 1){
            //横向拼接
            if (matrixRowNums != a.getMatrixRowNums()) {
                throw new Exception("矩阵行数不一致，无法拼接");
            }
            double[][] result = new double[matrixRowNums][matrixColNums + a.getMatrixColNums()];
            for (int i = 0; i < matrixRowNums; i++) {
                System.arraycopy(matrix[i],0,result[i],0,matrixColNums);
                System.arraycopy(a.getMatrix()[i],0,result[i],matrixColNums,a.getMatrixColNums());
            }
            return new Matrix(result);
        }else if(direction == 2){
            //纵向拼接
            if (matrixColNums != a.getMatrixColNums()) {
                throw new Exception("矩阵列数不一致，无法拼接");
            }
            double[][] result = new double[matrixRowNums + a.getMatrixRowNums()][matrixColNums];
            for (int i = 0; i < matrixRowNums; i++) {
                result[i] = matrix[i];
            }
            for (int i = 0; i < a.getMatrixRowNums(); i++) {
                result[matrixRowNums + i] = a.getMatrix()[i];
            }
            return new Matrix(result);
        }else{
            throw new Exception("方向参数有误");
        }
    }
    /**
     * 扩展矩阵
     * @param direction 扩展方向，1为横向，2为竖向
     * @param a
     * @return
     * @throws Exception
     */
    public Matrix extend(int direction , int a) throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        if(direction == 1){
            //横向复制
            double[][] result = new double[matrixRowNums][matrixColNums*a];
            for (int i = 0; i < matrixRowNums; i++) {
                for (int j = 0; j < a; j++) {
                    System.arraycopy(matrix[i],0,result[i],j*matrixColNums,matrixColNums);
                }
            }
            return new Matrix(result);
        }else if(direction == 2){
            //纵向复制
            double[][] result = new double[matrixRowNums*a][matrixColNums];
            for (int i = 0; i < matrixRowNums*a; i++) {
                result[i] = matrix[i%matrixRowNums];
            }
            return new Matrix(result);
        }else{
            throw new Exception("方向参数有误");
        }
    }
    /**
     * 获取每列的平均值
     * @return
     * @throws Exception
     */
    public Matrix getColAvg() throws Exception {
        Matrix tmp = this.sumCol();
        return tmp.divide(matrixRowNums);
    }

    /**
     * 获取协方差矩阵
     * @return
     * @throws Exception
     */
    public Matrix getCovariance() throws Exception {
        if (matrix == null) {
            throw new Exception("矩阵为空");
        }
        Matrix tmp = new Matrix(matrix);
        Matrix avg = this.getColAvg().extend(2, matrixRowNums);
        Matrix tmp2 = tmp.subtract(avg);
        return tmp2.transpose().multiple(tmp2).multiple(1/((double) matrixRowNums -1));
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
