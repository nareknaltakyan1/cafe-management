package com.sflpro.cafe.service.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.sflpro.cafe.config.CloudStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class AwsGateway {

    private final AmazonS3ClientBuilder amazonS3ClientBuilder;

    @Autowired
    public AwsGateway(CloudStorageConfig cloudStorageConfig) {
        CloudStorageConfig.Storage storageConfig = cloudStorageConfig.getStorage();
        amazonS3ClientBuilder =
            AmazonS3ClientBuilder
                .standard()
                .withCredentials(
                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(storageConfig.getAppKey(), storageConfig.getSecret()))
                )
                .withRegion(storageConfig.getRegion());
    }

    List<Bucket> getBuckets() {
        return createS3Client().listBuckets();
    }

    ObjectListing listObjects(String bucketName, String prefix) {
        return createS3Client().listObjects(bucketName, prefix + "/");
    }

    ObjectListing listObjects(String bucketName) {
        return createS3Client().listObjects(bucketName);
    }

    ObjectMetadata getObjectMetadata(String bucketName, String key) {
        return createS3Client().getObjectMetadata(bucketName, key);
    }

    public PutObjectResult uploadFile(
        String bucketName,
        String key,
        String contentType,
        InputStream inputStream,
        Map<String, String> metadata,
        Boolean personal
    ) {
        AmazonS3 s3Client = createS3Client();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        if (metadata != null) {
            for (String k : metadata.keySet()) {
                objectMetadata.addUserMetadata(k, metadata.get(key));
            }
        }

        CannedAccessControlList access = personal ? CannedAccessControlList.Private : CannedAccessControlList.PublicRead;
        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        request.withCannedAcl(access);

        return s3Client.putObject(request);
    }

    public URL getResourceUrl(String bucketName, String key) {
        return createS3Client().getUrl(bucketName, key);
    }

    private AmazonS3 createS3Client() {
        return amazonS3ClientBuilder.build();
    }

    public URL generatePreSingUrl(String bucketName, String key, Date expiration, HttpMethod method) {
        AmazonS3 s3Client = createS3Client();
        return s3Client.generatePresignedUrl(bucketName, key, expiration, method);
    }
}
