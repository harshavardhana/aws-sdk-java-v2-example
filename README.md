# MinIO Example App

This project contains an AWS Lambda maven application with [AWS Java SDK 2.x](https://github.com/aws/aws-sdk-java-v2) dependencies.

## Prerequisites
- Java 1.8+
- Apache Maven

## Development

The generated function handler class just returns the input. The configured AWS Java SDK client is created in `DependencyFactory` class and you can
add the code to interact with the SDK client based on your use case.

#### Building the project
```
mvn compile exec:java -Dexec.mainClass="com.example.s3.GetObjectData"

Usage:
    GetObjectData <bucketName> <keyName> <path>

Where:
    bucketName - the Amazon S3 bucket name.

    keyName - the key name.

    path - the path where the file is written to.
```

```
mvn compile exec:java -Dexec.mainClass="com.example.s3.GetObjectData" \
    -Dexec.args="testbucket hosts local-hosts"
```
