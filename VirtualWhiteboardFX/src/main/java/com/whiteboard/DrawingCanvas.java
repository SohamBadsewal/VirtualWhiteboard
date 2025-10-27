package com.whiteboard;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

/**
 * Drawing Canvas Manager
 * Handles drawing operations on JavaFX Canvas
 */
public class DrawingCanvas {
    
    private final Canvas canvas;
    private final GraphicsContext gc;
    private Point previousPoint;
    private Color currentColor;
    
    /**
     * Constructor
     */
    public DrawingCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.previousPoint = null;
        this.currentColor = Color.RED;
        
        // Set drawing properties
        gc.setLineWidth(5);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        gc.setLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        
        // Fill the background with white initially
        clearCanvas();
    }
    
    /**
     * Add a point and draw line from previous point
     */
    public void addPoint(Point point, Scalar bgrColor) {
        if (point == null) {
            liftPen();
            return;
        }
        
        // Convert BGR to JavaFX Color
        currentColor = Color.rgb(
            (int) bgrColor.val[2], // Red
            (int) bgrColor.val[1], // Green
            (int) bgrColor.val[0]  // Blue
        );
        
        // Draw line if we have a previous point
        if (previousPoint != null) {
            gc.setStroke(currentColor);
            gc.strokeLine(previousPoint.x, previousPoint.y, point.x, point.y);
        } else {
            // First point - draw a small dot
            gc.setFill(currentColor);
            gc.fillOval(point.x - 2, point.y - 2, 4, 4);
        }
        
        previousPoint = new Point(point.x, point.y);
    }
    
    /**
     * Lift pen - break the line
     */
    public void liftPen() {
        previousPoint = null;
    }
    
    /**
     * Clear the entire canvas with a white background
     */
    public void clearCanvas() {
        // Fill background with white instead of transparent
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        previousPoint = null;
    }
    
    /**
     * Render method (placeholder for future enhancements)
     */
    public void render() {
        // Currently, drawing happens immediately in addPoint()
    }
    
    /**
     * Get current drawing color
     */
    public Color getCurrentColor() {
        return currentColor;
    }
    
    /**
     * Set line width
     */
    public void setLineWidth(double width) {
        gc.setLineWidth(width);
    }
}
