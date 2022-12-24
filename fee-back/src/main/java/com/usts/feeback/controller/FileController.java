package com.usts.feeback.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.usts.feeback.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
        file.transferTo(FileUtil.touch(path));
        /*
         * 如果是拍照的图片 竖着的在压缩时会自动旋转 目前不做处理 后续优化
         *
         */
        File imageFile = new File(path);
        long size = FileUtil.size(imageFile);
        ImgUtil.scale(FileUtil.file(path), FileUtil.file(path), 0.8f);
        return Result.success(fileName);
    }
}
