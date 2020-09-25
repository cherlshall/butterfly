package com.cherlshall.butterfly.util.entity;

import lombok.Data;

/**
 * Created by htf on 2020/9/24.
 */
@Data
public class FileInfo<T> {

    private String fileName;
    private T fileContent;

    public FileInfo() {

    }

    public FileInfo(String fileName, T fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

}
