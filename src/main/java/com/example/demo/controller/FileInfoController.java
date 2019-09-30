package com.example.demo.controller;

import com.example.demo.controller.dto.BaseResDto;
import com.example.demo.controller.dto.ChunksReqDto;
import com.example.demo.entity.FileInfo;
import com.example.demo.service.IFileInfoService;
import com.example.demo.util.FileUploadUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

//		checkFile;//检测文件是否存在url
//		checkChunk;//检测分片url
//		mergeChunks;//合并文件请求地址
//		uploadChunks;//上传分片url
@Api(tags = {"断点续传接口"})
@Controller
@RequestMapping(value="/file")
public class FileInfoController extends BaseControler{

	@Autowired
	private IFileInfoService fileInfoService;
	private String uploadPath="F:/test";
	private String uploadUrl = "F:/test/upload";
	
	private String whitelist = "gif.jpg.jpeg.bmp.png.mp3.mp4.exe.txt.avi.flv.doc.docx.ppt.pptx.xls.xlsx,GIF.JPG.JPEG.BMP.PNG.MP3.MP4.EXE.TXT.AVI.FLV.DOC.DOCX."
			+ "PPT.PPTX.XLS.XLSX,zip"
			+ ",ZIP,RAR,rar";

	
	
	
	/**
	 * 
	 * <p>Title: checkFile</p>
	 * <p>Description:根据文件MD5查询文件是否存在 </p>
	 * @param fileMd5 文件唯一指纹
	 * @return
	 * @author JobsZhang
	 * @date 2018年6月8日 下午5:48:29
	 */
	@ApiOperation(value="检测文件md5" , notes = "文件是否在服务器中接口")
	@ApiImplicitParam( name = "fileMd5" ,value = "文件md5值",paramType = "query",dataType = "String")
	@RequestMapping(value="/checkFile",method= RequestMethod.POST)
	@ResponseBody
	public BaseResDto checkFile (@RequestParam String fileMd5){
		BaseResDto brd = new BaseResDto();
//		FileInfo fileInfo = fileInfoService.selectByMd5(fileMd5);
//		brd.setSuccess(fileInfo!=null);
//		brd.setValues(fileInfo);
		brd.setSuccess(false);
		return brd;
	}
	
	/**
	 * 
	 * <p>Title: checkChunk</p>
	 * <p>Description: 检测分片是否已经存在</p>
	 * @param fileMd5 文件唯一指纹
	 * @param chunk 分片编号
	 * @param chunkSize 分片大小
	 * @return
	 * @author JobsZhang
	 * @date 2018年6月8日 下午5:48:56
	 */
	@ApiOperation(value="检测分片" , notes = "检测文件分片是否在服务器中已存在接口")
	@ApiImplicitParams({@ApiImplicitParam( name = "fileMd5" ,value = "文件md5值",paramType = "query",dataType = "String"),
			@ApiImplicitParam( name = "chunk" ,value = "该分片索引",paramType = "query",dataType = "int"),
			@ApiImplicitParam( name = "chunkSize" ,value = "该分片大小",paramType = "query",dataType = "long")
	})
	@RequestMapping(value="/checkChunk",method= RequestMethod.POST)
	@ResponseBody
	public BaseResDto checkChunk (@RequestParam String fileMd5, @RequestParam int chunk, @RequestParam long chunkSize){
		//定义返回值
		BaseResDto brd = new BaseResDto();
		//获取文件保存路径
		String path = uploadUrl;
		//按分片序号命名排序
		String filePath = path+File.separator+fileMd5+File.separator+chunk;//合并文件必须要按照顺序合并，所以不能重命名		
		File file = new File(filePath);
		long length = file.length();
		if(file.exists()&&length==chunkSize){//分片已存在，这里一定要验证传过来的分片大小是否和已存在的分片大小相同，
			brd.setSuccess(true);
		}
		return brd;
	}
	
	/**
	 * 
	 * <p>Title: uploadChunks</p>
	 * <p>Description: 分片上传</p>
	 * @param fileMd5 文件唯一指纹
	 * @param chunk 分片编号
	 * @param chunkSize 分片大小
	 * @return
	 * @author JobsZhang
	 * @date 2018年6月8日 下午5:58:46
	 */

	@RequestMapping(value="/uploadChunks",method= RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="上传分片" , notes = "上传文件分片接口")
	public BaseResDto uploadChunks (@RequestParam("file")@ApiParam(name = "multipartFile" ,value = "上传分片",required = true) MultipartFile multipartFile,
									ChunksReqDto reqDto){
		BaseResDto brd = new BaseResDto();
		FileInfo fileInfo = null;
		try {
			fileInfo = FileUploadUtil.chunksUpload(multipartFile, request, uploadUrl, whitelist, reqDto.getFileMd5(), reqDto.getChunk());
			brd.setSuccess(true);
			brd.setValues(fileInfo);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			brd.setMesg("上传出错"+e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			brd.setMesg("上传出错"+e.getMessage());
		}
		return brd;
	}
	/**
	 * 上传完成合并分片
	 * @param fileMd5 文件唯一指纹
	 * @param fileName 文件名
	 * @return
	 */
	@RequestMapping(value="/mergeChunks",method= RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="合并分片" , notes = "合并文件分片接口")
	@ApiImplicitParams({@ApiImplicitParam( name = "fileMd5" ,value = "文件md5",paramType = "query",dataType = "String"),
			@ApiImplicitParam( name = "fileName" ,value = "文件名称",paramType = "query",dataType = "String"),
	})
	public BaseResDto mergeChunks (@RequestParam String fileMd5, @RequestParam String fileName){
		BaseResDto brd = new BaseResDto();
		FileInfo fileInfo = null;
		try {
			fileInfo = FileUploadUtil.mergeChunks(request, uploadUrl, whitelist, fileMd5, fileName, false);
			//fileInfoService.insert(fileInfo);
			brd.setSuccess(true);
			brd.setValues(fileInfo);
		}catch (IllegalStateException e) {
			e.printStackTrace();
			brd.setMesg("合并文件出错"+e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			brd.setMesg("合并文件出错"+e.getMessage());
		}
		return brd;
	}

}
