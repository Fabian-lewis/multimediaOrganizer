import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

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
        Button wordButton = new Button("Word Document");
        Button excelButton = new Button("Excel Document");
        Button powerPointButton = new Button("PowerPoint Documents");
        powerPointButton.setWrapText(true);
        Button videoButton = new Button("Video Files");
        Button musicButton = new Button("Music Files");

        pdfButton.setOnAction(event -> showFileList("PDF"));
        videoButton.setOnAction(event -> showFileList("Video"));
        musicButton.setOnAction(event -> showFileList("Music"));
        wordButton.setOnAction(event->showFileList("Word Documents"));
        excelButton.setOnAction(event->showFileList("Excel Documents"));
        powerPointButton.setOnAction(event->showFileList("PowerPoint Documents"));


        VBox dashboardLayout = new VBox(15);
        dashboardLayout.getChildren().addAll(pdfButton, wordButton,excelButton,powerPointButton,videoButton, musicButton);

        dashboardScene = new Scene(dashboardLayout, 800, 600);
        dashboardScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Multimedia Document Organizer");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }

    private void showFileList(String type) {
        BorderPane fileListLayout = new BorderPane();
        //List<FileInfo> files= DatabaseManager.retrieve(type);
        ListView<FileInfo> fileListView = new ListView<>();

       
        
        // Add placeholder files based on type
        /*
        
         */
        if (type.equals("PDF")||type.equals("Word Documents")||type.equals("PowerPoint Documents")||type.equals("Excel Documents")) {
            List<FileInfo> files= DatabaseManager.retrieve(type);
            ObservableList<FileInfo> fileItems = FXCollections.observableArrayList(files);

            // Create a SortedList to sort the items alphabetically based on file name
            SortedList<FileInfo> sortedFiles = new SortedList<>(fileItems, (file1, file2) -> file1.getName().compareTo(file2.getName()));
            fileListView.setItems(sortedFiles);

        } else if (type.equals("Video")) {
            List<FileInfo> files= DatabaseManager.retrieve(type);
            ObservableList<FileInfo> fileItems = FXCollections.observableArrayList(files);

            // Create a SortedList to sort the items alphabetically based on file name
            SortedList<FileInfo> sortedFiles = new SortedList<>(fileItems, (file1, file2) -> file1.getName().compareTo(file2.getName()));
            fileListView.setItems(sortedFiles);
            //fileListView.getItems().addAll("SampleVideo1.mp4", "SampleVideo2.mp4");
            //fileListView.getItems().addAll(files);
        } else if (type.equals("Music")) {
            List<FileInfo> files= DatabaseManager.retrieve(type);
            ObservableList<FileInfo> fileItems = FXCollections.observableArrayList(files);

            // Create a SortedList to sort the items alphabetically based on file name
            SortedList<FileInfo> sortedFiles = new SortedList<>(fileItems, (file1, file2) -> file1.getName().compareTo(file2.getName()));
            fileListView.setItems(sortedFiles);
            //fileListView.getItems().addAll("SampleMusic1.mp3", "SampleMusic2.mp3");
            //fileListView.getItems().addAll(files);
        }
        //Create a listener to respond to item selections
        fileListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    if (newValue != null) {
        Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle("File Selected");
        infoDialog.setHeaderText("File Information");
        infoDialog.setContentText("Name: " + newValue.getName() + "\nPath: " + newValue.getPath());
        infoDialog.showAndWait();
        openFile(newValue);
    }
});
;
       

        // Create the "Upload" button to add files
        Button uploadButton = new Button("Upload " + type);
        uploadButton.setOnAction(event -> uploadFiles(type, fileListView));

        // Back button to return to dashboard
        Button backButton = new Button("Back to Dashboard");
        backButton.setWrapText(true);
        backButton.setOnAction(event -> primaryStage.setScene(dashboardScene));

        // Layout with list and buttons
        GridPane buttonBox = new GridPane();
        buttonBox.setHgap(10);
        GridPane.setConstraints(uploadButton, 0, 0);
        GridPane.setConstraints(backButton, 1, 0);
        buttonBox.getChildren().addAll(uploadButton,backButton);
        fileListLayout.setCenter(fileListView);
        fileListLayout.setBottom(buttonBox);

        Scene fileListScene = new Scene(fileListLayout, 800, 600);
        fileListScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(fileListScene);
    }

    // Method to upload files and add them to the list
    private void uploadFiles(String type, ListView<FileInfo> fileListView) {
        FileChooser fileChooser = new FileChooser();

        // Set file extension filters based on type
        if (type.equals("PDF")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"));
            //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Documents", "*.xls", "*.xlsx"));
            //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PowerPoint Documents", "*.ppt", "*.pptx"));
        } else if (type.equals("Video")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi"));
        } else if (type.equals("Music")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));
        }
        else if (type.equals("Word Documents")) {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"));
        }
        else if (type.equals("Excel Documents")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Documents", "*.xls", "*.xlsx"));
        }
        else if (type.equals("PowerPoint Documents")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PowerPoint Documents", "*.ppt", "*.pptx"));
        }

        // Allow multiple file selection
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
        
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                String fileName = file.getName();
                String filePath = file.getAbsolutePath();

                FileInfo fileInfo = new FileInfo(fileName, filePath, type);


                fileListView.getItems().add(fileInfo); // Add the file name to the list view
                // Here you can also store file path to the database for later access

                DatabaseManager.insertFile(fileName, type, filePath);
            }
        }
    }


    private void openFile(FileInfo  fileInfo) {

        String fileType = fileInfo.getType();
        String filePath = fileInfo.getPath();

        if(fileType.equals("PDF")||fileType.equals("Word Documents")||fileType.equals("PowerPoint Documents")||fileType.equals("Excel Documents")){
            openPDF(filePath);
        }
        else if (fileType.equals("Video") || fileType.equals("Music")) {
            openMedia(filePath, fileType);
        }
    }
    public void openPDF(String filePath){
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openMedia(String filePath, String fileType) {
    System.out.println("Opening Media.....");
    Stage mediaStage = new Stage();
    mediaStage.setTitle("MediaPlayer");

    // Create the media object and player
    Media media = new Media(new File(filePath).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);

    // Create the MediaView for video files
    MediaView mediaView = new MediaView(mediaPlayer);

    // StackPane layout for video and music
    StackPane mediaLayout = new StackPane(mediaView);

    // Get the file name from the path
    String fileName = new File(filePath).getName();

    // Label to display the file name
    Label fileNameLabel = new Label(fileName);
    fileNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
    fileNameLabel.setAlignment(Pos.CENTER);
    fileNameLabel.setMaxWidth(Double.MAX_VALUE);

    if (fileType.equals("Music")) {
        // Hide the video view for music files
        mediaView.setFitWidth(0);
        mediaView.setFitHeight(0);

        // Music playback controls (play, pause, stop, volume)
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Slider volumeSlider = new Slider(0, 1, 0.5); // Volume slider

        // Play button action
        playButton.setOnAction(event -> mediaPlayer.play());

        // Pause button action
        pauseButton.setOnAction(event -> mediaPlayer.pause());

        // Stop button action
        stopButton.setOnAction(event -> mediaPlayer.stop());

        // Volume slider action
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
            mediaPlayer.setVolume(newValue.doubleValue()));

        // Layout for music controls
        HBox musicControls = new HBox(10, playButton, pauseButton, stopButton, volumeSlider);
        musicControls.setAlignment(Pos.CENTER);
        musicControls.setSpacing(10);

        // Layout for music
        VBox musicLayout = new VBox(10, fileNameLabel, musicControls);
        musicLayout.setAlignment(Pos.CENTER);
        musicLayout.setSpacing(10);

        // Add the music layout
        mediaLayout.getChildren().add(musicLayout);
    } else if (fileType.equals("Video")) {
        // Video playback controls (play, pause, stop, volume)
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Slider volumeSlider = new Slider(0, 1, 0.5); // Volume slider

        // Play button action
        playButton.setOnAction(event -> mediaPlayer.play());

        // Pause button action
        pauseButton.setOnAction(event -> mediaPlayer.pause());

        // Stop button action
        stopButton.setOnAction(event -> mediaPlayer.stop());

        // Volume slider action
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
            mediaPlayer.setVolume(newValue.doubleValue()));

        // Layout for video controls
        HBox videoControls = new HBox(10, playButton, pauseButton, stopButton, volumeSlider);
        videoControls.setAlignment(Pos.CENTER);
        videoControls.setSpacing(10);

        // Layout for video
        VBox videoLayout = new VBox(10, fileNameLabel, videoControls);
        videoLayout.setAlignment(Pos.CENTER);
        videoLayout.setSpacing(10);

        // Add the video layout
        mediaLayout.getChildren().add(videoLayout);
    }

    // Create the scene with the media layout
    Scene mediaScene = new Scene(mediaLayout, 800, 600);
    mediaScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

    mediaStage.setScene(mediaScene);
    mediaStage.show();

    // Auto play the media
    mediaPlayer.setAutoPlay(true);
}




    public static void main(String[] args) {
        launch(args);
    }


}

