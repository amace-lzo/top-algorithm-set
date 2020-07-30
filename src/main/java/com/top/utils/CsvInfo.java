package com.top.utils;

import com.top.matrix.Matrix;

import java.util.ArrayList;

public class CsvInfo {
    private String[] header;
    private int csvRowCount;
    private int csvColCount;
    private ArrayList<String[]> csvFileList;

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public int getCsvRowCount() {
        return csvRowCount;
    }

    public int getCsvColCount() {
        return csvColCount;
    }

    public ArrayList<String[]> getCsvFileList() {
        return csvFileList;
    }

    public void setCsvFileList(ArrayList<String[]> csvFileList) {
        this.csvFileList = csvFileList;
        this.csvColCount = csvFileList.get(0) != null?csvFileList.get(0).length:0;
        this.csvRowCount = csvFileList.size();
    }

    public Matrix toMatrix() throws Exception {
        double[][] arr = new double[csvFileList.size()][csvFileList.get(0).length];
        for (int i = 0; i < csvFileList.size(); i++) {
            for (int j = 0; j < csvFileList.get(0).length; j++) {
                try {
                    arr[i][j] = Double.parseDouble(csvFileList.get(i)[j]);
                }catch (NumberFormatException e){
                    throw new Exception("Csv中含有非数字字符，无法转换成Matrix对象");
                }
            }
        }
        return new Matrix(arr);
    }

}
