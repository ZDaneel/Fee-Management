package com.usts.feeback.controller;

import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * 文件处理
 *
 * @author leenadz
 * @since 2022-12-12 19:47
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.upload.path}")
    private String fileUploadPath;

    @SneakyThrows
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return "图片上传失败";
        }
        /*
         * 重命名并上传文件到根目录
         */
        String originalFilename = file.getOriginalFilename();
        System.out.println("originalFilename" + originalFilename);
        String type = FileUtil.extName(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + "." + type;
        String path = System.getProperty("user.dir") + fileUploadPath + "/" + fileName;
        file.transferTo(FileUtil.touch(path));
        return fileName;
    }
}
