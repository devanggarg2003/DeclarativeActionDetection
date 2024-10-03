package org.example;

public class BoundingBox { // class for bounding box and related operations with that
    public double minX, minY, maxX, maxY;

    public BoundingBox(double minX, double minY, double maxX, double maxY) {
        if (minX > maxX || minY > maxY) {
            throw new IllegalArgumentException("Minimum values must be less than or equal to maximum values. ");
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }


}
