package com.whiteboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

/**
 * Main Controller for Virtual Whiteboard UI
 * Manages camera feed, drawing, and user interactions
 */
public class MainController {

    @FXML private ImageView videoView;
    @FXML private Canvas drawingCanvas;
    @FXML private Button redButton;
    @FXML private Button greenButton;
    @FXML private Button blueButton;
    @FXML private Button yellowButton;
    @FXML private Button clearButton;
    @FXML private Button exitButton;
    @FXML private Label statusLabel;
    
    private CameraService cameraService;
    private DrawingCanvas drawingManager;
    private AnimationTimer timer;
    private Stage stage;
    private ColorRange currentColorRange;
    
    /**
     * Initialize controller
     */
    @FXML
    public void initialize() {
        // Initialize camera service
        cameraService = new CameraService();
        if (!cameraService.openCamera()) {
            statusLabel.setText("ERROR: Cannot open camera!");
            showError("Camera Error", "Failed to open camera. Please check if camera is connected.");
            return;
        }
        
        // Initialize drawing manager
        drawingManager = new DrawingCanvas(drawingCanvas);
        
        // Set default color to red
        currentColorRange = ColorRange.RED;
        
        // Setup button actions
        setupButtonActions();
        
        // Start video processing
        startVideoProcessing();
        
        statusLabel.setText("Ready - Select a color and move colored object to draw");
    }
    
    /**
     * Set stage reference
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Setup button click handlers
     */
    private void setupButtonActions() {
        redButton.setOnAction(e -> setColor(ColorRange.RED));
        greenButton.setOnAction(e -> setColor(ColorRange.GREEN));
        blueButton.setOnAction(e -> setColor(ColorRange.BLUE));
        yellowButton.setOnAction(e -> setColor(ColorRange.YELLOW));
        clearButton.setOnAction(e -> clearCanvas());
        exitButton.setOnAction(e -> shutdown());
    }
    
    /**
     * Start video processing loop
     */
    private void startVideoProcessing() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                processFrame();
            }
        };
        timer.start();
    }
    
    /**
     * Process each video frame
     */
    private void processFrame() {
        Mat frame = cameraService.captureFrame();
        if (frame == null || frame.empty()) {
            return;
        }
        
        // Flip frame for mirror effect
        org.opencv.core.Core.flip(frame, frame, 1);
        
        // Detect colored object
        Point center = cameraService.detectColoredObject(frame, currentColorRange);
        
        // Update drawing
        if (center != null) {
            drawingManager.addPoint(center, currentColorRange.getDrawColor());
            updateStatus("Drawing - Object detected at (" + 
                (int)center.x + ", " + (int)center.y + ")");
        } else {
            drawingManager.liftPen();
            updateStatus("Ready - Move " + currentColorRange.name().toLowerCase() + 
                " object in view to draw");
        }
        
        // Draw the canvas overlay
        drawingManager.render();
        
        // Convert and display frame
        BufferedImage bufferedImage = cameraService.matToBufferedImage(frame);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        videoView.setImage(image);
        
        // Clean up
        frame.release();
    }
    
    /**
     * Set color for detection and drawing
     */
    private void setColor(ColorRange color) {
        currentColorRange = color;
        drawingManager.clearCanvas();
        updateStatus("Color changed to " + color.name() + " - Canvas cleared");
        
        // Update button styles
        resetButtonStyles();
        switch (color) {
            case RED: redButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;"); break;
            case GREEN: greenButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;"); break;
            case BLUE: blueButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;"); break;
            case YELLOW: yellowButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-weight: bold;"); break;
        }
    }
    
    /**
     * Reset button styles
     */
    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 2;";
        redButton.setStyle(defaultStyle);
        greenButton.setStyle(defaultStyle);
        blueButton.setStyle(defaultStyle);
        yellowButton.setStyle(defaultStyle);
    }
    
    /**
     * Clear the drawing canvas
     */
    private void clearCanvas() {
        drawingManager.clearCanvas();
        updateStatus("Canvas cleared - Ready to draw");
    }
    
    /**
     * Update status label safely from any thread
     */
    private void updateStatus(String message) {
        Platform.runLater(() -> statusLabel.setText(message));
    }
    
    /**
     * Show error dialog
     */
    private void showError(String title, String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    /**
     * Shutdown application and release resources
     */
    public void shutdown() {
        if (timer != null) {
            timer.stop();
        }
        if (cameraService != null) {
            cameraService.releaseCamera();
        }
        Platform.exit();
        System.exit(0);
    }
}