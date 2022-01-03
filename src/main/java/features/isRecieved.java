package features;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class isRecieved {
    public static void main(String  queueUrl, Path destination){
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_WEST_1)
                .build();
        Timer timer = new Timer();

        timer.schedule( new TimerTask() {
            public void run() {
                ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(5)
                        .build();
                List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
                if (messages.isEmpty())
                    System.out.println("No message found, retry in a minute");
                else
                    timer.cancel();
                    System.out.println("message found");
                    System.out.println("saving file from message");

                    Message message0 = messages.get(0);
                    String fileBucketName = message0.body();

                    Message message1 = messages.get(1);
                    String fileKey = message1.body();

                var getRequest = GetObjectRequest.builder()
                        .bucket(fileBucketName)
                        .key(fileKey)
                        .build();
                S3Client s3Client = S3Client.builder().build();
                s3Client.getObject(getRequest, ResponseTransformer.toFile(destination));

                }

        }, 0, 60*1000);



        System.out.println("deleting messages");
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .build();
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
                for (Message message : messages) {
                    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build();
                    sqsClient.deleteMessage(deleteMessageRequest);
                }




    }

    public static List<Message> getMessages(String queueUrl, SqsClient sqsClient){
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .build();
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
        return messages;
    }

}
