package com.top.utils;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.top.matrix.Matrix;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CsvUtil {
    /**
     * 获取CSV中的信息
     * @param hasHeader 是否含有表头
     * @param path CSV文件的路径
     * @return
     * @throws IOException
     */
    public static CsvInfo getCsvInfo(boolean hasHeader , String path) throws IOException {
        //创建csv对象，存储csv中的信息
        CsvInfo csvInfo = new CsvInfo();
        //获取CsvReader流
        CsvReader csvReader = new CsvReader(path, ',', StandardCharsets.UTF_8);
        if(hasHeader){
            csvReader.readHeaders();
        }
        //获取Csv中的所有记录
        ArrayList<String[]> csvFileList = new ArrayList<String[]>();
        while (csvReader.readRecord()) {
            csvFileList.add(csvReader.getValues());
        }
        //赋值
        csvInfo.setHeader(csvReader.getHeaders());
        csvInfo.setCsvFileList(csvFileList);
        //关闭流
        csvReader.close();
        return csvInfo;
    }

    /**
     * 将矩阵写入到csv文件中
     * @param header 表头
     * @param data 以矩阵形式存放的数据
     * @param path 写入的文件地址
     * @throws Exception
     */
    public static void createCsvFile(String[] header,Matrix data,String path) throws Exception {

        if (header!=null && header.length != data.getMatrixColCount()) {
            throw new Exception("表头列数与数据列数不符");
        }
        CsvWriter csvWriter = new CsvWriter(path, ',', StandardCharsets.UTF_8);

        if (header != null) {
            csvWriter.writeRecord(header);
        }
        for (int i = 0; i < data.getMatrixRowCount(); i++) {
            String[] record = new String[data.getMatrixColCount()];
            for (int j = 0; j < data.getMatrixColCount(); j++) {
                record[j] = data.getValOfIdx(i, j)+"";
            }
            csvWriter.writeRecord(record);
        }
        csvWriter.close();
    }
}
