package features;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HomePanel extends JFrame{
    public static String path = "";
    public static String bucketName ="";
    public static JLabel textLabelFile = new JLabel("<html>Please select a file<br/></html>");
    public static JLabel textLabelBucket = new JLabel("<html>Please select a bucket<br/><html>");
    public static JLabel textLabelPath = new JLabel("<html>Please select a destination<br/><html>");
    public static JLabel nameYourBucket = new JLabel("name your new bucket");
    public static JFrame hello = new JFrame("Bucket Creation");
    public static JPanel input = new JPanel();
    public static Path destination = new Path() {
        @Override
        public FileSystem getFileSystem() {
            return null;
        }

        @Override
        public boolean isAbsolute() {
            return false;
        }

        @Override
        public Path getRoot() {
            return null;
        }

        @Override
        public Path getFileName() {
            return null;
        }

        @Override
        public Path getParent() {
            return null;
        }

        @Override
        public int getNameCount() {
            return 0;
        }

        @Override
        public Path getName(int i) {
            return null;
        }

        @Override
        public Path subpath(int i, int i1) {
            return null;
        }

        @Override
        public boolean startsWith(Path path) {
            return false;
        }

        @Override
        public boolean endsWith(Path path) {
            return false;
        }

        @Override
        public Path normalize() {
            return null;
        }

        @Override
        public Path resolve(Path path) {
            return null;
        }

        @Override
        public Path relativize(Path path) {
            return null;
        }

        @Override
        public URI toUri() {
            return null;
        }

        @Override
        public Path toAbsolutePath() {
            return null;
        }

        @Override
        public Path toRealPath(LinkOption... linkOptions) throws IOException {
            return null;
        }

        @Override
        public WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kinds, WatchEvent.Modifier... modifiers) throws IOException {
            return null;
        }

        @Override
        public int compareTo(Path path) {
            return 0;
        }
    };

    //the UI of the client in swing
    public static void UI(){

        JFrame frame = new JFrame("Client Panel");
        JPanel mainPanel= new JPanel();

        mainPanel.setSize(300, 300);
        JPanel panelFile= new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JPanel panelBucket = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        frame.setSize(500,500);

        /*JTextField textFieldUserName = new JTextField(50);
        frame.add(textFieldUserName);*/
        JMenuBar menuBar = new JMenuBar();

        //kind of placholder
        panelFile.add(textLabelFile);
        panelFile.add(textLabelPath);
        panelBucket.add(textLabelBucket);
        mainPanel.add(panelFile);
        mainPanel.add(panelBucket);


        JMenu menuFile = new JMenu("File");
        JMenu s3Menu = new JMenu("S3");
        //run create a bucket
        JMenuItem s3Create = new JMenuItem(new AbstractAction("Create Bucket") {
            public void actionPerformed(ActionEvent ae) {



                input.setLayout(new GridBagLayout());
                input.setBorder(new EmptyBorder(50, 50, 50, 50));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                input.setSize(400, 200);
                JTextField inpuFieldt = new JTextField(10);
                inpuFieldt.setBorder(new EmptyBorder(150, 50, 50, 50));
                inpuFieldt.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                JButton create = new JButton(new AbstractAction("Create") {
                    public void actionPerformed(ActionEvent ae) {
                        String name = inpuFieldt.getText();


                        CreateBucket.main(name);



                    }
                });
                input.add(nameYourBucket, gbc);
                input.add(inpuFieldt, gbc);
                input.add(create);

                nameYourBucket.setLayout(new BorderLayout());
                nameYourBucket.setVisible(true);

                hello.add(input, BorderLayout.CENTER);
                hello.setSize(500,500);
                hello.setVisible(true);



            }
        });
        JMenuItem selectBucket = new JMenuItem(new AbstractAction("Select bucket") {
            //make a radio list of all buckets
            public void actionPerformed(ActionEvent ae) {
                S3ListBuckets.main();
            }
        });
        //create a button send file
        JMenuItem sendToBucket = new JMenuItem("Send File to Bucket");

        //allow to choose a destination path
        JMenuItem selectPath = new JMenuItem(new AbstractAction("Choose Path") {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser pathChooser = new JFileChooser();

                pathChooser.setDialogTitle("select a directory");
                pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                pathChooser.setAcceptAllFileFilterUsed(false);
                if (pathChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    Path p = pathChooser.getSelectedFile().toPath();
                    destination = p;

                }
                else {
                    System.out.println("No Selection ");
                }
                Path p = pathChooser.getSelectedFile().toPath();
                System.out.println("selected path: "+ p.toString());
                textLabelPath.removeAll();
                textLabelPath.setText(p.toString());
                frame.setVisible(true);
            }
        });

        //open a file chooser for CSV only
        JMenuItem menuFileChoose = new JMenuItem(new AbstractAction("Choose File") {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser file = new JFileChooser();
                file.setDialogTitle("select a file");
                FileFilter filter = new FileNameExtensionFilter("CSV File","csv");
                file.setFileFilter(filter);
                file.setMultiSelectionEnabled(true);
                file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                file.setFileHidingEnabled(false);
                if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    java.io.File f = file.getSelectedFile();
                    System.err.println(f.getPath());
                }
                java.io.File f = file.getSelectedFile();
                path = f.getPath();
                System.out.println("selected file: "+ path);
                textLabelFile.removeAll();
                textLabelFile.setText(path);

                frame.setVisible(true);

            }
        });
        menuFile.add(menuFileChoose);
        menuFile.add(selectPath);
        s3Menu.add(s3Create);
        s3Menu.add(selectBucket);
        s3Menu.add(sendToBucket);

        menuBar.add(menuFile);
        menuBar.add(s3Menu);
        menuBar.setBackground(Color.ORANGE);

//on click, make a progress bar, send file, send message and then lauch the timer in "isRecieved"
        JButton bsend = new JButton(new AbstractAction("Send") {
            public void actionPerformed(ActionEvent ae) {
                final JDialog dialog = new JDialog(frame, true); // modal
                dialog.setUndecorated(true);
                JProgressBar bar = new JProgressBar();
                bar.setIndeterminate(true);
                bar.setStringPainted(true);
                bar.setString("Sending file");
                dialog.add(bar);
                dialog.pack();
                SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
                {
                    @Override
                    protected Void doInBackground()
                    {
                        SendFile.main(path, bucketName);
                        bar.setString("Sending message");
                        dialog.add(bar);
                        dialog.pack();
                        SendMessage.main(path, bucketName);
                        isRecieved.main("outbox", destination);



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


            }
        });
        JButton bclear = new JButton(new AbstractAction("Clear") {
            public void actionPerformed(ActionEvent ae) {
                textLabelFile.setText("<html>Please select a file<br/></html>");
                textLabelBucket.setText("<html>Please select a bucket<br/><html>");
                textLabelPath.setText("<html>Please select a destination<br/><html>");
                frame.setVisible(true);
            }
        });
        panelButtons.add(bsend);
        panelButtons.add(bclear);
        mainPanel.add(panelButtons);


// adds menu bar to the frame
        frame.add(mainPanel);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

    }

    public static void main (String[] args) {
        UI();
    }
}
