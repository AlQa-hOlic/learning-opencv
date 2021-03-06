package in.alqaholic.OpenCVJava.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import in.alqaholic.OpenCVJava.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static in.alqaholic.OpenCVJava.Utils.mat2Image;

public class MainController implements Initializable {
    private final WritableImage ERROR_IMAGE = new WritableImage(48,48);

    private Stage parent;

    @FXML
    private BorderPane root;

    @FXML
    private JFXTextField inputFile;

    @FXML
    private ImageView image;


    public MainController() {
        for (int i = 0; i < 48 * 48; i++) {
            ERROR_IMAGE.getPixelWriter().setColor(i%48, i/48, Color.web("#607d8b"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputFile.setText("No file Selected!");
        setErrorImage();
    }

    private void setErrorImage() {
        image.setImage(ERROR_IMAGE);
    }


    public void openImage(ActionEvent actionEvent) throws MalformedURLException {
        File file = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", List.of("*.jpg", "*.jpeg", "*.png", "*.tiff")));
        file = fileChooser.showOpenDialog(parent);

        if(file == null || !file.exists()) {
            inputFile.setText("No file Selected!");
            image.setImage(ERROR_IMAGE);
        }
        else {
            inputFile.setText(file.getAbsolutePath());
            image.setImage(new Image(file.toURI().toURL().toExternalForm()));
        }
    }

    public void showImage(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Mat src = Imgcodecs.imread(inputFile.getText(), Imgcodecs.IMREAD_GRAYSCALE);

        stage.setScene(new Scene(new Pane(new ImageView(mat2Image(src)))));
        stage.setTitle("Original");
        stage.showAndWait();
    }

    public void transposeImage(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Mat src = Imgcodecs.imread(inputFile.getText(), Imgcodecs.IMREAD_GRAYSCALE);
        Core.transpose(src,src);
        stage.setScene(new Scene(new Pane(new ImageView(mat2Image(src)))));
        stage.setTitle("Transpose");
        stage.showAndWait();
    }


    public void invertImage(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setScene(new Scene(new Pane(new ImageView(Utils.invertImage(inputFile.getText())))));
        stage.setTitle("Inverted");
        stage.showAndWait();
    }

    public void visualizeFourier(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setScene(new Scene(new Pane(new ImageView(Utils.visualizeFourier(inputFile.getText())))));
        stage.setTitle("Fourier");
        stage.showAndWait();
    }

    public void setStage(Stage parent) {
        this.parent = parent;
    }
}
