package org.example;

import org.example.queryBuilder.EntityType;
import org.example.queryBuilder.SpatialRelation;
import org.example.stream.Kafka;

import java.util.Vector;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        NFA nfa = new NFA();
        State s1 = new State(nfa.states.size(), false, false, 2);
        SpatialRelation spatialRelation = new SpatialRelation();
        Predicate p1 = new Predicate(s1.predicates.size(), 2, (prevEvent, currEvent) -> {
            if(currEvent.get(0).entity_class != 2 || currEvent.get(1).entity_class != 2) {
                return false;
            }
            return spatialRelation.isLeft(currEvent.get(0), currEvent.get(1));
        });
        p1.nStates = 2;
        p1.stateIds = new Vector<>();
        p1.stateIds.add(0);
        p1.stateIds.add(0);
        p1.objectIds = new Vector<>();
        p1.objectIds.add(new Vector<>());
        p1.objectIds.add(new Vector<>());
        p1.objectIds.get(0).add(0);
        p1.objectIds.get(1).add(0);
        nfa.states.add(s1);
        s1.predicates.add(p1);
        nfa.states.get(1).isAcceptingState = true;
        Kafka kf = new Kafka(nfa);
        try {
            kf.readStream();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}