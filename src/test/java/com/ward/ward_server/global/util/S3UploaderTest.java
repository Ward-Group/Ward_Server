package com.ward.ward_server.global.util;

import com.ward.ward_server.global.config.S3Config;
import com.ward.ward_server.global.config.S3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import(S3MockConfig.class)
@ContextConfiguration(classes=S3Uploader.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class S3UploaderTest {
    @Autowired
    private S3Mock s3Mock;
    @Autowired
    private S3Uploader s3Uploader;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }
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
        String url = s3Uploader.upload(multipartFile, "test");
        System.out.println(url);
    }
    @Test
    void upload() throws IOException {
        // given
        String path = "test.png";
        String contentType = "image/png";
        String dirName = "test";

        MockMultipartFile file = new MockMultipartFile("test", path, contentType, "test".getBytes());
        File file2 = ResourceUtils.getFile("classpath:test-image.jpeg");
        FileInputStream input = new FileInputStream(file2);
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                file2.getName(),
                "image/jpeg",
                input
        );
        // when
        String urlPath = s3Uploader.upload(multipartFile, dirName);

        // then
        assertThat(urlPath).contains(path);
        assertThat(urlPath).contains(dirName);
    }

}