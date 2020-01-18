package com.cherlshall.butterfly.hdfs.dao.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.hdfs.dao.FileDao;
import com.cherlshall.butterfly.hdfs.entity.HdfsBean;
import com.cherlshall.butterfly.hdfs.util.HdfsFile;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileDaoImpl implements FileDao {

    @Autowired
    private FileSystem fs;

    @Override
    public String[] listFileName(String parent) {
        HdfsFile dir = new HdfsFile(parent, fs);
        HdfsFile[] files = dir.listFiles();
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }
        return names;
    }

    @Override
    public List<HdfsBean> listFileDetail(String parent) {
        HdfsFile dir = new HdfsFile(parent, fs);
        try {
            FileStatus[] status = fs.listStatus(dir.getFsPath());
            String prePath = "/".equals(dir.getPath()) ? dir.getPath() : dir.getPath() + "/";
            List<HdfsBean> beans = new ArrayList<>(status.length);
            for (FileStatus s : status) {
                HdfsBean bean = new HdfsBean();
                bean.setPath(prePath + s.getPath().getName());
                bean.fromStatus(s);
                beans.add(bean);
            }
            return beans;
        } catch (FileNotFoundException e) {
            throw new ButterflyException("file not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ButterflyException();
        }
    }

    @Override
    public boolean create(String path) {
        HdfsFile file = new HdfsFile(path, fs);
        return file.createNewFile();
    }

    @Override
    public boolean delete(String path) {
        HdfsFile file = new HdfsFile(path, fs);
        return file.delete();
    }

    @Override
    public boolean exists(String path) {
        HdfsFile file = new HdfsFile(path, fs);
        try {
            return file.exists();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ButterflyException();
        }
    }

    @Override
    public boolean mkdirs(String path) {
        HdfsFile file = new HdfsFile(path, fs);
        return file.mkdirs();
    }
}
