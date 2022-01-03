package features;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;

import java.io.File;

public class SendFile {
    public static void main(String  path, String bucket){
        File file = new File(path);
        String key = file.getName();
    Region region = Region.US_WEST_1;
    S3Client s3 = S3Client.builder().region(region).build();
    PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket).key(key)
            .build();

        s3.putObject(objectRequest, RequestBody.fromFile(file));
    // s3.putObject(bucketName,"Desktop/tests3.txt", new File("/Users/bruno/Desktop/tests3.txt");
        System.out.println("uploaded");

    }
}
