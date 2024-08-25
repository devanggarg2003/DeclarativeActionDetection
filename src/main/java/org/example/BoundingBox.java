package org.example;

public class BoundingBox { // class for bounding box and related operations with that
    double minX, minY, maxX, maxY;

    public BoundingBox(double minX, double minY, double maxX, double maxY) {
        if (minX > maxX || minY > maxY) {
            throw new IllegalArgumentException("Minimum values must be less than or equal to maximum values. ");
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    final int BEFORE = 0;
    final int MEETS_LEFT = 1;
    final int OVERLAPS_LEFT = 2;
    final int STARTS_BEFORE = 3;
    final int CONTAINS = 4;
    final int ENDS_BEFORE = 5;
    final int EQUALS = 6;
    final int ENDS_AFTER = 7;
    final int INSIDE = 8;
    final int STARTS_AFTER = 9;
    final int OVERLAPS_RIGHT = 10;

    final int MEETS_RIGHT = 11;
    final int AFTER = 12;

    int findRelation_1d(double minX1, double maxX1, double minX2, double maxX2) {
        if (minX1 < minX2) {
            if (maxX1 < minX2) {
                return BEFORE;
            } else if (maxX1 == minX2) {
                return MEETS_LEFT;
            } else if (maxX1 < maxX2) {
                return OVERLAPS_LEFT;
            } else if (maxX1 == maxX2) {
                return STARTS_BEFORE;
            } else {
                return CONTAINS;
            }
        } else if (minX1 == minX2) {
            if (maxX1 < maxX2) {
                return ENDS_BEFORE;
            } else if (maxX1 == maxX2) {
                return EQUALS;
            } else {
                return ENDS_AFTER;
            }
        } else if (minX1 < maxX2) {
            if (maxX1 < maxX2) {
                return INSIDE;
            } else if (maxX1 == maxX2) {
                return STARTS_AFTER;
            } else {
                return OVERLAPS_RIGHT;
            }
        } else if (minX1 == maxX2) {
            return MEETS_RIGHT;
        } else {
            return AFTER;
        }
    }
    public int topological_relation(BoundingBox b2){
        int x_pos = findRelation_1d(minX, maxX, b2.minX, b2.maxX);
        int y_pos = findRelation_1d(minY, maxY, b2.minY, b2.maxY);
        return x_pos*13 + y_pos;
    }

}
