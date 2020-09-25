package com.cherlshall.butterfly.util;

import com.cherlshall.butterfly.util.entity.FileInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by htf on 2020/9/24.
 */
public class Zip {

    public static <T> byte[] create(String dirName, List<FileInfo<T>> fileInfos) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(out)) {
            if (dirName != null && !dirName.isEmpty()) {
                if (!dirName.endsWith("/") && !dirName.endsWith("\\")) {
                    dirName += File.separator;
                }
                zos.putNextEntry(new ZipEntry(dirName));
            } else {
                dirName = "";
            }
            for (FileInfo<T> fileInfo : fileInfos) {
                ZipEntry zipEntry = new ZipEntry(dirName + fileInfo.getFileName());
                zos.putNextEntry(zipEntry);
                T fileContent = fileInfo.getFileContent();
                if (fileContent instanceof byte[]) {
                    zos.write((byte[]) fileContent);
                } else {
                    zos.write(fileContent.toString().getBytes());
                }
            }
            zos.close();
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
