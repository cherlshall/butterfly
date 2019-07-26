package com.cherlshall.butterfly.module.hdfs.entity;

import lombok.Data;
import org.apache.hadoop.fs.FileStatus;

@Data
public class HdfsBean {
    private String path;
    private String name;
    private long len;
    private boolean file;
    private long modificationTime;
    private long blockSize;
    private String group;
    private String owner;
    private String permission;

    public HdfsBean fromStatus(FileStatus status) {
        this.setName(status.getPath().getName());
        this.setFile(status.isFile());
        this.setLen(status.getLen());
        this.setModificationTime(status.getModificationTime());
        this.setBlockSize(status.getBlockSize());
        this.setGroup(status.getGroup());
        this.setOwner(status.getOwner());
        this.setPermission(status.getPermission().toString());
        return this;
    }
}
