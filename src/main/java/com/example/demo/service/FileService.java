package com.example.demo.service;

import com.example.demo.entity.UploadFile;
import com.example.demo.mapper.FileMapper;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    @Autowired FileMapper fileMapper;
    public int save(UploadFile uploadFile){
        return fileMapper.save(uploadFile);
    }
    public boolean isMd5Exist(String md5) {
//        Query query = new Query(entityManager);
//        @SuppressWarnings("unchecked") List<File> result = query.from(File.class)
//                .select()
//                .whereEqual("MD5", md5)
//                .createTypedQuery()
//                .getResultList();

//        return !result.isEmpty();
        String name=fileMapper.isMd5Exist(md5);
        if (name==null || "".equals(name)){
            return false;
        }
        return true;
    }
}
