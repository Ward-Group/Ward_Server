package com.ward.ward_server.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorMessage.FILE_CONVERT_FAIL;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3ImageManager {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.cloudFront.domain}")
    private String cloudFrontDomain;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new ApiException(INVALID_INPUT, FILE_CONVERT_FAIL.getMessage()));
        log.debug("uploadFile name: {}", uploadFile.getName());
        String filePath = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, filePath);
        uploadFile.delete();
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return cloudFrontDomain + "/" + fileName;
    }

    public String getUrl(String fileName) {
        return cloudFrontDomain + "/" + fileName;
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        log.debug("convertFile name: {}", convertFile.getName());
        if (convertFile.createNewFile()) {
            log.debug("create new file");
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
        }
        return Optional.of(convertFile);
    }

    public void delete(String url) {
        if (url == null) return;
        String fileOriginName = extractFileOriginName(url);
        log.debug("delete file origin name: {}", fileOriginName);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileOriginName));
    }

    private String extractFileOriginName(String url) {
        return url.substring(cloudFrontDomain.length() + 1);
    }
}
