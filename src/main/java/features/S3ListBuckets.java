package features;

//import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static features.HomePanel.bucketName;
import static features.HomePanel.textLabelBucket;

public class S3ListBuckets {

    public static void main(String[]... args) {

        //Region region = Region.US_EAST_1;

        JFrame selectBucket = new JFrame("select a bucket");
        selectBucket.setLayout(new GridLayout(2,1));
        selectBucket.setSize(500,500);
        JDialog modalDialog = new JDialog(selectBucket, "loading", Dialog.ModalityType.DOCUMENT_MODAL);
        ButtonGroup bgroup = new ButtonGroup();
        JPanel selectionBucketPanel = new JPanel();
        selectionBucketPanel.setLayout(new GridLayout(10, 1));
        final JDialog dialog = new JDialog(selectBucket, true); // modal
        dialog.setUndecorated(true);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setStringPainted(true);
        bar.setString("Retreiving buckets");
        dialog.add(bar);
        dialog.pack();
        SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
        {
            @Override
            protected Void doInBackground()
            {
                Region region = Region.US_WEST_1;
                S3Client s3 = S3Client.builder().region(region).build(); //.region(region)

                ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
                        .build();
                ListBucketsResponse listBucketResponse = s3.listBuckets(listBucketsRequest);
                List<String> bucketNames = new ArrayList<String>();
                listBucketResponse.buckets().stream()
                        .forEach(x -> bucketNames.add(x.name()));

                //System.out.println(bucketNames);
                //List<JRadioButton> buttonList = new ArrayList<JRadioButton>();

                int i = 0;
                for (String name : bucketNames){
                    System.out.println(name);
                    JRadioButton button = new JRadioButton(name);
                    bgroup.add(button);
                    button.setActionCommand(name);
                    selectionBucketPanel.add(button);
                }
                return null;
            }

            @Override
            protected void done()
            {
                dialog.dispose();
            }
        };
        worker.execute();
        dialog.setVisible(true);





        JButton okButton = new JButton("select");
        JPanel selectPanel = new JPanel();

        selectPanel.add(okButton);
        selectPanel.setSize(200, 200);
        selectPanel.setVisible(true);
        selectBucket.add(selectionBucketPanel);
        selectBucket.add(selectPanel);
        selectBucket.setVisible(true);
        okButton.addActionListener(e ->
        {
            String selection = bgroup.getSelection().getActionCommand();
            System.out.println(bgroup.getSelection().getActionCommand());
            bucketName = selection;
            textLabelBucket.removeAll();
            textLabelBucket.setText(selection);
            selectBucket.dispose();
        });


    }
}