package com.ward.ward_server.global.config;

import akka.http.scaladsl.Http;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("test")
@Configuration
public class S3MockConfig {
    private int port;

    @Bean(name = "s3Mock")
    public S3Mock s3Mock() {
        log.debug("s3 mock 빈 생성");
        S3Mock s3Mock = S3Mock.create(0, "s3mock");
        Http.ServerBinding binding = s3Mock.start();
        port = binding.localAddress().getPort();
        log.debug("port:{}", port);
        return s3Mock;
    }

    @Bean
    @DependsOn("s3Mock")
    public AmazonS3Client amazonS3() {
        log.debug("amazon client 빈 생성");
        AwsClientBuilder.EndpointConfiguration endpoint =
                new AwsClientBuilder.EndpointConfiguration(
                        "http://127.0.0.1:" + port, Regions.AP_NORTHEAST_2.name());
        AmazonS3Client client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
        client.createBucket("mock-bucket");
        return client;
    }
}
