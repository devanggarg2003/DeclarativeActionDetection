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
        State s1 = new State(nfa, false, false, 2);
        s1.addInput(0, 0);
        s1.addInput(0, 0);
        s1.addEntityClassCondition(0, EntityType.CAR);
        s1.addEntityClassCondition(1, EntityType.CAR);
        s1.addSpatialCondition(SpatialRelation::isLeft, 0, 1);
        s1.addSpatialCondition(SpatialRelation::isLeft, 0, 1, true);
        s1.enableHolding();

        State s2 = new State(nfa, false, false, 2);
        s2.addInput(1, 0, 1);
        s2.addSpatialCondition(SpatialRelation::isIntersectInX, 0, 1);
        s2.addSpatialCondition(SpatialRelation::isIntersectInX, 0, 1, true);
        s2.enableHolding();

        State s3 = new State(nfa, false, true, 2);
        s3.addInput(2, 0, 1);
        s3.addSpatialCondition(SpatialRelation::isRight, 0, 1);
        s3.addSpatialCondition(SpatialRelation::isRight, 0, 1, true);
        s3.enableHolding();

        Kafka kf = new Kafka(nfa);
        try {
            kf.readStream();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}