package org.example;
import java.util.Vector;

public class State {
    public int state_id;
    public boolean isStartState;
    public boolean isAcceptingState;
    public int nObjects;
    public Vector<Predicate> predicates;
    public Vector<Vector<Entity>> satisfyingEvents;

    public State(int state_id, boolean isStartState, boolean isAcceptingState, int nObjects){
        this.state_id = state_id;
        this.isStartState = isStartState;
        this.isAcceptingState = isAcceptingState;
        this.nObjects = nObjects;
        this.satisfyingEvents = new Vector<>();
        this.predicates = new Vector<>();
    }
}
