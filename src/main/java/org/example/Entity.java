package org.example;

import org.example.queryBuilder.EntityType;

public class Entity {
    public Integer frame_id;
    public Integer entity_id;
    public Integer entity_class;
    public BoundingBox boundingBox;

    public void processObject(EntityType objectType) {
        System.out.println("Processing: " + objectType + " with value: " + objectType.getValue());
    }

    public Entity(int frame_id, int entity_id, int entity_class, BoundingBox boundingBox){
        this.frame_id = frame_id;
        this.entity_class = entity_class;
        this.entity_id = entity_id;
        this.boundingBox = boundingBox;
    }
}
