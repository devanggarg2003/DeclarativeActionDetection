package org.example;

import java.util.Vector;

@FunctionalInterface
public interface ForwardFunction {
    boolean apply(Vector<Entity> prevFrame, Vector<Entity> currFrame);
}
