package com.ward.ward_server.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("test")
public class S3MockConfig {
    @Bean
    public S3Mock s3Mock() {
        System.out.println("s3 mock 빈 생성");
        S3Mock s3Mock= new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
        s3Mock.start();
        return s3Mock;
    }

    @Primary
    @Bean
    public AmazonS3Client amazonS3() {
        System.out.println("amazon s3 빈 생성");
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://127.0.0.1:8001", Regions.AP_NORTHEAST_2.name());
        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();

    }
}
