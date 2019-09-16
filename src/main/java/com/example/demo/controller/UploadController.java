package com.example.demo.controller;

import com.example.demo.service.FileService;
import com.example.demo.util.MergeFile;
import com.example.demo.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.demo.util.IsAllUploaded.Uploaded;

@Controller
public class UploadController extends SaveFile {
   @Autowired
   private FileService fileService;

   @GetMapping("/")
   public String index(){
       return "/upload";
   }

    @ResponseBody
    @PostMapping(value = "/IsMD5Exist")
    public String bigFileUpload(String fileMd5, String fileName, String fileID) {

        try {
            boolean md5Exist = fileService.isMd5Exist(fileMd5);
            if (md5Exist) {
                return "this file is exist";
            } else {
                return "this file is not exist";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "this file is not exist";
        }
    }


    /**
     * @param guid             临时文件名
     * @param md5value         客户端生成md5值
     * @param chunks           分块数
     * @param chunk            分块序号
     * @param id               文件id便于区分
     * @param name             上传文件名
     * @param type             文件类型
     * @param lastModifiedDate 上次修改时间
     * @param size             文件大小
     * @param file             文件本身
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/BigFileUp")
    public String fileUpload(String guid,
                             String md5value,
                             String chunks,
                             String chunk,
                             String id,
                             String name,
                             String type,
                             String lastModifiedDate,
                             int size,
                             MultipartFile file) {

        String fileName;
        try {
            //当前分块
            int index;
            //获取上传的文件存放位置
            String uploadFolderPath = getRealPath();

            //加上guid唯一标识符区分用户文件
            String mergePath =uploadFolderPath + guid + "/";
            //获取文件后缀
            String ext = name.substring(name.lastIndexOf("."));

            //判断文件是否分块
            if (chunks != null && chunk != null) {
                index = Integer.parseInt(chunk);
                //将分块索引转为字符串文件名 0.jpg
                fileName = String.valueOf(index) + ext;
                // 将文件分块保存到临时文件夹里，便于之后的合并文件
                saveFile(mergePath, fileName, file);
                // 验证所有分块是否上传成功，成功的话进行合并
                Uploaded(name,md5value, guid, chunk, chunks, uploadFolderPath, fileName, ext, fileService);
            } else {
               // fileName = guid + ext;
                //上传文件没有分块的话就直接保存
                saveFile(uploadFolderPath, name, file);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "{\"error\":true}";
        }

        return "{jsonrpc = \"2.0\",id = id,filePath = \"/Upload/\" + fileFullName}";
    }

    @RequestMapping(value = "MergeFile")
    @ResponseBody
    public String mergeFile(String guid,
                            String md5value,
                            String chunks,
                            String name) throws Exception {
        String ext = name.substring(name.lastIndexOf("."));
        //获取上传的文件存放位置
        String uploadFolderPath = getRealPath();
        MergeFile.mergeFile(Integer.parseInt(chunks),ext,guid,uploadFolderPath,name);
        return null;
    }

}

