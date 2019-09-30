package com.example.demo.service;

import com.example.demo.entity.FileInfo;
import com.example.demo.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IFileInfoServiceImpl implements IFileInfoService {

	@Autowired
	private FileInfoMapper fileInfoMapper;
	@Override
	public boolean insert(FileInfo t) {
		return fileInfoMapper.insert(t)>0;
	}

	@Override
	public FileInfo selectById(String id) {
		return fileInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public boolean updateById(FileInfo t) {
		return fileInfoMapper.updateByPrimaryKey(t)>0;
	}

	@Override
	public boolean deleteById(String id) {
		return fileInfoMapper.deleteByPrimaryKey(id)>0;
	}

	@Override
	public List<FileInfo> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public PageInfo<FileInfo> queryByParamsWithPage(int pageNum, int pageSize,
//			FileInfo t) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public FileInfo selectByMd5(String fileMd5) {
		return fileInfoMapper.selectByMd5(fileMd5);
	}

}
