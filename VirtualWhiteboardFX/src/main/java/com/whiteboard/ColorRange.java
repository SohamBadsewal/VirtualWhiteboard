package com.whiteboard;

import org.opencv.core.Scalar;

/**
 * Color Range definitions for HSV color detection
 * Defines lower and upper bounds for different colors in HSV color space
 */
public enum ColorRange {
    
    RED(
        new Scalar(0, 120, 70),      // Lower bound (H, S, V)
        new Scalar(10, 255, 255),    // Upper bound
        new Scalar(0, 0, 255)        // Draw color (BGR: Red)
    ),
    
    GREEN(
        new Scalar(40, 50, 50),      // Lower bound
        new Scalar(80, 255, 255),    // Upper bound
        new Scalar(0, 255, 0)        // Draw color (BGR: Green)
    ),
    
    BLUE(
        new Scalar(100, 100, 100),   // Lower bound
        new Scalar(130, 255, 255),   // Upper bound
        new Scalar(255, 0, 0)        // Draw color (BGR: Blue)
    ),
    
    YELLOW(
        new Scalar(20, 100, 100),    // Lower bound
        new Scalar(30, 255, 255),    // Upper bound
        new Scalar(0, 255, 255)      // Draw color (BGR: Yellow)
    );
    
    private final Scalar lowerBound;
    private final Scalar upperBound;
    private final Scalar drawColor;
    
    ColorRange(Scalar lowerBound, Scalar upperBound, Scalar drawColor) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.drawColor = drawColor;
    }
    
    public Scalar getLowerBound() {
        return lowerBound;
    }
    
    public Scalar getUpperBound() {
        return upperBound;
    }
    
    public Scalar getDrawColor() {
        return drawColor;
    }
}