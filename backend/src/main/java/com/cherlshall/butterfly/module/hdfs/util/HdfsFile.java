package com.cherlshall.butterfly.module.hdfs.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HdfsFile {
    private FileSystem fs;

    private String path;
    private String parentPath;
    private Path fsPath;

    /**
     * create a new hdfs FileSystem
     */
    public static FileSystem createFs(String host, int port) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://" + host + ":" + port);
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        try {
            return FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FileSystem getFs() {
        return fs;
    }

    public HdfsFile(String path, FileSystem fs) {
        this.fs = fs;
        if ("/".equals(path)) {
            this.path = path;
            this.parentPath = null;
        } else {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            this.path = path;
            this.parentPath = path.substring(0, path.lastIndexOf("/"));
        }
    }

    public Path getFsPath() {
        if (this.fsPath == null) {
            this.fsPath = new Path(this.path);
        }
        return this.fsPath;
    }

    /**
     * @return parent dir's absolute path
     */
    public String getParent() {
        return parentPath;
    }

    /**
     * @return parent dir's HdfsFile
     */
    public HdfsFile getParentFile() {
        if (parentPath == null) {
            return null;
        }
        return new HdfsFile(parentPath, fs);
    }

    /**
     * @return current file's absolute path
     */
    public String getPath() {
        return path;
    }

    public boolean isDirectory() throws IOException {
        FileStatus fileStatus = fs.getFileStatus(getFsPath());
        return fileStatus.isDirectory();
    }

    public boolean isFile() throws IOException {
        FileStatus fileStatus = fs.getFileStatus(getFsPath());
        return fileStatus.isFile();
    }

    public boolean exists() throws IOException {
        return fs.exists(getFsPath());
    }

    /**
     * create current HdfsFile on hdfs
     * @return create success: true; failure/exists: false
     */
    public boolean createNewFile() {
        try {
            if (exists()) {
                return false;
            }
            return fs.createNewFile(getFsPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * delete current HdfsFile on hdfs
     * @return delete success: true; failure/exists: false
     */
    public boolean delete() {
        try {
            return fs.delete(getFsPath(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * rename/move current HdfsFile to dest
     * @param dest dest file
     * @return success: true; failure: false
     */
    public boolean renameTo(HdfsFile dest) {
        try {
            return fs.rename(getFsPath(), dest.getFsPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * list files under current dir
     * @param filter filter files
     * @return files under current dir which pass filter
     */
    public HdfsFile[] listFiles(FilenameFilter filter) {
        try {
            if (isFile()) {
                return null;
            }
            FileStatus[] status = fs.listStatus(getFsPath());
            List<HdfsFile> files = new ArrayList<>();
            String prePath = "/".equals(this.path) ? this.path : this.path + "/";
            for (FileStatus fileStatus : status) {
                String fileName = fileStatus.getPath().getName();
                String absolutePath = prePath + fileName;
                if (filter == null || filter.accept(this, fileName)) {
                    files.add(new HdfsFile(absolutePath, this.fs));
                }
            }
            return files.toArray(new HdfsFile[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HdfsFile[] listFiles() {
        return listFiles(null);
    }

    /**
     * mkdir recurrence
     * @return success: true; exist/failure: false
     */
    public boolean mkdirs() {
        try {
            if (exists()) {
                return false;
            }
            return fs.mkdirs(getFsPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get current file name
     * @return if root return /, else return current file name
     */
    public String getName() {
        if (getParent() == null) {
            return path;
        }
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * get file size(byte)
     * @return file: file bytes; dir: 0; file not exists: -1
     */
    public long length() {
        try {
            return fs.getFileStatus(getFsPath()).getLen();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
