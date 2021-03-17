package bpnn;

import com.top.bpnn.BPModel;
import com.top.bpnn.BPNeuralNetworkFactory;
import com.top.bpnn.BPParameter;
import com.top.matrix.Matrix;
import com.top.utils.CsvInfo;
import com.top.utils.CsvUtil;
import com.top.utils.SerializationUtil;
import org.junit.Test;

import java.util.Date;

public class bpnnTest {
    @Test
    public void test() throws Exception {
        // 创建训练集矩阵
        CsvInfo csvInfo = CsvUtil.getCsvInfo(false, "E:\\jarTest\\trainData.csv");
        Matrix trainSet = csvInfo.toMatrix();
        // 创建BPNN工厂对象
        BPNeuralNetworkFactory factory = new BPNeuralNetworkFactory();
        // 创建BP参数对象
        BPParameter bpParameter = new BPParameter();
        bpParameter.setInputLayerNeuronCount(4);
        bpParameter.setHiddenLayerNeuronCount(5);
        bpParameter.setPrecision(0.01);
        bpParameter.setMaxTimes(100000);

        // 训练BP神经网络
        System.out.println(new Date());
        BPModel bpModel = factory.trainBP(bpParameter, trainSet);
        System.out.println(new Date());

        // 将BPModel序列化到本地
        SerializationUtil.serialize(bpModel, "test");

        CsvInfo csvInfo2 = CsvUtil.getCsvInfo(false, "E:\\jarTest\\testData.csv");
        Matrix testSet = csvInfo2.toMatrix();

        Matrix testData1 = testSet.subMatrix(0, testSet.getMatrixRowCount(), 0, testSet.getMatrixColCount() - 1);
        Matrix testLabel = testSet.subMatrix(0, testSet.getMatrixRowCount(), testSet.getMatrixColCount() - 1, 1);
        // 将BPModel反序列化
        BPModel bpModel1 = (BPModel) SerializationUtil.deSerialization("test");
        Matrix result = factory.computeBP(bpModel1, testData1);

        int total = result.getMatrixRowCount();
        int correct = 0;
        for (int i = 0; i < result.getMatrixRowCount(); i++) {
            if(Math.round(result.getValOfIdx(i,0)) == testLabel.getValOfIdx(i,0)){
                correct++;
            }
        }
        double correctRate = Double.valueOf(correct) / Double.valueOf(total);
        System.out.println(correctRate);
    }
    
    /**
     * 使用示例
     * @throws Exception
     */
    @Test
    public void bpnnUsing() throws Exception{
        CsvInfo csvInfo = CsvUtil.getCsvInfo(false, "E:\\jarTest\\data.csv");
        Matrix data = csvInfo.toMatrix();
        // 将BPModel反序列化
        BPModel bpModel1 = (BPModel) SerializationUtil.deSerialization("test");
        // 创建工厂
        BPNeuralNetworkFactory factory = new BPNeuralNetworkFactory();
        Matrix result = factory.computeBP(bpModel1, data);
        CsvUtil.createCsvFile(null,result,"E:\\jarTest\\computeResult.csv");
    }
    
}
