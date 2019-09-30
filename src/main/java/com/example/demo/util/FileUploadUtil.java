package com.example.demo.util;

import com.example.demo.entity.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

public class FileUploadUtil {

	public static String getFileExt(String fileName){
		if(StringUtils.isBlank(fileName)){
			return "";
		}
	    return StringUtils.trim(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())).toLowerCase();
	   }
	
	
	public static String getFileName(String name ){
		if(StringUtils.isBlank(name)){
			return "";
		}
		if(name.indexOf(".") == -1){
			return name;
		}
		return name.substring(0, name.lastIndexOf("."));
	}


	/**
	 * 
	 * <p>Title: chunksUpload</p>
	 * <p>Description: 上传分片</p>
	 * @param multipartFile
	 * @param request
	 * @param uploadUrl 上传路径
	 * @param whitelist 允许上传类型
	 * @param fileMd5 文件唯一指纹
	 * @param chunk 分片编号
	 * @return
	 * @throws IllegalStateException
	 * @throws Exception
	 * @author JobsZhang
	 * @date 2018年6月8日 下午5:52:42
	 */
	public static FileInfo chunksUpload(MultipartFile multipartFile, HttpServletRequest request, String uploadUrl, String whitelist, String fileMd5, int chunk)
			throws IllegalStateException, Exception{
		// 获取绝对路径
		String path = uploadUrl;
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		//原始文件名
		String oldName = multipartFile.getOriginalFilename();
		//文件类型
		String fileType = getFileExt(oldName);
		
		if(StringUtils.isBlank(fileType)){
			throw new Exception("文件后缀不能为空！");
		}
		
		String fileName = getFileName(oldName);
		
		if(whitelist.indexOf(fileType)==-1){ //文件类型不在白名单中
			throw new Exception("文件格式不正确！允许上传的文件类型"+whitelist);
		}
		File dir = new File( path+File.separator+fileMd5+File.separator);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String filePath = path+File.separator+fileMd5+File.separator+chunk;//合并文件必须要按照顺序合并，所以不能重命名
		multipartFile.transferTo(new File(filePath));
		FileInfo fileInfo = new FileInfo();
		long fileSize =  multipartFile.getSize();
		fileInfo.setFileName(fileName);
		fileInfo.setId(UUID.getUUID());
		fileInfo.setPath(filePath);
		fileInfo.setSize(fileSize);
		fileInfo.setType(fileType);
		fileInfo.setUploadTime(new Date());
		fileInfo.setUrl(uploadUrl+"/"+fileName+"."+fileType);
		return fileInfo;
	}
	
	/**
	 * 
	 * <p>Title: mergeChunks</p>
	 * <p>Description:合并分片 </p>
	 * @param request
	 * @param uploadUrl 上传基本路径
	 * @param whitelist 允许上传类型
	 * @param fileMd5 文件唯一指纹
	 * @param fileAllName 带后缀的文件名
	 * @param isReName 是否需要重命名
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 * @author JobsZhang
	 * @date 2018年6月8日 下午5:52:57
	 */
	@SuppressWarnings("resource")
	public static synchronized FileInfo mergeChunks(HttpServletRequest request, String uploadUrl, String whitelist, String fileMd5, String fileAllName, boolean isReName)
			throws FileNotFoundException,IOException,Exception {
		//文件类型
		String fileType = getFileExt(fileAllName);
		
		if(StringUtils.isBlank(fileType)){
			throw new Exception("文件后缀不能为空！");
		}
		
		String fileName = getFileName(fileAllName);
		
		if(whitelist.indexOf(fileType)==-1){ //文件类型不在白名单中
			throw new Exception("文件格式不正确！允许上传的文件类型"+whitelist);
		}
		
		// 获取绝对路径
		String path = uploadUrl;
		
		
		File f = new File(path+"/"+fileMd5);
		// 排除目录，只要文件
		File[] fileArray = f.listFiles(pathname -> {
			if (pathname.isDirectory()) {
				return false;
			}
			return true;
		});
		// 转成集合，便于排序
		List<File> fileList = new ArrayList<>(Arrays.asList(fileArray));
		// 按照分片的顺序排
		Collections.sort(fileList, (o1, o2) -> {
			if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
				return -1;
			}
			return 1;
		});
		
		String filePath = isReName ? path+File.separator+UUID.getUUID()+"."+fileType : path+File.separator+fileAllName;
		
		File file = new File(filePath);
		file.createNewFile();
		// 输出流
		FileChannel outChannel  = new FileOutputStream(file).getChannel();
		// 合并
		FileChannel inChannel;
		for (File tmp : fileList) {
			inChannel = new FileInputStream(tmp).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
			inChannel.close();
			// 删除分片
			file.delete();
		}
		if (outChannel != null) {
			// 清除临时文件
			deleteFile(f);
			// 关闭流
			outChannel.close();
			
			
		}
	
		
		File uploadFile = new File(filePath);
		FileInfo fileInfo = new FileInfo();
		long fileSize =  uploadFile.length();
		fileInfo.setFileName(fileName);
		fileInfo.setId(UUID.getUUID());

		fileInfo.setPath(filePath);
		fileInfo.setSize(fileSize);
		fileInfo.setType(fileType);
		fileInfo.setFileMd5(fileMd5);
		fileInfo.setUploadTime(new Date());
		fileInfo.setUrl(uploadUrl+"/"+fileName+"."+fileType);
		return fileInfo;
	}
	
	  /**
	   * 递归删除文件夹
	   * @param file
	   * @return
	   */
	   public static boolean deleteFile(File file) {
		   if(file==null||!file.exists()){
			   
			   return true;
		   }
	       if (file.isDirectory()) {
	           String[] children = file.list();//递归删除目录中的子目录下
	           for (int i=0; i<children.length; i++) {
	               boolean success = deleteFile(new File(file, children[i]));
	               if (!success) {
	                   return false;
	               }
	           }
	       }
	       // 目录此时为空，可以删除
	       return file.delete();
	   }
}
