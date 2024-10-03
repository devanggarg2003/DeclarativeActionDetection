package org.example.queryBuilder;

import org.example.BoundingBox;
import org.example.Entity;

public class SpatialRelation {
    final static int BEFORE = 0;
    final static int MEETS_LEFT = 1;
    final static int OVERLAPS_LEFT = 2;
    final static int STARTS_BEFORE = 3;
    final static int CONTAINS = 4;
    final static int ENDS_BEFORE = 5;
    final static int EQUALS = 6;
    final static int ENDS_AFTER = 7;
    final static int INSIDE = 8;
    final static int STARTS_AFTER = 9;
    final static int OVERLAPS_RIGHT = 10;
    final static int MEETS_RIGHT = 11;
    final static int AFTER = 12;

    static int findRelation_1d(double minX1, double maxX1, double minX2, double maxX2) {
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
    public static int topological_relation(BoundingBox b1, BoundingBox b2){
        int x_pos = findRelation_1d(b1.minX,b1.maxX, b2.minX, b2.maxX);
        int y_pos = findRelation_1d(b1.minY, b1.maxY, b2.minY, b2.maxY);
        return x_pos*13 + y_pos;
    }

    public static boolean isLeft(Entity entity1, Entity entity2) {
        return topological_relation(entity1.boundingBox, entity2.boundingBox) / 13 < 1;
    }

    public static boolean isDown(Entity entity1, Entity entity2) {
        return topological_relation(entity1.boundingBox, entity2.boundingBox) % 13 < 1;
    }

    public static boolean isRight(Entity entity1, Entity entity2) {
        return topological_relation(entity1.boundingBox, entity2.boundingBox) / 13 == 12;
    }

    public static boolean isUp(Entity entity1, Entity entity2) {
        return topological_relation(entity1.boundingBox, entity2.boundingBox) % 13 == 12;
    }

    public static boolean isIntersectInX(Entity entity1, Entity entity2) {
        return topological_relation(entity1.boundingBox, entity2.boundingBox) / 13 >= 1 &&
                    topological_relation(entity1.boundingBox, entity2.boundingBox) / 13 < 12;
    }

    public static boolean isIntersectInY(Entity entity1, Entity entity2) {
        return topological_relation(entity1.boundingBox, entity2.boundingBox) % 13 >= 1 &&
                topological_relation(entity1.boundingBox, entity2.boundingBox) % 13 < 12;
    }


}
