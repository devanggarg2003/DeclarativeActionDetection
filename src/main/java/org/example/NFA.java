package org.example;
import java.util.*;


public class NFA {
    public Map<Integer, Entity> currFrame; // Integer :- Entity_id
    public Vector<State> states;
    public Vector<Predicate> predicates;
    public Map<Integer, Vector<Pair<Integer,Integer>>> statePredicates;

    public NFA(){
        currFrame = new HashMap<>();
        statePredicates = new HashMap<>();
        states = new Vector<>();
        predicates = new Vector<>();
    }

    public void process_NFA(){
        // for all states, create a map containing the corresponding set of new events that occurred in the current frame
        Map<Integer, Set<Vector<Entity>>> nextEvents = new HashMap<>();
        for(int i = 0; i < states.size(); i++) {
            nextEvents.put(i, new HashSet<>());
        }

        for (int i = 0; i < states.size(); i++){
            State currState = states.get(i);
            while (!currState.satisfyingEvents.isEmpty()){
                Vector<Entity> prevEvent = currState.satisfyingEvents.poll();
                Vector<Entity> currEvent = new Vector<>();
                boolean allEntityPresent = true;
                for(Entity entity : prevEvent){
                    if (!currFrame.containsKey(entity.entity_id)){
                        allEntityPresent = false;
                        break;
                    }
                    currEvent.add(currFrame.get(entity.entity_id));
                }
                if (!allEntityPresent){
                    continue;
                }
                Vector<Pair<Integer, Integer>> possibleTransitions = statePredicates.get(i);
                for (int j = 0; j < possibleTransitions.size(); j++){
                    boolean isTransit = predicates.get(possibleTransitions.get(j).first).forward(prevEvent, currEvent);
                    if (isTransit){
                        // add the new event in the set for the new state fetched from the map
                        Integer nextState = possibleTransitions.get(j).second;
                        nextEvents.get(nextState).add(currEvent);
                    }
                }
            }
        }

        for (int i = 0; i < states.size(); i++){
            // add the new events from the set for the corresponding state
            for(Vector<Entity> event: nextEvents.get(i)) {
                states.get(i).satisfyingEvents.add(event);
                if (states.get(i).isAcceptingState){
                    System.out.println(event.firstElement() + " <--> " +  event.lastElement());
                }
            }

        }
    }
}
