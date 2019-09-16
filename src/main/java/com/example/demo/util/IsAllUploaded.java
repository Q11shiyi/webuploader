package com.example.demo.util;


import com.example.demo.entity.UploadFile;
import com.example.demo.entity.UploadInfo;
import com.example.demo.service.FileService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.util.MergeFile.mergeFile;

public class IsAllUploaded {

    private final static List<UploadInfo> uploadInfoList = new ArrayList<>();

    /**
     * @param md5
     * @param chunks
     * @return
     */
    public static boolean isAllUploaded(@NotNull final String md5,
                                        @NotNull final String chunks) {
        //遍历集合中该MD5值的元素
        int size = uploadInfoList.stream()
                .filter(item -> item.getMd5().equals(md5))
                .distinct()
                .collect(Collectors.toList())
                .size();
        boolean bool = (size == Integer.parseInt(chunks));//如果元素数量等于分块大小
        if (bool) {
            synchronized (uploadInfoList) {
                uploadInfoList.removeIf(item -> Objects.equals(item.getMd5(), md5));
            }
        }
        //全部上传完成
        return bool;
    }

    /**
     * @param md5         MD5
     * @param guid        随机生成的文件名
     * @param chunk       文件分块序号
     * @param chunks      文件分块数
     * @param fileName    文件名
     * @param ext         文件后缀名
     * @param fileService fileService
     */
    public static void Uploaded(@NotNull final String name,
                                @NotNull final String md5,
                                @NotNull final String guid,
                                @NotNull final String chunk,
                                @NotNull final String chunks,
                                @NotNull final String uploadFolderPath,
                                @NotNull final String fileName,
                                @NotNull final String ext,
                                @NotNull final FileService fileService)
            throws Exception {
        //每上传一个分片就添加到集合中
        synchronized (uploadInfoList) {
            uploadInfoList.add(new UploadInfo(md5, chunks, chunk, uploadFolderPath, fileName, ext));
        }
        //拿着该MD5值和分块总数去验证是否全部上传成功
        boolean allUploaded = isAllUploaded(md5, chunks);
        int chunksNumber = Integer.parseInt(chunks);

        if (allUploaded) {
            //合并上传文件
            mergeFile(chunksNumber, ext, guid, uploadFolderPath,name);
            //数据持久化
            fileService.save(new UploadFile(name, md5,uploadFolderPath, new Date() ));
        }
    }
}



