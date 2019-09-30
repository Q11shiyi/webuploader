package com.example.demo.service;

import com.example.demo.entity.FileInfo;

public interface IFileInfoService extends IBaseService<FileInfo>{

	FileInfo selectByMd5(String fileMd5);

}
