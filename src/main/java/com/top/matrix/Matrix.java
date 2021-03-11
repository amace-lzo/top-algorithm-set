package com.top.matrix;

import com.top.constants.OrderEnum;

import java.io.Serializable;

public class Matrix implements Serializable {
    private double[][] matrix;
    //矩阵列数
    private int matrixColCount;
    //矩阵行数
    private int matrixRowCount;

    /**
     * 构造一个空矩阵
     */
    public Matrix() {
        this.matrix = null;
        this.matrixColCount = 0;
        this.matrixRowCount = 0;
    }

    /**
     * 构造一个matrix矩阵
     * @param matrix
     */
    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        this.matrixRowCount = matrix.length;
        this.matrixColCount = matrix[0].length;
    }

    /**
     * 构造一个rowCount行colCount列值为0的矩阵
     * @param rowCount
     * @param colCount
     */
    public Matrix(int rowCount,int colCount) {
        double[][] matrix = new double[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                matrix[i][j] = 0;
            }
        }
        this.matrix = matrix;
        this.matrixRowCount = rowCount;
        this.matrixColCount = colCount;
    }

    /**
     * 构造一个rowCount行colCount列值为val的矩阵
     * @param val
     * @param rowCount
     * @param colCount
     */
    public Matrix(double val,int rowCount,int colCount) {
        double[][] matrix = new double[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                matrix[i][j] = val;
            }
        }
        this.matrix = matrix;
        this.matrixRowCount = rowCount;
        this.matrixColCount = colCount;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
        this.matrixRowCount = matrix.length;
        this.matrixColCount = matrix[0].length;
    }

    public int getMatrixColCount() {
        return matrixColCount;
    }

    public int getMatrixRowCount() {
        return matrixRowCount;
    }

    /**
     * 获取矩阵指定位置的值
     *
     * @param x
     * @param y
     * @return
     */
    public double getValOfIdx(int x, int y) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (x > matrixRowCount - 1) {
            throw new IllegalArgumentException("索引x越界");
        }
        if (y > matrixColCount - 1) {
            throw new IllegalArgumentException("索引y越界");
        }
        return matrix[x][y];
    }

    /**
     * 获取矩阵指定行
     *
     * @param x
     * @return
     */
    public Matrix getRowOfIdx(int x) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (x > matrixRowCount - 1) {
            throw new IllegalArgumentException("索引x越界");
        }
        double[][] result = new double[1][matrixColCount];
        result[0] = matrix[x];
        return new Matrix(result);
    }

    /**
     * 获取矩阵指定列
     *
     * @param y
     * @return
     */
    public Matrix getColOfIdx(int y) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (y > matrixColCount - 1) {
            throw new IllegalArgumentException("索引y越界");
        }
        double[][] result = new double[matrixRowCount][1];
        for (int i = 0; i < matrixRowCount; i++) {
            result[i][0] = matrix[i][y];
        }
        return new Matrix(result);
    }

    /**
     * 设置矩阵中x,y位置元素的值
     * @param x
     * @param y
     * @param val
     */
    public void setValue(int x, int y, double val) {
        if (x > this.matrixRowCount - 1) {
            throw new IllegalArgumentException("行索引越界");
        }
        if (y > this.matrixColCount - 1) {
            throw new IllegalArgumentException("列索引越界");
        }
        this.matrix[x][y] = val;
    }

    /**
     * 矩阵乘矩阵
     *
     * @param a
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix multiple(Matrix a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (a.getMatrix() == null || a.getMatrixRowCount() == 0 || a.getMatrixColCount() == 0) {
            throw new IllegalArgumentException("参数矩阵为空");
        }
        if (matrixColCount != a.getMatrixRowCount()) {
            throw new IllegalArgumentException("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowCount][a.getMatrixColCount()];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < a.getMatrixColCount(); j++) {
                for (int k = 0; k < matrixColCount; k++) {
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
    public Matrix multiple(double a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
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
    public Matrix pointMultiple(Matrix a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (a.getMatrix() == null || a.getMatrixRowCount() == 0 || a.getMatrixColCount() == 0) {
            throw new IllegalArgumentException("参数矩阵为空");
        }
        if (matrixRowCount != a.getMatrixRowCount() && matrixColCount != a.getMatrixColCount()) {
            throw new IllegalArgumentException("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
                result[i][j] = matrix[i][j] * a.getMatrix()[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵除一个数字
     * @param a
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix divide(double a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
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
    public Matrix plus(Matrix a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (a.getMatrix() == null || a.getMatrixRowCount() == 0 || a.getMatrixColCount() == 0) {
            throw new IllegalArgumentException("参数矩阵为空");
        }
        if (matrixRowCount != a.getMatrixRowCount() && matrixColCount != a.getMatrixColCount()) {
            throw new IllegalArgumentException("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
                result[i][j] = matrix[i][j] + a.getMatrix()[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵加一个数字
     * @param a
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix plus(double a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
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
    public Matrix subtract(Matrix a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (a.getMatrix() == null || a.getMatrixRowCount() == 0 || a.getMatrixColCount() == 0) {
            throw new IllegalArgumentException("参数矩阵为空");
        }
        if (matrixRowCount != a.getMatrixRowCount() && matrixColCount != a.getMatrixColCount()) {
            throw new IllegalArgumentException("矩阵纬度不同，不可计算");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
                result[i][j] = matrix[i][j] - a.getMatrix()[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵减一个数字
     * @param a
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix subtract(double a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
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
    public Matrix sumRow() throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixRowCount][1];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
                result[i][0] += matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵列求和
     *
     * @return
     */
    public Matrix sumCol() throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[1][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
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
    public double sumAll() throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double result = 0;
        for (double[] doubles : matrix) {
            for (int j = 0; j < matrixColCount; j++) {
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
    public Matrix square() throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
                result[i][j] = matrix[i][j] * matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵所有元素求N次方
     *
     * @return
     */
    public Matrix pow(double n) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixRowCount][matrixColCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
                result[i][j] = Math.pow(matrix[i][j],n);
            }
        }
        return new Matrix(result);
    }

    /**
     * 矩阵转置
     *
     * @return
     */
    public Matrix transpose() throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        double[][] result = new double[matrixColCount][matrixRowCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixColCount; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    /**
     * 截取矩阵
     * @param startRowIndex 开始行索引
     * @param rowCount   截取行数
     * @param startColIndex 开始列索引
     * @param colCount   截取列数
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix subMatrix(int startRowIndex,int rowCount,int startColIndex,int colCount) throws IllegalArgumentException {
        if (startRowIndex + rowCount > matrixRowCount) {
            throw new IllegalArgumentException("行索引越界");
        }
        if (startColIndex + colCount> matrixColCount) {
            throw new IllegalArgumentException("列索引越界");
        }
        double[][] result = new double[rowCount][colCount];
        for (int i = startRowIndex; i < startRowIndex + rowCount; i++) {
            if (startColIndex + colCount - startColIndex >= 0)
                System.arraycopy(matrix[i], startColIndex, result[i - startRowIndex], 0, colCount);
        }
        return new Matrix(result);
    }

    /**
     * 矩阵合并
     * @param direction 合并方向，1为横向，2为竖向
     * @param a
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix splice(int direction, Matrix a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if (a.getMatrix() == null || a.getMatrixRowCount() == 0 || a.getMatrixColCount() == 0) {
            throw new IllegalArgumentException("参数矩阵为空");
        }
        if(direction == 1){
            //横向拼接
            if (matrixRowCount != a.getMatrixRowCount()) {
                throw new IllegalArgumentException("矩阵行数不一致，无法拼接");
            }
            double[][] result = new double[matrixRowCount][matrixColCount + a.getMatrixColCount()];
            for (int i = 0; i < matrixRowCount; i++) {
                System.arraycopy(matrix[i],0,result[i],0,matrixColCount);
                System.arraycopy(a.getMatrix()[i],0,result[i],matrixColCount,a.getMatrixColCount());
            }
            return new Matrix(result);
        }else if(direction == 2){
            //纵向拼接
            if (matrixColCount != a.getMatrixColCount()) {
                throw new IllegalArgumentException("矩阵列数不一致，无法拼接");
            }
            double[][] result = new double[matrixRowCount + a.getMatrixRowCount()][matrixColCount];
            for (int i = 0; i < matrixRowCount; i++) {
                result[i] = matrix[i];
            }
            for (int i = 0; i < a.getMatrixRowCount(); i++) {
                result[matrixRowCount + i] = a.getMatrix()[i];
            }
            return new Matrix(result);
        }else{
            throw new IllegalArgumentException("方向参数有误");
        }
    }
    /**
     * 扩展矩阵
     * @param direction 扩展方向，1为横向，2为竖向
     * @param a
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix extend(int direction , int a) throws IllegalArgumentException {
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if(direction == 1){
            //横向复制
            double[][] result = new double[matrixRowCount][matrixColCount*a];
            for (int i = 0; i < matrixRowCount; i++) {
                for (int j = 0; j < a; j++) {
                    System.arraycopy(matrix[i],0,result[i],j*matrixColCount,matrixColCount);
                }
            }
            return new Matrix(result);
        }else if(direction == 2){
            //纵向复制
            double[][] result = new double[matrixRowCount*a][matrixColCount];
            for (int i = 0; i < matrixRowCount*a; i++) {
                result[i] = matrix[i%matrixRowCount];
            }
            return new Matrix(result);
        }else{
            throw new IllegalArgumentException("方向参数有误");
        }
    }
    /**
     * 获取每列的平均值
     * @return
     * @throws IllegalArgumentException
     */
    public Matrix getColAvg() throws IllegalArgumentException {
        Matrix tmp = this.sumCol();
        return tmp.divide(matrixRowCount);
    }

    /**
     * 矩阵行排序
     * @param index 根据第几列的数进行行排序
     * @param order 排序顺序，升序或降序
     * @return
     * @throws IllegalArgumentException
     */
    public void sort(int index,OrderEnum order) throws IllegalArgumentException{
        if (matrix == null || matrixRowCount == 0 || matrixColCount == 0) {
            throw new IllegalArgumentException("矩阵为空");
        }
        if(index >= matrixColCount){
            throw new IllegalArgumentException("排序索引index越界");
        }
        sort(index,order,0,this.matrixRowCount - 1);
    }

    /**
     * 判断是否是方阵
     * 行列数相等，并且不等于0
     * @return
     */
    public boolean isSquareMatrix(){
        return matrixColCount == matrixRowCount && matrixColCount != 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\r\n");
        for (int i = 0; i < matrixRowCount; i++) {
            stringBuilder.append("# ");
            for (int j = 0; j < matrixColCount; j++) {
                stringBuilder.append(matrix[i][j]).append("\t ");
            }
            stringBuilder.append("#\r\n");
        }
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }

    private void sort(int index,OrderEnum order,int start,int end){
        if(start >= end){
            return;
        }
        int tmp = partition(index,order,start,end);
        sort(index,order, start, tmp - 1);
        sort(index,order, tmp + 1, end);
    }

    private int partition(int index,OrderEnum order,int start,int end){
        int l = start + 1,r = end;
        double v = matrix[start][index];
        switch (order){
            case ASC:
                while(true){
                    while(matrix[r][index] >= v && r > start){
                        r--;
                    }
                    while(matrix[l][index] <= v && l < end){
                        l++;
                    }
                    if(l >= r){
                        break;
                    }
                    double[] tmp = matrix[r];
                    matrix[r] = matrix[l];
                    matrix[l] = tmp;
                }
                break;
            case DESC:
                while(true){
                    while(matrix[r][index] <= v && r > start){
                        r--;
                    }
                    while(matrix[l][index] >= v && l < end){
                        l++;
                    }
                    if(l >= r){
                        break;
                    }
                    double[] tmp = matrix[r];
                    matrix[r] = matrix[l];
                    matrix[l] = tmp;
                }
                break;
        }
        double[] tmp = matrix[r];
        matrix[r] = matrix[start];
        matrix[start] = tmp;
        return r;
    }
}
