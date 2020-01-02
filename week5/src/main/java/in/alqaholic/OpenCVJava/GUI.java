package in.alqaholic.OpenCVJava;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import in.alqaholic.OpenCVJava.controllers.MainController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.File;

import static in.alqaholic.OpenCVJava.Utils.makeDraggable;

public class GUI extends Application {

    static {
        try {
            // pls make sure "opencv_java412.[ext]" exists and has the desired extension for your os
            //linux
//            System.load(new File(Core.NATIVE_LIBRARY_NAME + ".so").getAbsolutePath());
            //win
//            System.load(new File(Core.NATIVE_LIBRARY_NAME + ".dll").getAbsolutePath());
            //mac
            System.load(new File(Core.NATIVE_LIBRARY_NAME + ".dylib").getAbsolutePath());
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(
                JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                GUI.class.getResource("/css/main.css").toExternalForm()
        );
        primaryStage.setTitle("OpenCV Java");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
