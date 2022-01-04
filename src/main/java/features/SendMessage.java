package features;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.io.File;

public class SendMessage {
    public static void main(String path, String bucket){

        SqsClient sqs = SqsClient.builder()
                .region(Region.US_WEST_1)
                .build();
        String message;


        message = bucket + " "+ path;
        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName("inbox")
                .build();
        CreateQueueResponse createResult = sqs.createQueue(createQueueRequest);
        GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                .queueName("inbox")
                .build();

        String queueUrl = sqs.getQueueUrl(getQueueRequest).queueUrl();

        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .delaySeconds(5)
                .build();
        sqs.sendMessage(sendMsgRequest);
        System.out.println("message sent to inbox");
    }
}
