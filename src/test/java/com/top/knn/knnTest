package knn;

import com.top.knn.KNN;
import com.top.matrix.Matrix;
import com.top.utils.CsvInfo;
import com.top.utils.CsvUtil;
import com.top.utils.DoubleUtil;
import org.junit.Test;

/**
 * @program: top-algorithm-set
 * @description:
 * @author: Mr.Zhao
 * @create: 2020-10-26 22:04
 **/
public class knnTest {
    @Test
    public void test() throws Exception {
        // 训练集
        CsvInfo csvInfo = CsvUtil.getCsvInfo(false, "E:\\jarTest\\trainData.csv");
        Matrix trainSet = csvInfo.toMatrix();
        Matrix trainSetLabels = trainSet.getColOfIdx(trainSet.getMatrixColCount() - 1);
        Matrix trainSetData = trainSet.subMatrix(0, trainSet.getMatrixRowCount(), 0, trainSet.getMatrixColCount() - 1);

        CsvInfo csvInfo1 = CsvUtil.getCsvInfo(false, "E:\\jarTest\\testData.csv");
        Matrix testSet = csvInfo1.toMatrix();
        Matrix testSetData = trainSet.subMatrix(0, testSet.getMatrixRowCount(), 0, testSet.getMatrixColCount() - 1);
        Matrix testSetLabels = trainSet.getColOfIdx(testSet.getMatrixColCount() - 1);

        // 分类
        long startTime = System.currentTimeMillis();
        Matrix result = KNN.classify(testSetData, trainSetData, trainSetLabels, 5);
        long endTime = System.currentTimeMillis();
        System.out.println("run time:" + (endTime - startTime));
        // 正确率
        Matrix error = result.subtract(testSetLabels);
        int total = error.getMatrixRowCount();
        int correct = 0;
        for (int i = 0; i < error.getMatrixRowCount(); i++) {
            if (DoubleUtil.equals(error.getValOfIdx(i, 0), 0.0)) {
                correct++;
            }
        }
        double correctRate = Double.valueOf(correct) / Double.valueOf(total);
        System.out.println("correctRate:"+ correctRate);
    }
}
