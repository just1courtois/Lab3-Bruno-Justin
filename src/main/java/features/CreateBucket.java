package features;

import java.util.Scanner;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import static features.HomePanel.*;

public class CreateBucket {



    public static void CreateBucket(S3Client s3Client, String bucketName) {
        try {
            S3Waiter s3Waiter = s3Client.waiter();
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.createBucket(bucketRequest);
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();


            // Wait until the bucket is created and print out the response
            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(bucketName +" is ready");
            nameYourBucket.setText(bucketName+ " has just been created");
            hello.setVisible(false);

        //update the placeholder
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            nameYourBucket.removeAll();
            nameYourBucket.setText("enter a valid name");
            input.setVisible(true);
            hello.setVisible(true);
        }
    }
    //not used
    public static String nameYourBucket() {
        String name = null;
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter a valid bucket name");
        name = input.nextLine();  // Read user input
        input.close();
        return name;
    }

    public static void main(String name) {
        Region region = Region.US_WEST_1;
        S3Client s3 = S3Client.builder().region(region).build();
        CreateBucket(s3, name);

    }
}
