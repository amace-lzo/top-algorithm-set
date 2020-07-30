package com.top.utils;

import com.top.matrix.Matrix;

public class MatrixUtil {

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

                double tmp = result[i][j]/result[j][j];
                //遍历行中的列
                for (int k = j; k < matrixHeight; k++) {
                    result[i][k] = result[i][k] - tmp * result[j][k];
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
}
