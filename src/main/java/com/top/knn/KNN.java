package com.top.knn;

import com.top.constants.OrderEnum;
import com.top.matrix.Matrix;
import com.top.utils.MatrixUtil;

import java.util.*;


/**
 * @program: top-algorithm-set
 * @description: KNN k-临近算法进行分类
 * @author: Mr.Zhao
 * @create: 2020-10-13 22:03
 **/
public class KNN {
    public static Matrix classify(Matrix input, Matrix dataSet, Matrix labels, int k) throws Exception {
        if (dataSet.getMatrixRowCount() != labels.getMatrixRowCount()) {
            throw new IllegalArgumentException("矩阵训练集与标签维度不一致");
        }
        if (input.getMatrixColCount() != dataSet.getMatrixColCount()) {
            throw new IllegalArgumentException("待分类矩阵列数与训练集列数不一致");
        }
        if (dataSet.getMatrixRowCount() < k) {
            throw new IllegalArgumentException("训练集样本数小于k");
        }
        // 归一化
        int trainCount = dataSet.getMatrixRowCount();
        int testCount = input.getMatrixRowCount();
        Matrix trainAndTest = dataSet.splice(2, input);
        Map<String, Object> normalize = MatrixUtil.normalize(trainAndTest, 0, 1);
        trainAndTest = (Matrix) normalize.get("res");
        dataSet = trainAndTest.subMatrix(0, trainCount, 0, trainAndTest.getMatrixColCount());
        input = trainAndTest.subMatrix(0, testCount, 0, trainAndTest.getMatrixColCount());

        // 获取标签信息
        List<Double> labelList = new ArrayList<>();
        for (int i = 0; i < labels.getMatrixRowCount(); i++) {
            if (!labelList.contains(labels.getValOfIdx(i, 0))) {
                labelList.add(labels.getValOfIdx(i, 0));
            }
        }

        Matrix result = new Matrix(new double[input.getMatrixRowCount()][1]);
        for (int i = 0; i < input.getMatrixRowCount(); i++) {
            // 求向量间的欧式距离
            Matrix var1 = input.getRowOfIdx(i).extend(2, dataSet.getMatrixRowCount());
            Matrix var2 = dataSet.subtract(var1);
            Matrix var3 = var2.square();
            Matrix var4 = var3.sumRow();
            Matrix var5 = var4.pow(0.5);
            // 距离矩阵合并上labels矩阵
            Matrix var6 = var5.splice(1, labels);
            // 将计算出的距离矩阵按照距离升序排序
            var6.sort(0, OrderEnum.ASC);
            // 遍历最近的k个变量
            Map<Double, Integer> map = new HashMap<>();
            for (int j = 0; j < k; j++) {
                // 遍历标签种类数
                for (Double label : labelList) {
                    if (var6.getValOfIdx(j, 1) == label) {
                        map.put(label, map.getOrDefault(label, 0) + 1);
                    }
                }
            }
            result.setValue(i, 0, getKeyOfMaxValue(map));
        }
        return result;
    }

    /**
     * 取map中值最大的key
     *
     * @param map
     * @return
     */
    private static Double getKeyOfMaxValue(Map<Double, Integer> map) {
        if (map == null)
            return null;
        Double keyOfMaxValue = 0.0;
        Integer maxValue = 0;
        for (Double key : map.keySet()) {
            if (map.get(key) > maxValue) {
                keyOfMaxValue = key;
                maxValue = map.get(key);
            }
        }
        return keyOfMaxValue;
    }

}
