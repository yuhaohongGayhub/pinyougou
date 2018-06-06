package com.pinyougou.manager.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {

    /** 初始化文件上传客户端 */
    static {
        String path = UploadController.class.getResource("/fastdfs_client.conf").getPath();
        try {
            /** 初始化客户端全局的对象 */
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Value("${fileServerUrl}")
    private String fastDfsServer;

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam(name = "file") MultipartFile multipartFile) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", 500);
        try {
            //获取扩展名
            String fileName = multipartFile.getOriginalFilename();
            String extension = FilenameUtils.getExtension(fileName);
            //文件上传客户端
            StorageClient client = new StorageClient();
            String[] ret = client.upload_file(multipartFile.getBytes(), extension, null);
            //返回的结果
            StringBuilder url = new StringBuilder(fastDfsServer);
            for (String str : ret) {
                url.append("/").append(str);
            }
            String path = url.toString();
            System.out.println("上传的图片访问地址为: " + path);
            data.put("status", 200);
            data.put("url", path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
