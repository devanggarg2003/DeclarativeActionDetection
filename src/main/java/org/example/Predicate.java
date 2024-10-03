package org.example;
import java.util.Vector;


public class Predicate {
    public int predicate_id;
    public int nStates;
    public int nObjects;
    public Vector<Integer> stateIds;
    public Vector<Vector<Integer>> objectIds;
    private final Vector<ForwardFunction> extraConditions = new Vector<>();

    public Predicate(int predicate_id, int nObjects) {
        this.nObjects = nObjects;
        this.predicate_id = predicate_id;
        this.nStates = 0;
        this.stateIds = new Vector<>();
        this.objectIds = new Vector<>();
    }

    public void addExtraCondition(ForwardFunction condition) {
        extraConditions.add(condition);
    }

    public boolean forward(Vector<Entity> prevEvent, Vector<Entity> currEvent) {
        for (ForwardFunction condition : extraConditions) {
            if (!condition.apply(prevEvent, currEvent)) {
                return false;  // If any extra condition fails, return false
            }
        }
        return true;
    }
}