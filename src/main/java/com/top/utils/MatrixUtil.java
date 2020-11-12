package com.top.utils;

import Jama.EigenvalueDecomposition;
import com.top.matrix.Matrix;

import java.util.*;

public class MatrixUtil {
    /**
     * 创建一个单位矩阵
     * @param matrixRowCount 单位矩阵的纬度
     * @return
     */
    public static Matrix eye(int matrixRowCount){
        double[][] result = new double[matrixRowCount][matrixRowCount];
        for (int i = 0; i < matrixRowCount; i++) {
            for (int j = 0; j < matrixRowCount; j++) {
                if(i == j){
                    result[i][j] = 1;
                }else{
                    result[i][j] = 0;
                }
            }
        }
        return new Matrix(result);
    }

    /**
     * 求矩阵的逆
     * 原理:AE=EA^-1
     * @param a
     * @return
     * @throws Exception
     */
    public static Matrix inv(Matrix a) throws Exception {
        if (!invable(a)) {
            throw new Exception("矩阵不可逆");
        }
        // [a|E]
        Matrix b = a.splice(1, eye(a.getMatrixRowCount()));
        double[][] data = b.getMatrix();
        int rowCount = b.getMatrixRowCount();
        int colCount = b.getMatrixColCount();
        //此处应用a的列数，为简化，直接用b的行数
        for (int j = 0; j < rowCount; j++) {
            //若遇到0则交换两行
            int notZeroRow = -2;
            if(data[j][j] == 0){
                notZeroRow = -1;
                for (int l = j; l < rowCount; l++) {
                    if (data[l][j] != 0) {
                        notZeroRow = l;
                        break;
                    }
                }
            }
            if (notZeroRow == -1) {
                throw new Exception("矩阵不可逆");
            }else if(notZeroRow != -2){
                //交换j与notZeroRow两行
                double[] tmp = data[j];
                data[j] = data[notZeroRow];
                data[notZeroRow] = tmp;
            }
            //将第data[j][j]化为1
            if (data[j][j] != 1) {
                double multiple = data[j][j];
                for (int colIdx = j; colIdx < colCount; colIdx++) {
                    data[j][colIdx] /= multiple;
                }
            }
            //行与行相减
            for (int i = 0; i < rowCount; i++) {
                if (i != j) {
                    double multiple = data[i][j] / data[j][j];
                    //遍历行中的列
                    for (int k = j; k < colCount; k++) {
                        data[i][k] = data[i][k] - multiple * data[j][k];
                    }
                }
            }
        }
        Matrix result = new Matrix(data);
        return result.subMatrix(0, rowCount, rowCount, rowCount);
    }

    /**
     * 求矩阵的伴随矩阵
     * 原理:A*=|A|A^-1
     * @param a
     * @return
     * @throws Exception
     */
    public static Matrix adj(Matrix a) throws Exception {
        return inv(a).multiple(det(a));
    }

    /**
     * 矩阵转成上三角矩阵
     * @param a
     * @return
     * @throws Exception
     */
    public static Matrix getTopTriangle(Matrix a) throws Exception {
        if (!a.isSquareMatrix()) {
            throw new Exception("不是方阵无法进行计算");
        }
        int matrixHeight = a.getMatrixRowCount();
        double[][] result = a.getMatrix();
        //遍历列
        for (int j = 0; j < matrixHeight; j++) {
            //遍历行
            for (int i = j+1; i < matrixHeight; i++) {
                //若遇到0则交换两行
                int notZeroRow = -2;
                if(result[j][j] == 0){
                    notZeroRow = -1;
                    for (int l = i; l < matrixHeight; l++) {
                        if (result[l][j] != 0) {
                            notZeroRow = l;
                            break;
                        }
                    }
                }
                if (notZeroRow == -1) {
                    throw new Exception("矩阵不可逆");
                }else if(notZeroRow != -2){
                    //交换j与notZeroRow两行
                    double[] tmp = result[j];
                    result[j] = result[notZeroRow];
                    result[notZeroRow] = tmp;
                }

                double multiple = result[i][j]/result[j][j];
                //遍历行中的列
                for (int k = j; k < matrixHeight; k++) {
                    result[i][k] = result[i][k] - multiple * result[j][k];
                }
            }
        }
        return new Matrix(result);
    }

    /**
     * 计算矩阵的行列式
     * @param a
     * @return
     * @throws Exception
     */
    public static double det(Matrix a) throws Exception {
        //将矩阵转成上三角矩阵
        Matrix b = MatrixUtil.getTopTriangle(a);
        double result = 1;
        //计算矩阵行列式
        for (int i = 0; i < b.getMatrixRowCount(); i++) {
            result *= b.getValOfIdx(i, i);
        }
        return result;
    }
    /**
     * 获取协方差矩阵
     * @param a
     * @return
     * @throws Exception
     */
    public static Matrix cov(Matrix a) throws Exception {
        if (a.getMatrix() == null) {
            throw new Exception("矩阵为空");
        }
        Matrix avg = a.getColAvg().extend(2, a.getMatrixRowCount());
        Matrix tmp = a.subtract(avg);
        return tmp.transpose().multiple(tmp).multiple(1/((double) a.getMatrixRowCount() -1));
    }

    /**
     * 判断矩阵是否可逆
     * 如果可转为上三角矩阵则可逆
     * @param a
     * @return
     */
    public static boolean invable(Matrix a) {
        try {
            getTopTriangle(a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取矩阵的特征值矩阵，调用Jama中的getV方法
     * @param a
     * @return
     */
    public static Matrix getV(Matrix a) {
        EigenvalueDecomposition eig = new EigenvalueDecomposition(new Jama.Matrix(a.getMatrix()));
        return new Matrix(eig.getV().getArray());
    }

    /**
     * 取特征值实部
     * @param a
     * @return
     */
    public double[] getRealEigenvalues(Matrix a){
        EigenvalueDecomposition eig = new EigenvalueDecomposition(new Jama.Matrix(a.getMatrix()));
        return eig.getRealEigenvalues();
    }

    /**
     * 取特征值虚部
     * @param a
     * @return
     */
    public double[] getImagEigenvalues(Matrix a){
        EigenvalueDecomposition eig = new EigenvalueDecomposition(new Jama.Matrix(a.getMatrix()));
        return eig.getImagEigenvalues();
    }

    /**
     * 取块对角特征值矩阵
     * @param a
     * @return
     */
    public static Matrix getD(Matrix a) {
        EigenvalueDecomposition eig = new EigenvalueDecomposition(new Jama.Matrix(a.getMatrix()));
        return new Matrix(eig.getD().getArray());
    }

    /**
     * 数据归一化
     * @param a 要归一化的数据
     * @param normalizationMin  要归一化的区间下限
     * @param normalizationMax  要归一化的区间上限
     * @return
     */
    public static Map<String, Object> normalize(Matrix a, double normalizationMin, double normalizationMax) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        double[][] maxArr = new double[1][a.getMatrixColCount()];
        double[][] minArr = new double[1][a.getMatrixColCount()];
        double[][] res = new double[a.getMatrixRowCount()][a.getMatrixColCount()];
        for (int i = 0; i < a.getMatrixColCount(); i++) {
            List tmp = new ArrayList();
            for (int j = 0; j < a.getMatrixRowCount(); j++) {
                tmp.add(a.getValOfIdx(j,i));
            }
            double max = (double) Collections.max(tmp);
            double min = (double) Collections.min(tmp);
            //数据归一化(注:若max与min均为0则不需要归一化)
            if (max != 0 || min != 0) {
                for (int j = 0; j < a.getMatrixRowCount(); j++) {
                    res[j][i] = normalizationMin + (a.getValOfIdx(j,i) - min) / (max - min) * (normalizationMax - normalizationMin);
                }
            }
            maxArr[0][i] = max;
            minArr[0][i] = min;
        }
        result.put("max", new Matrix(maxArr));
        result.put("min", new Matrix(minArr));
        result.put("res", new Matrix(res));
        return result;
    }

    /**
     * 反归一化
     * @param a 要反归一化的数据
     * @param normalizationMin 要反归一化的区间下限
     * @param normalizationMax 要反归一化的区间上限
     * @param dataMax   数据最大值
     * @param dataMin   数据最小值
     * @return
     */
    public static Matrix inverseNormalize(Matrix a, double normalizationMax, double normalizationMin , Matrix dataMax,Matrix dataMin){
        double[][] res = new double[a.getMatrixRowCount()][a.getMatrixColCount()];
        for (int i = 0; i < a.getMatrixColCount(); i++) {
            //数据反归一化
            if (dataMin.getValOfIdx(0,i) != 0 || dataMax.getValOfIdx(0,i) != 0) {
                for (int j = 0; j < a.getMatrixRowCount(); j++) {
                    res[j][i] = dataMin.getValOfIdx(0,i) + (dataMax.getValOfIdx(0,i) - dataMin.getValOfIdx(0,i)) * (a.getValOfIdx(j,i) - normalizationMin) / (normalizationMax - normalizationMin);
                }
            }
        }
        return new Matrix(res);
    }
}
