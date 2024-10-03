package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;


public class NFA {
    public Map<Integer, Entity> currFrame; // Integer :- Entity_id
    public Vector<State> states;
    public NFA(){
        currFrame = new HashMap<>();
        states = new Vector<>();
        State startState = new State( this,true, false, 1);
    }

    public  void addState(State state){
        state.state_id = states.size();
        states.add(state);
//        return states.size() - 1;
    }

    private void helper_1(Integer index, Predicate predicate, Vector<Entity> prevEvent, Vector<Entity> currEvent, Set<Vector<Entity>> satisfyingEntities){
        if (index == predicate.nStates){
            if (predicate.forward(prevEvent, currEvent)){
                satisfyingEntities.add(new Vector<>(currEvent));
            }
            return;
        }
        State dataState = states.get(predicate.stateIds.get(index));
        Vector<Integer> requiredEntitiesIndex = predicate.objectIds.get(index);
        for (int i = 0; i < dataState.satisfyingEvents.size(); i++){
            boolean dataPresentInCurrFrame = true;
            for (Integer integer : requiredEntitiesIndex) {
                if (!currFrame.containsKey(dataState.satisfyingEvents.get(i).get(integer).entity_id)) {
                    dataPresentInCurrFrame = false;
                    break;
                }
            }
            if (!dataPresentInCurrFrame){
                continue;
            }
            for (Integer entitiesIndex : requiredEntitiesIndex) {
                prevEvent.add(dataState.satisfyingEvents.get(i).get(entitiesIndex));
                currEvent.add(currFrame.get(dataState.satisfyingEvents.get(i).get(entitiesIndex).entity_id));
            }
            helper_1(index + 1, predicate, prevEvent, currEvent, satisfyingEntities);
            for (Integer ignored : requiredEntitiesIndex) {
                prevEvent.removeLast();
                currEvent.removeLast();
            }
        }
    }

    public void process_NFA(){
        // for all states, create a map containing the corresponding set of new events that occurred in the current frame
        Map<Integer, Set<Vector<Entity>>> nextEvents = new HashMap<>();
        for (int i = 1; i < states.size(); i++) {
            State currState = states.get(i);
            Set<Vector<Entity>> satisfyingEntities = new HashSet<>();
            for (int j = 0; j < currState.predicates.size(); j++) {
                System.out.println("predicate no " + j);
                Predicate currPredicate = currState.predicates.get(j);
                Vector<Entity> prevEvent = new Vector<>();
                Vector<Entity> currEvent = new Vector<>();
                helper_1(0, currPredicate, prevEvent, currEvent, satisfyingEntities);
            }
            nextEvents.put(i, satisfyingEntities);
        }
        for (int i = 1; i < states.size(); i++){
            states.get(i).satisfyingEvents.clear();
            System.out.println(i + " " + nextEvents.get(i).size());
            for(Vector<Entity> event: nextEvents.get(i)) {
//                System.out.println("event size:" + event.size());
                states.get(i).satisfyingEvents.add(event);
            }
        }
//        System.out.println("NFA processed2");
    }
}
