package com.cherlshall.butterfly.module.hdfs.util;

@FunctionalInterface
public interface FilenameFilter {
    boolean accept(HdfsFile dir, String name);
}
