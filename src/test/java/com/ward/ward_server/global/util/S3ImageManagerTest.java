package com.ward.ward_server.global.util;

import com.ward.ward_server.global.config.S3MockConfig;
import io.findify.s3mock.S3Mock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        S3ImageManager.class,
        S3MockConfig.class
})
class S3ImageManagerTest {
    @Autowired
    private S3Mock s3Mock;
    @Autowired
    private S3ImageManager s3ImageManager;

    @AfterEach
    public void tearDown() {
        log.debug("s3mock server stop");
        s3Mock.stop();
    }

    @Test
    void 이미지_업로드() throws IOException {
        // given
        String dirName = "main";
        String mockBucketName = "mock-bucket";
        String mockCloudFrontDomain = "https://mock.cloudfront.net";
        File file = ResourceUtils.getFile("classpath:test-image.png");
        MultipartFile multipartFile = new MockMultipartFile(
                "test",
                file.getName(),
                "image/png",
                new FileInputStream(file)
        );
        ReflectionTestUtils.setField(s3ImageManager, "bucket", mockBucketName);
        ReflectionTestUtils.setField(s3ImageManager, "cloudFrontDomain", mockCloudFrontDomain);

        // when
        String urlPath = s3ImageManager.upload(multipartFile, dirName);

        // then
        assertThat(urlPath).contains(mockCloudFrontDomain);
        assertThat(urlPath).contains(dirName);
        assertThat(urlPath).contains(file.getName());
    }
}