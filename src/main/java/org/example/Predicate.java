package org.example;
import java.util.Vector;


public class Predicate {
    public int predicate_id;
    public int nStates;
    public int nObjects;
    public Vector<Integer> stateIds;
    public Vector<Vector<Integer>> objectIds;
    private final ForwardFunction forwardFunction;

    public Predicate(int predicate_id, int nObjects, ForwardFunction forwardFunction) {
        this.nObjects = nObjects;
        this.predicate_id = predicate_id;
        this.forwardFunction = forwardFunction;
    }

    public boolean forward(Vector<Entity> prevEvent, Vector<Entity> currEvent) {
        return forwardFunction.apply(prevEvent, currEvent);
    }
}