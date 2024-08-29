package org.example;

import org.example.stream.Kafka;

import java.util.Vector;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        NFA nfa = new NFA();
        State startState = new State(nfa.states.size(), true, false, 2);
        nfa.states.add(startState);

        Predicate beforeX = new Predicate(0, 2, (prevEvent, currEvent) -> {
            return currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) / 13 < 1;
        });

        Predicate beforeY = new Predicate(1, 2, (prevEvent, currEvent) -> {
            return currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) % 13 < 1;
        });

        Predicate intersectX = new Predicate(2, 2, (prevEvent, currEvent) -> {
            return currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) / 13 >= 1 &&
                    currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) / 13 < 12;
        });

        Predicate intersectY = new Predicate(3, 2, (prevEvent, currEvent) -> {
            return currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) % 13 >= 1 &&
                    currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) % 13 < 12;
        });

        Predicate afterX = new Predicate(4, 2, (prevEvent, currEvent) -> {
            return currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) / 13 == 12;
        });

        Predicate afterY = new Predicate(5, 2, (prevEvent, currEvent) -> {
            return currEvent.get(0).boundingBox.topological_relation(currEvent.get(1).boundingBox) % 13 == 12;
        });

        for (int i = 0; i < 6; i++){
            State state = new State(nfa.states.size(), false, false, 2);
            nfa.states.add(state);
        }
        nfa.states.get(5).isAcceptingState = true;
        nfa.states.get(6).isAcceptingState = true;
        nfa.predicates.add(beforeX);
        nfa.predicates.add(beforeY);
        nfa.predicates.add(intersectX);
        nfa.predicates.add(intersectY);
        nfa.predicates.add(afterX);
        nfa.predicates.add(afterY);

        nfa.statePredicates.put(0, new Vector<>());
        Pair<Integer, Integer> pair1 = new Pair<>(0, 1);
        Pair<Integer, Integer> pair2 = new Pair<>(1, 2);
        nfa.statePredicates.get(0).add(pair1);
        nfa.statePredicates.get(0).add(pair2);

        nfa.statePredicates.put(1, new Vector<>());
        Pair<Integer, Integer> pair3 = new Pair<>(2, 3);
        nfa.statePredicates.get(1).add(pair1);
        nfa.statePredicates.get(1).add(pair3);

        nfa.statePredicates.put(2, new Vector<>());
        Pair<Integer, Integer> pair4 = new Pair<>(3, 4);
        nfa.statePredicates.get(2).add(pair2);
        nfa.statePredicates.get(2).add(pair4);

        nfa.statePredicates.put(3, new Vector<>());
        Pair<Integer, Integer> pair5 = new Pair<>(4, 5);
        nfa.statePredicates.get(3).add(pair1);
        nfa.statePredicates.get(3).add(pair3);
        nfa.statePredicates.get(3).add(pair5);

        nfa.statePredicates.put(4, new Vector<>());
        Pair<Integer, Integer> pair6 = new Pair<>(5, 6);
        nfa.statePredicates.get(4).add(pair2);
        nfa.statePredicates.get(4).add(pair4);
        nfa.statePredicates.get(4).add(pair6);

        nfa.statePredicates.put(5, new Vector<>());
        nfa.statePredicates.put(6, new Vector<>());


        Kafka kf = new Kafka(nfa);
        try {
            kf.readStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}