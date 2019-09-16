package com.example.demo.mapper;


import com.example.demo.entity.UploadFile;
import com.example.demo.entity.UploadInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    int save(UploadFile uploadFile);
    String isMd5Exist(String md5);
}
