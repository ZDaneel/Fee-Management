package com.usts.feeback.controller;

import cn.hutool.core.io.FileUtil;
import com.usts.feeback.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * 文件处理
 *
 * @author leenadz
 * @since 2022-12-12 19:47
 */
@Api(tags = "文件信息接口")
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${file.upload.path}")
    private String fileUploadPath;

    @SneakyThrows
    @ApiOperation("图片上传并保存到服务器中")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("图片上传失败");
        }
        /*
         * 重命名并上传文件到根目录
         */
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + "." + type;
        String path = fileUploadPath + fileName;
        System.out.println(path);
        file.transferTo(FileUtil.touch(path));
        return Result.success(fileName);
    }

    @ApiOperation(value = "登陆页面使用图片")
    @GetMapping("/login-image")
    public Result<String> queryLoginImage() {
        String fileName = "bb39991acccf4937823b0e344683cdbe.JPG";
        return Result.success(fileName);
    }
}
