import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;





public class App extends Application {
   
    private Stage primaryStage;
    private Scene dashboardScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        DatabaseManager.createDatabase();

        // Create dashboard layout with category buttons
        Button pdfButton = new Button("PDF Documents");
        Button videoButton = new Button("Video Files");
        Button musicButton = new Button("Music Files");

        pdfButton.setOnAction(event -> showFileList("PDF"));
        videoButton.setOnAction(event -> showFileList("Video"));
        musicButton.setOnAction(event -> showFileList("Music"));

        VBox dashboardLayout = new VBox(15);
        dashboardLayout.getChildren().addAll(pdfButton, videoButton, musicButton);

        dashboardScene = new Scene(dashboardLayout, 300, 200);
        primaryStage.setTitle("Multimedia Document Organizer");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }

    private void showFileList(String type) {
        BorderPane fileListLayout = new BorderPane();
        ListView<FileInfo> fileListView = new ListView<>();

         

        
        // Add placeholder files based on type
        if (type.equals("PDF")) {
            List<FileInfo> files= DatabaseManager.retrieve(type);
            //fileListView.getItems().addAll("SamplePDF1.pdf", "SamplePDF2.pdf");
            fileListView.getItems().addAll(files);

        } else if (type.equals("Video")) {
            List<FileInfo> files= DatabaseManager.retrieve(type);
            //fileListView.getItems().addAll("SampleVideo1.mp4", "SampleVideo2.mp4");
            fileListView.getItems().addAll(files);
        } else if (type.equals("Music")) {
            List<FileInfo> files= DatabaseManager.retrieve(type);
            //fileListView.getItems().addAll("SampleMusic1.mp3", "SampleMusic2.mp3");
            fileListView.getItems().addAll(files);
        }

        // Create the "Upload" button to add files
        Button uploadButton = new Button("Upload " + type);
        uploadButton.setOnAction(event -> uploadFiles(type, fileListView));

        // Back button to return to dashboard
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(event -> primaryStage.setScene(dashboardScene));

        // Layout with list and buttons
        VBox buttonBox = new VBox(10, uploadButton, backButton);
        fileListLayout.setCenter(fileListView);
        fileListLayout.setBottom(buttonBox);

        Scene fileListScene = new Scene(fileListLayout, 400, 300);
        primaryStage.setScene(fileListScene);
    }

    // Method to upload files and add them to the list
    private void uploadFiles(String type, ListView<FileInfo> fileListView) {
        FileChooser fileChooser = new FileChooser();

        // Set file extension filters based on type
        if (type.equals("PDF")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        } else if (type.equals("Video")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi"));
        } else if (type.equals("Music")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));
        }

        // Allow multiple file selection
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
        
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                String fileName = file.getName();
                String filePath = file.getAbsolutePath();

                FileInfo fileInfo = new FileInfo(fileName, filePath);


                fileListView.getItems().add(fileInfo); // Add the file name to the list view
                // Here you can also store file path to the database for later access

                DatabaseManager.insertFile(fileName, type, filePath);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}

