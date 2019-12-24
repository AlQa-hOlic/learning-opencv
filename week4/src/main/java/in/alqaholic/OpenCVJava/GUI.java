package in.alqaholic.OpenCVJava;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import in.alqaholic.OpenCVJava.controllers.MainController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.File;

import static in.alqaholic.OpenCVJava.Utils.makeDraggable;

public class GUI extends Application {

    static {
        try {
            // pls make sure "lib/opencv_java412.[ext]" exists and has the desired extension for your os
            //linux
//            System.load(new File("lib" + System.getProperty("file.separator") + Core.NATIVE_LIBRARY_NAME + ".so").getAbsolutePath());
            //win
//            System.load(new File("lib" + System.getProperty("file.separator") + Core.NATIVE_LIBRARY_NAME + ".dll").getAbsolutePath());
            //mac
            System.load(new File("lib" + System.getProperty("file.separator") + Core.NATIVE_LIBRARY_NAME + ".dylib").getAbsolutePath());
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

        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        decorator.setCustomMaximize(true);
        decorator.setStyle("-jfx-");

        Scene scene = new Scene(decorator);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                JFoenixResources.load("/css/main.css").toExternalForm());
        makeDraggable(primaryStage, decorator);
        primaryStage.setTitle("OpenCV Java");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
