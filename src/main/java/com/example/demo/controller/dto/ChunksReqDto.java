package com.example.demo.controller.dto;

import io.swagger.annotations.ApiParam;
public class ChunksReqDto {
	@ApiParam(value = "文件md5",required = true)
	String fileMd5;
	@ApiParam(value = "该分片索引",required = true)
    int chunks;
	@ApiParam(value = "该分片大小",required = true)
    int chunk;
	public String getFileMd5() {
		return fileMd5;
	}
	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	public int getChunks() {
		return chunks;
	}
	public void setChunks(int chunks) {
		this.chunks = chunks;
	}
	public int getChunk() {
		return chunk;
	}
	public void setChunk(int chunk) {
		this.chunk = chunk;
	}
}
