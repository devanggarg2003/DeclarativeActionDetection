package org.example;
import org.example.queryBuilder.EntityType;

import java.util.Vector;
import java.util.function.BiFunction;

public class State {
    public int state_id;
    public boolean isStartState;
    public boolean isAcceptingState;
    public int nObjects;
    int lastPredicateIndex;
    public Vector<Predicate> predicates;
    public Vector<Vector<Entity>> satisfyingEvents;
    public boolean isHolding;

    public State(NFA nfa, boolean isStartState, boolean isAcceptingState, int nObjects){
        this.state_id = -1;
        this.isStartState = isStartState;
        this.isAcceptingState = isAcceptingState;
        this.nObjects = nObjects;
        this.satisfyingEvents = new Vector<>();
        this.predicates = new Vector<>();
        Predicate initialPredicate = new Predicate(0, this.nObjects);
        predicates.add(initialPredicate);
        Predicate predicate = new Predicate(1, this.nObjects);
        predicates.add(predicate);
        this.lastPredicateIndex = 1;
        this.isHolding = false;
        nfa.addState(this);
    }

    public void addNewInputSource(){
        Predicate initialPredicate = new Predicate(0, this.nObjects);
        predicates.add(initialPredicate);
        this.lastPredicateIndex = predicates.size() - 1;
    }

    public void addInput(int sourceStateId, int... entities){
        predicates.get(lastPredicateIndex).nStates += 1;
        predicates.get(lastPredicateIndex).stateIds.add(sourceStateId);
        Vector<Integer> entitiesVector = new Vector<>();
        for (int entity : entities) {
            entitiesVector.add(entity);
        }
        predicates.get(lastPredicateIndex).objectIds.add(entitiesVector);
    }

    public void addEntityClassCondition(int entityIndex, EntityType entityType){
        predicates.get(lastPredicateIndex).addExtraCondition(((prevFrame, currFrame) -> {
            return currFrame.get(entityIndex).entity_class == entityType.getValue();
        }));
    }

    public void addSpatialCondition(BiFunction<Entity, Entity, Boolean> spatialRelationFunc, int entityIndex1, int entityIndex2) {
        predicates.get(lastPredicateIndex).addExtraCondition(((prevFrame, currFrame) -> {
            return spatialRelationFunc.apply(currFrame.get(entityIndex1), currFrame.get(entityIndex2));
        }));
    }
    public void addSpatialCondition(BiFunction<Entity, Entity, Boolean> spatialRelationFunc, int entityIndex1, int entityIndex2, boolean holdingCondition) {
        predicates.firstElement().addExtraCondition(((prevFrame, currFrame) -> {
            return spatialRelationFunc.apply(currFrame.get(entityIndex1), currFrame.get(entityIndex2));
        }));
    }

    public void enableHolding(){
        if (isHolding){
            return;
        }
        isHolding = true;
        predicates.firstElement().nStates += 1;
        predicates.firstElement().stateIds.add(this.state_id);
        Vector<Integer> entitiesVector = new Vector<>();
        for (int i = 0; i < this.nObjects; i++) {
            entitiesVector.add(i);
        }
        predicates.firstElement().objectIds.add(entitiesVector);
    }
}
