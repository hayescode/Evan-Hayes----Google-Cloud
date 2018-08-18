package com.evanhayes.evanhayes.models.Images;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class s3client {
    public static AmazonS3 s3() {
        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAJP5L52ZHKKTREDIQ",
                "oB3EHGViri/bjUK8fvlCVKwZWyHIShGoSxjp3Fra"
        );
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
        return s3client;
    }
}
