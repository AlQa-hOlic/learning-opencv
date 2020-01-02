package in.alqaholic.OpenCVJava.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import in.alqaholic.OpenCVJava.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {

    @FXML
    private BorderPane root;

    private Stage parent;

    @FXML
    private JFXComboBox<String> algorithm;

    @FXML
    private ImageView src_image_view;

    @FXML
    private ImageView dst_image_view;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton openBtn;

    private VideoCapture capture;
    private boolean isCameraOpen = false;
    private ScheduledExecutorService executorService;
    private Runnable frameGrabber = () -> {
        if (capture.isOpened()) {
            Mat frame = new Mat();
            capture.read(frame);

            if(!frame.empty()) {
                Utils.onFXThread(src_image_view.imageProperty(), Utils.mat2Image(frame));

                switch (algorithm.getValue()) {
                    case "Grayscale":
                        Imgproc.cvtColor(frame,frame, Imgproc.COLOR_BGR2GRAY);
                        Utils.onFXThread(dst_image_view.imageProperty(), Utils.mat2Image(frame));
                        break;
                    case "Transpose":
                        Core.transpose(frame,frame);
                        Utils.onFXThread(dst_image_view.imageProperty(), Utils.mat2Image(frame));
                        break;
                    case "Fourier Transform":
                        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);

                        int optimalDFTRows = Core.getOptimalDFTSize(frame.rows());
                        int optimalDFTCols = Core.getOptimalDFTSize(frame.cols());

                        Mat padded = new Mat();

                        Core.copyMakeBorder(frame, padded, 0, optimalDFTRows - frame.rows(), 0, optimalDFTCols - frame.cols(),Core.BORDER_CONSTANT, Scalar.all(0));
                        padded.convertTo(padded, CvType.CV_64F);

                        List<Mat> planes = new ArrayList<>();
                        planes.add(padded);
                        planes.add(Mat.zeros(padded.size(),CvType.CV_64F));
                        Mat complexI = new Mat();

                        Core.merge(planes, complexI);

                        Core.dft(complexI, complexI);

                        Core.split(complexI, planes);

                        Mat mag = new Mat();

                        Core.magnitude(planes.get(0), planes.get(1), mag);

                        Core.add(mag, Scalar.all(1), mag);
                        Core.log(mag, mag);

                        mag = mag.submat(new Rect(0,0, mag.cols() & -2, mag.rows() & -2));
                        int cx = frame.cols() / 2;
                        int cy = frame.rows() / 2;

                        Mat q0 = new Mat(mag, new Rect(0, 0, cx, cy));
                        Mat q1 = new Mat(mag, new Rect(cx, 0, cx, cy));
                        Mat q2 = new Mat(mag, new Rect(0, cy, cx, cy));
                        Mat q3 = new Mat(mag, new Rect(cx, cy, cx, cy));

                        Mat tmp = new Mat();
                        q0.copyTo(tmp);
                        q3.copyTo(q0);
                        tmp.copyTo(q3);

                        q1.copyTo(tmp);
                        q2.copyTo(q1);
                        tmp.copyTo(q2);

                        Core.normalize(mag, mag, 0, 255.0, Core.NORM_MINMAX);

                        mag.convertTo(mag, CvType.CV_8U);
                        Utils.onFXThread(dst_image_view.imageProperty(), Utils.mat2Image(new Mat(mag, new Rect(0,0,frame.cols(),frame.rows()))));
                        break;
                    case "HSV":
                        Imgproc.cvtColor(frame,frame, Imgproc.COLOR_BGR2HSV);
                        Utils.onFXThread(dst_image_view.imageProperty(), Utils.mat2Image(frame));
                        break;
                    default:
                        Utils.onFXThread(dst_image_view.imageProperty(), null);
                        break;
                }
            }
        }
    };

    public void setStage(Stage parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algorithm.getItems().addAll(
                "Grayscale",
                "Transpose",
                "Fourier Transform",
                "HSV"
        );
        algorithm.getSelectionModel().select(0);
        openBtn.setDisable(false);
        closeBtn.setDisable(true);
    }

    public void openCam(ActionEvent actionEvent) {
        if(!isCameraOpen) {
            capture = new VideoCapture();
            capture.open(0);
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
            isCameraOpen = true;
            openBtn.setDisable(true);
            closeBtn.setDisable(false);
        }
    }

    public void closeCam(ActionEvent actionEvent) {
        if (isCameraOpen) {
            try {
                executorService.shutdown();
                executorService.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            capture.release();
            isCameraOpen = false;
            openBtn.setDisable(false);
            closeBtn.setDisable(true);
        }
    }
}
