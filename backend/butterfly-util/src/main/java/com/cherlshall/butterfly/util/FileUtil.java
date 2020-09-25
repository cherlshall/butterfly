package com.cherlshall.butterfly.util;

import java.io.*;

/**
 * Created by htf on 2020/9/24.
 */
public class FileUtil {

    public static String readFileToString(String path) {
        try {
            return convertStreamToString(new FileInputStream(path), true);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static String readFileToString(File file) {
        try {
            return convertStreamToString(new FileInputStream(file), true);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static String convertStreamToString(InputStream inputStream, boolean autoClose) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (autoClose) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static boolean writeToFile(String path, String content) {
        if (prepareForWriteToFile(path)) {
            try (FileWriter writer = new FileWriter(path)) {
                writer.write(content);
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean writeToFile(String path, byte[] content) {
        if (prepareForWriteToFile(path)) {
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(content);
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    private static boolean prepareForWriteToFile(String path) {
        File file = new File(path);
        if (file.exists() && !file.delete()) {
            return false;
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists() || parentFile.isFile()) {
            if (!parentFile.mkdirs()) {
                return false;
            }
        }
        try {
            if (!file.createNewFile()) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (!deleteFile(f)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }
}
