package com.example.demo.mapper;

import com.example.demo.entity.FileInfo;

public interface FileInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(FileInfo record);

    int insertSelective(FileInfo record);

    FileInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FileInfo record);

    int updateByPrimaryKey(FileInfo record);

    /**
     * 
     * <p>Title: selectByMd5</p>
     * <p>Description:根据文件MD5值去查询 </p>
     * @param fileMd5
     * @return
     * @author JobsZhang
     * @date 2018年5月15日 下午8:46:14
     */
	FileInfo selectByMd5(String fileMd5);
}