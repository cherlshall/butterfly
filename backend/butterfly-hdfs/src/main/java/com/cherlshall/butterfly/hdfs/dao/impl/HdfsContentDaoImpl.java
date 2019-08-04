package com.cherlshall.butterfly.hdfs.dao.impl;

import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.hdfs.dao.HdfsContentDao;
import com.cherlshall.butterfly.hdfs.util.HdfsFile;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HdfsContentDaoImpl implements HdfsContentDao {

    @Autowired
    private FileSystem fs;

    @Override
    public boolean write(String path, String content) {
        HdfsFile file = new HdfsFile(path, fs);
        try (FSDataOutputStream fos = fs.create(file.getFsPath())) {
            fos.writeBytes(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String read(String path) {
        HdfsFile file = new HdfsFile(path, fs);
        try (FSDataInputStream fis = fs.open(file.getFsPath());
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageData<String> readLine(String path, int startLine, int size) {
        HdfsFile file = new HdfsFile(path, fs);
        try (FSDataInputStream fis = fs.open(file.getFsPath());
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            int lineIndex = 0;
            PageData<String> pageData = new PageData<>();
            List<String> list = new ArrayList<>();
            pageData.setDataSource(list);
            while((line = br.readLine()) != null){
                if (lineIndex >= startLine && lineIndex < startLine + size) {
                    list.add(line);
                }
                lineIndex++;
            }
            pageData.setTotal(lineIndex);
            return pageData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
