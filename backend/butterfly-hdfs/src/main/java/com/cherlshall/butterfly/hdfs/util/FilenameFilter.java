package com.cherlshall.butterfly.hdfs.util;

@FunctionalInterface
public interface FilenameFilter {
    boolean accept(HdfsFile dir, String name);
}
