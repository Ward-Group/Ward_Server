package com.ward.ward_server.global.util;

import com.ward.ward_server.global.config.QuerydslConfig;
import com.ward.ward_server.global.config.S3Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@Import(S3Config.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = S3Uploader.class)
class S3UploaderTest {
    @Autowired
    private S3Uploader s3Uploader;

    @Test
    void 이미지_업로드() throws IOException {
        File file = ResourceUtils.getFile("classpath:test-image.jpeg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                file.getName(),
                "image/jpeg",
                input
        );
        String url=s3Uploader.upload(multipartFile, "test");
        System.out.println(url);
    }
}