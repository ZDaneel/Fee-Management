package com.usts.feeback;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @author leenadz
 * @since 2022-12-12 22:49
 */
public class FileTest {
    @SneakyThrows
    public static void main(String[] args) {
        Resource resource = new ClassPathResource("");
        System.out.println(resource.getFile().getAbsolutePath());
    }
}
