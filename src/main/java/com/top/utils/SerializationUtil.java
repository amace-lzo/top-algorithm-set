package com.top.utils;

import com.top.bpnn.BPModel;

import java.io.*;

public class SerializationUtil {
    /**
     * 对象序列化到本地
     * @param object
     * @throws IOException
     */
    public static void serialize(Object object, String path) throws IOException {
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(object);
        out.close();
    }

    /**
     * 对象反序列化
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deSerialization(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
        Object object = oin.readObject();
        oin.close();
        return object;
    }
}
