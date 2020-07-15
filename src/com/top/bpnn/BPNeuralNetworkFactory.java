package com.top.bpnn;

import java.io.*;
import java.util.*;

public class BPNeuralNetworkFactory {
    /**
     * 训练BP神经网络模型
     * @param bpParameter
     * @param inputAndOutput
     * @return
     */
    public BPModel trainBP(BPParameter bpParameter, Matrix inputAndOutput) throws Exception {
        //BP神经网络的输出
        BPModel result = new BPModel();
        result.setBpParameter(bpParameter);

        ActivationFunction activationFunction = bpParameter.getActivationFunction();
        int inputNum = bpParameter.getInputLayerNeuronNum();
        int hiddenNum = bpParameter.getHiddenLayerNeuronNum();
        int outputNum = bpParameter.getOutputLayerNeuronNum();
        double normalizationMin = bpParameter.getNormalizationMin();
        double normalizationMax = bpParameter.getNormalizationMax();
        double step = bpParameter.getStep();
        double momentumFactor = bpParameter.getMomentumFactor();
        double precision = bpParameter.getPrecision();
        int maxTimes = bpParameter.getMaxTimes();

        if(inputAndOutput.getMatrixColNums() != inputNum + outputNum){
            throw new Exception("神经元个数不符，请修改");
        }
        //初始化权值
        Matrix weightIJ = initWeight(inputNum, hiddenNum);
        Matrix weightJP = initWeight(hiddenNum, outputNum);

        //初始化阈值
        Matrix b1 = initThreshold(hiddenNum);
        Matrix b2 = initThreshold(outputNum);

        //动量项
        Matrix deltaWeightIJ0 = new Matrix(inputNum, hiddenNum);
        Matrix deltaWeightJP0 = new Matrix(hiddenNum, outputNum);
        Matrix deltaB10 = new Matrix(1, hiddenNum);
        Matrix deltaB20 = new Matrix(1, outputNum);

        Matrix input = new Matrix(new double[inputAndOutput.getMatrixRowNums()][inputNum]);
        Matrix output = new Matrix(new double[inputAndOutput.getMatrixRowNums()][outputNum]);
        for (int i = 0; i < inputAndOutput.getMatrixRowNums(); i++) {
            for (int j = 0; j < inputNum; j++) {
                input.getMatrix()[i][j] = inputAndOutput.getValOfIdx(i,j);
            }
            for (int j = 0; j < inputAndOutput.getMatrixColNums() - inputNum; j++) {
                output.getMatrix()[i][j] = inputAndOutput.getValOfIdx(i,inputNum+j);
            }
        }

        //归一化
        Map<String,Object> inputAfterNormalize = normalize(input, normalizationMin, normalizationMax);
        input = (Matrix) inputAfterNormalize.get("res");
        Matrix inputMax = (Matrix) inputAfterNormalize.get("max");
        Matrix inputMin = (Matrix) inputAfterNormalize.get("min");
        result.setInputMax(inputMax);
        result.setInputMin(inputMin);

        Map<String,Object> outputAfterNormalize = normalize(output, normalizationMin, normalizationMax);
        output = (Matrix) outputAfterNormalize.get("res");
        Matrix outputMax = (Matrix) outputAfterNormalize.get("max");
        Matrix outputMin = (Matrix) outputAfterNormalize.get("min");
        result.setOutputMax(outputMax);
        result.setOutputMin(outputMin);

        int times = 1;
        double E = 0;//误差
        while (times < maxTimes) {
            /*-----------------正向传播---------------------*/
            //隐含层输入
            Matrix jIn = input.multiple(weightIJ);
            double[][] b1CopyArr = new double[jIn.getMatrixRowNums()][b1.getMatrixRowNums()];
            //扩充阈值
            for (int i = 0; i < jIn.getMatrixRowNums(); i++) {
                b1CopyArr[i] = b1.getMatrix()[0];
            }
            Matrix b1Copy = new Matrix(b1CopyArr);
            //加上阈值
            jIn = jIn.plus(b1Copy);
            //隐含层输出
            Matrix jOut = computeValue(jIn,activationFunction);
            //输出层输入
            Matrix pIn = jOut.multiple(weightJP);
            double[][] b2CopyArr = new double[pIn.getMatrixRowNums()][b2.getMatrixRowNums()];
            //扩充阈值
            for (int i = 0; i < pIn.getMatrixRowNums(); i++) {
                b2CopyArr[i] = b2.getMatrix()[0];
            }
            Matrix b2Copy = new Matrix(b2CopyArr);
            //加上阈值
            pIn = pIn.plus(b2Copy);
            //输出层输出
            Matrix pOut = computeValue(pIn,activationFunction);
            //计算误差
            Matrix e = output.subtract(pOut);
            E = computeE(e);//误差
            //判断是否符合精度
            if (Math.abs(E) <= precision) {
                System.out.println("满足精度");
                break;
            }

            /*-----------------反向传播---------------------*/
            //J与P之间权值修正量
            Matrix deltaWeightJP = e.multiple(step);
            deltaWeightJP = deltaWeightJP.pointMultiple(computeDerivative(pIn,activationFunction));
            deltaWeightJP = deltaWeightJP.transpose().multiple(jOut);
            deltaWeightJP = deltaWeightJP.transpose();
            //P层神经元阈值修正量
            Matrix deltaThresholdP = e.multiple(step);
            deltaThresholdP = deltaThresholdP.transpose().multiple(computeDerivative(pIn, activationFunction));

            //I与J之间的权值修正量
            Matrix deltaO = e.pointMultiple(computeDerivative(pIn,activationFunction));
            Matrix tmp = weightJP.multiple(deltaO.transpose()).transpose();
            Matrix deltaWeightIJ = tmp.pointMultiple(computeDerivative(jIn, activationFunction));
            deltaWeightIJ = input.transpose().multiple(deltaWeightIJ);
            deltaWeightIJ = deltaWeightIJ.multiple(step);

            //J层神经元阈值修正量
            Matrix deltaThresholdJ = tmp.transpose().multiple(computeDerivative(jIn, activationFunction));
            deltaThresholdJ = deltaThresholdJ.multiple(-step);

            if (times == 1) {
                //更新权值与阈值
                weightIJ = weightIJ.plus(deltaWeightIJ);
                weightJP = weightJP.plus(deltaWeightJP);
                b1 = b1.plus(deltaThresholdJ);
                b2 = b2.plus(deltaThresholdP);
            }else{
                //加动量项
                weightIJ = weightIJ.plus(deltaWeightIJ).plus(deltaWeightIJ0.multiple(momentumFactor));
                weightJP = weightJP.plus(deltaWeightJP).plus(deltaWeightJP0.multiple(momentumFactor));
                b1 = b1.plus(deltaThresholdJ).plus(deltaB10.multiple(momentumFactor));
                b2 = b2.plus(deltaThresholdP).plus(deltaB20.multiple(momentumFactor));
            }

            deltaWeightIJ0 = deltaWeightIJ;
            deltaWeightJP0 = deltaWeightJP;
            deltaB10 = deltaThresholdJ;
            deltaB20 = deltaThresholdP;

            times++;
        }

        result.setWeightIJ(weightIJ);
        result.setWeightJP(weightJP);
        result.setB1(b1);
        result.setB2(b2);
        result.setError(E);
        result.setTimes(times);
        System.out.println("循环次数：" + times + "，误差：" + E);

        return result;
    }

    /**
     * 计算BP神经网络的值
     * @param bpModel
     * @param input
     * @return
     */
    public Matrix computeBP(BPModel bpModel,Matrix input) throws Exception {
        if (input.getMatrixColNums() != bpModel.getBpParameter().getInputLayerNeuronNum()) {
            throw new Exception("输入矩阵纬度有误");
        }
        ActivationFunction activationFunction = bpModel.getBpParameter().getActivationFunction();
        Matrix weightIJ = bpModel.getWeightIJ();
        Matrix weightJP = bpModel.getWeightJP();
        Matrix b1 = bpModel.getB1();
        Matrix b2 = bpModel.getB2();
        double[][] normalizedInput = new double[input.getMatrixRowNums()][input.getMatrixColNums()];
        for (int i = 0; i < input.getMatrixRowNums(); i++) {
            for (int j = 0; j < input.getMatrixColNums(); j++) {
                normalizedInput[i][j] = bpModel.getBpParameter().getNormalizationMin()
                        + (input.getValOfIdx(i,j) - bpModel.getInputMin().getValOfIdx(0,j))
                        / (bpModel.getInputMax().getValOfIdx(0,j) - bpModel.getInputMin().getValOfIdx(0,j))
                        * (bpModel.getBpParameter().getNormalizationMax() - bpModel.getBpParameter().getNormalizationMin());
            }
        }
        Matrix normalizedInputMatrix = new Matrix(normalizedInput);
        Matrix jIn = normalizedInputMatrix.multiple(weightIJ);
        double[][] b1CopyArr = new double[jIn.getMatrixRowNums()][b1.getMatrixRowNums()];
        //扩充阈值
        for (int i = 0; i < jIn.getMatrixRowNums(); i++) {
            b1CopyArr[i] = b1.getMatrix()[0];
        }
        Matrix b1Copy = new Matrix(b1CopyArr);
        //加上阈值
        jIn = jIn.plus(b1Copy);
        //隐含层输出
        Matrix jOut = computeValue(jIn,activationFunction);
        //输出层输入
        Matrix pIn = jOut.multiple(weightJP);
        double[][] b2CopyArr = new double[pIn.getMatrixRowNums()][b2.getMatrixRowNums()];
        //扩充阈值
        for (int i = 0; i < pIn.getMatrixRowNums(); i++) {
            b2CopyArr[i] = b2.getMatrix()[0];
        }
        Matrix b2Copy = new Matrix(b2CopyArr);
        //加上阈值
        pIn = pIn.plus(b2Copy);
        //输出层输出
        Matrix pOut = computeValue(pIn,activationFunction);
        //反归一化
        Matrix result = inverseNormalize(pOut, bpModel.getBpParameter().getNormalizationMax(), bpModel.getBpParameter().getNormalizationMin(), bpModel.getOutputMax(), bpModel.getOutputMin());

        return result;

    }

    //初始化权值
    private Matrix initWeight(int x,int y){
        Random random=new Random();
        double[][] weight = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                weight[i][j] = 2*random.nextDouble()-1;
            }
        }
        return new Matrix(weight);
    }
    //初始化阈值
    private Matrix initThreshold(int x){
        Random random = new Random();
        double[][] result = new double[1][x];
        for (int i = 0; i < x; i++) {
            result[0][i] = 2*random.nextDouble()-1;
        }
        return new Matrix(result);
    }

    /**
     * 计算激活函数的值
     * @param a
     * @return
     */
    private Matrix computeValue(Matrix a, ActivationFunction activationFunction) throws Exception {
        if (a.getMatrix() == null) {
            throw new Exception("参数值为空");
        }
        double[][] result = new double[a.getMatrixRowNums()][a.getMatrixColNums()];
        for (int i = 0; i < a.getMatrixRowNums(); i++) {
            for (int j = 0; j < a.getMatrixColNums(); j++) {
                result[i][j] = activationFunction.computeValue(a.getValOfIdx(i,j));
            }
        }
        return new Matrix(result);
    }

    /**
     * 激活函数导数的值
     * @param a
     * @return
     */
    private Matrix computeDerivative(Matrix a , ActivationFunction activationFunction) throws Exception {
        if (a.getMatrix() == null) {
            throw new Exception("参数值为空");
        }
        double[][] result = new double[a.getMatrixRowNums()][a.getMatrixColNums()];
        for (int i = 0; i < a.getMatrixRowNums(); i++) {
            for (int j = 0; j < a.getMatrixColNums(); j++) {
                result[i][j] = activationFunction.computeDerivative(a.getValOfIdx(i,j));
            }
        }
        return new Matrix(result);
    }

    /**
     * 数据归一化
     * @param a 要归一化的数据
     * @param normalizationMin  要归一化的区间下限
     * @param normalizationMax  要归一化的区间上限
     * @return
     */
    private Map<String, Object> normalize(Matrix a, double normalizationMin, double normalizationMax) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        double[][] maxArr = new double[1][a.getMatrixColNums()];
        double[][] minArr = new double[1][a.getMatrixColNums()];
        double[][] res = new double[a.getMatrixRowNums()][a.getMatrixColNums()];
        for (int i = 0; i < a.getMatrixColNums(); i++) {
            List tmp = new ArrayList();
            for (int j = 0; j < a.getMatrixRowNums(); j++) {
                tmp.add(a.getValOfIdx(j,i));
            }
            double max = (double) Collections.max(tmp);
            double min = (double) Collections.min(tmp);
            //数据归一化(注:若max与min均为0则不需要归一化)
            if (max != 0 || min != 0) {
                for (int j = 0; j < a.getMatrixRowNums(); j++) {
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
    private Matrix inverseNormalize(Matrix a, double normalizationMax, double normalizationMin , Matrix dataMax,Matrix dataMin) throws Exception {
        double[][] res = new double[a.getMatrixRowNums()][a.getMatrixColNums()];
        for (int i = 0; i < a.getMatrixColNums(); i++) {
            //数据反归一化
            if (dataMin.getValOfIdx(0,i) != 0 || dataMax.getValOfIdx(0,i) != 0) {
                for (int j = 0; j < a.getMatrixRowNums(); j++) {
                    res[j][i] = dataMin.getValOfIdx(0,i) + (dataMax.getValOfIdx(0,i) - dataMin.getValOfIdx(0,i)) * (a.getValOfIdx(j,i) - normalizationMin) / (normalizationMax - normalizationMin);
                }
            }
        }
        return new Matrix(res);
    }

    /**
     * 计算误差
     * @param e
     * @return
     */
    private double computeE(Matrix e) throws Exception {
        e = e.square();
        return 0.5*e.sumAll();
    }

    /**
     * 将BP模型序列化到本地
     * @param bpModel
     * @throws IOException
     */
    public void serialize(BPModel bpModel,String path) throws IOException {
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(bpModel);
        out.close();
    }

    /**
     * 将BP模型反序列化
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public BPModel deSerialization(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
        BPModel bpModel = (BPModel) oin.readObject(); // 强制转换到BPModel类型
        oin.close();
        return bpModel;
    }
}
