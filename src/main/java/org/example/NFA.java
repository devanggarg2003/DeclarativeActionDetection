package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;


public class NFA {
    public Map<Integer, Entity> currFrame; // Integer :- Entity_id
    public Vector<State> states;
//    public Vector<Predicate> predicates;
//    public Map<Integer, Vector<Integer>> statePredicates;

    public NFA(){
        currFrame = new HashMap<>();
//        statePredicates = new HashMap<>();
        states = new Vector<>();
//        predicates = new Vector<>();
        State startState = new State(0, true, false, 1);
        states.add(startState);
    }

    private void helper_1(Integer index, Predicate predicate, Vector<Entity> prevEvent, Vector<Entity> currEvent, Set<Vector<Entity>> satisfyingEntities){
//        System.out.println("reached first\n");
        if (index == predicate.nStates){
//            System.out.println("reached here\n");
            if (predicate.forward(prevEvent, currEvent)){
                satisfyingEntities.add(currEvent);
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
        System.out.println("reached NFA\n");
        // for all states, create a map containing the corresponding set of new events that occurred in the current frame
        Map<Integer, Set<Vector<Entity>>> nextEvents = new HashMap<>();
        for (int i = 1; i < states.size(); i++) {
            State currState = states.get(i);
            Set<Vector<Entity>> satisfyingEntities = new HashSet<>();
            for (int j = 0; j < currState.predicates.size(); j++) {
                System.out.println("predicate:" + j);
                Predicate currPredicate = currState.predicates.get(j);
                Vector<Entity> prevEvent = new Vector<>();
                Vector<Entity> currEvent = new Vector<>();
                helper_1(0, currPredicate, prevEvent, currEvent, satisfyingEntities);
            }
            nextEvents.put(i, satisfyingEntities);
        }

        for (int i = 1; i < states.size(); i++){
            // add the new events from the set for the corresponding state
            states.get(i).satisfyingEvents.clear();
            System.out.println("state :" + i + "<----> Satisfying Events: " + nextEvents.get(i).size());
            for(Vector<Entity> event: nextEvents.get(i)) {
                states.get(i).satisfyingEvents.add(event);
//                if (states.get(i).isAcceptingState) {
//                    System.out.println(event.firstElement().entity_class + " <--> " + event.lastElement().entity_class);
//                }
            }
            System.out.println("-------\n");
        }
    }
}
