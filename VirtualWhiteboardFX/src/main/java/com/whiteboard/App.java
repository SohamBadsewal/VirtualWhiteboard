package com.whiteboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 * Main Application Entry Point
 * Loads OpenCV and launches JavaFX application
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        
        // Get controller and set stage reference
        MainController controller = loader.getController();
        controller.setStage(primaryStage);
        
        // Configure scene
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Virtual Whiteboard - Computer Vision Drawing");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            controller.shutdown();
        });
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Load OpenCV native library
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("✓ OpenCV loaded successfully!");
            System.out.println("✓ OpenCV Version: " + Core.VERSION);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("✗ Error: Could not load OpenCV library");
            System.err.println("Make sure opencv_java4xx.dll/.so/.dylib is in the lib folder");
            System.err.println("Add VM argument: -Djava.library.path=lib");
            e.printStackTrace();
            System.exit(1);
        }
        
        // Launch JavaFX application
        launch(args);
    }
}
