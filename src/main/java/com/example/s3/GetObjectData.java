/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/

package com.example.s3;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.net.URI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * To run this AWS code example, ensure that you have setup your development environment, including your AWS credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */

public class GetObjectData {

    public static void main(String[] args) {

     final String USAGE = "\n" +
                "Usage:\n" +
                "    GetObjectData <bucketName> <keyName> <path>\n\n" +
                "Where:\n" +
                "    bucketName - the Amazon S3 bucket name. \n\n"+
                "    keyName - the key name. \n\n"+
                "    path - the path where the file is written to. \n\n";

        if (args.length != 3) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bucketName = args[0];
        String keyName = args[1];
        String path = args[2];

        URI uri = URI.create("https://play.min.io");
        AwsCredentialsProvider provider = StaticCredentialsProvider
            .create(AwsBasicCredentials.create("Q3AM3UQ867SPQQA43P2F",
                                               "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG"));
        S3Configuration confBuilder = S3Configuration.builder().pathStyleAccessEnabled(true).build();
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder()
            .credentialsProvider(provider)
            .endpointOverride(uri)
            .serviceConfiguration(confBuilder)
            .region(region)
            .build();

        getObjectBytes(s3, bucketName, keyName, path);
        s3.close();
        printObjectMeta(s3, bucketName, keyName);
    }

    public static void printObjectMeta (S3Client s3, String bucketName, String keyName) {
        try {
            HeadObjectRequest objectRequest = HeadObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();
            HeadObjectResponse response = s3.headObject(objectRequest);
            System.out.println("Content-Type: " + response.contentType());
            System.out.println("ETag: "+ response.eTag());
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void getObjectBytes (S3Client s3, String bucketName, String keyName, String path ) {

        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();

            // Write the data to a local file
            File myFile = new File(path );
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            System.out.println("Successfully obtained bytes from an S3 object");
            os.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
