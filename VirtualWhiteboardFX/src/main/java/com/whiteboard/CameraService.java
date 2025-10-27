package com.whiteboard;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

/**
 * Camera Service - Handles webcam capture and color detection
 */
public class CameraService {
    
    private VideoCapture camera;
    private static final int CAMERA_INDEX = 0;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    
    /**
     * Open camera and configure settings
     */
    public boolean openCamera() {
        try {
            camera = new VideoCapture(CAMERA_INDEX);
            
            if (!camera.isOpened()) {
                System.err.println("✗ Error: Could not open camera");
                return false;
            }
            
            // Configure camera properties
            camera.set(Videoio.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH);
            camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT);
            camera.set(Videoio.CAP_PROP_FPS, 30);
            
            System.out.println("✓ Camera opened successfully");
            return true;
        } catch (Exception e) {
            System.err.println("✗ Exception opening camera: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Capture a single frame from camera
     */
    public Mat captureFrame() {
        if (camera == null || !camera.isOpened()) {
            return null;
        }
        
        Mat frame = new Mat();
        if (camera.read(frame)) {
            return frame;
        }
        return null;
    }
    
    /**
     * Detect colored object in frame and return its center point
     */
    public Point detectColoredObject(Mat frame, ColorRange colorRange) {
        if (frame == null || frame.empty()) {
            return null;
        }
        
        // Convert BGR to HSV color space
        Mat hsvFrame = new Mat();
        Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);
        
        // Create mask for color range
        Mat mask = new Mat();
        Core.inRange(hsvFrame, colorRange.getLowerBound(), 
                     colorRange.getUpperBound(), mask);
        
        // Apply morphological operations to remove noise
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, 
                                                    new Size(5, 5));
        Imgproc.erode(mask, mask, kernel, new Point(-1, -1), 2);
        Imgproc.dilate(mask, mask, kernel, new Point(-1, -1), 2);
        
        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, 
                            Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        Point center = null;
        
        // Find the largest contour
        if (!contours.isEmpty()) {
            double maxArea = 0;
            MatOfPoint largestContour = null;
            
            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);
                if (area > maxArea && area > 500) { // Minimum area threshold
                    maxArea = area;
                    largestContour = contour;
                }
            }
            
            // Calculate center of largest contour
            if (largestContour != null) {
                Moments moments = Imgproc.moments(largestContour);
                if (moments.m00 != 0) {
                    int cx = (int) (moments.m10 / moments.m00);
                    int cy = (int) (moments.m01 / moments.m00);
                    center = new Point(cx, cy);
                }
            }
        }
        
        // Clean up
        hsvFrame.release();
        mask.release();
        kernel.release();
        hierarchy.release();
        for (MatOfPoint contour : contours) {
            contour.release();
        }
        
        return center;
    }
    
    /**
     * Convert OpenCV Mat to BufferedImage
     */
    public BufferedImage matToBufferedImage(Mat mat) {
        if (mat == null || mat.empty()) {
            return null;
        }
        
        int type = BufferedImage.TYPE_3BYTE_BGR;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }
        
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        
        return image;
    }
    
    /**
     * Release camera resources
     */
    public void releaseCamera() {
        if (camera != null && camera.isOpened()) {
            camera.release();
            System.out.println("✓ Camera released");
        }
    }
}