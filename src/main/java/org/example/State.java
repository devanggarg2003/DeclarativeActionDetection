package org.example;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Queue;

public class State {
    public int state_id;
    public boolean isStartState;
    public boolean isAcceptingState;
    public int nObjects;

    public Queue<Vector<Entity>> satisfyingEvents;

    public State(int state_id, boolean isStartState, boolean isAcceptingState, int nObjects){
        this.state_id = state_id;
        this.isStartState = isStartState;
        this.isAcceptingState = isAcceptingState;
        this.nObjects = nObjects;
        this.satisfyingEvents = new LinkedList<>();
    }
}
