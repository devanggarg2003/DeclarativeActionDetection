package org.example.stream;

import org.example.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class Kafka {

    // a variable to store the last frame id that was processed
    private int last_frame_id = 0;
    NFA nfa;

    public Kafka(NFA nfa){
        this.nfa = nfa;

    }

    // a function to process the frame
    public void processFrame(Vector<Entity> frame_history) {
        // generate all the pair of events from frame history
        // and then process them
        nfa.currFrame.clear();
        for (Entity value : frame_history) {
            nfa.currFrame.put(value.entity_id, value);
        }
        System.out.println(last_frame_id + " : frame id, elements detected: " + frame_history.size());
        for (Entity entity : frame_history) {
            Vector<Entity> newEvent = new Vector<>();
            newEvent.add(entity);
            nfa.states.getFirst().satisfyingEvents.add(newEvent);
        }
        nfa.process_NFA();
    }

    public void readStream() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        String topic = "events"; // specify the topic here
        consumer.subscribe(Collections.singletonList(topic));

//        // this will store all the Entities in the frame
        Vector<Entity> frame_history = new Vector<>();

        try {
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String value = record.value();
                    value = value.substring(1, value.length() - 1);
                    String[] values = value.split(", ");
                    int frame_id = Integer.parseInt(values[0]);
                    Entity entity = getEntity(values, frame_id);
                    System.out.println(frame_id + " : " + entity.entity_id);
                    if (frame_history.isEmpty()) {
                        frame_history.add(entity);
                        last_frame_id = frame_id;
                    }
                    else {
                        if (frame_id == last_frame_id) {
                            frame_history.add(entity);
                        }
                        else {
                            // process the frame
                            processFrame(frame_history);
                            frame_history.clear();
                            frame_history.add(entity);
                            last_frame_id = frame_id;
                        }
                    }
                }
            }
        }
        finally {consumer.close();}
    }

    private static Entity getEntity(String[] values, int frame_id) {
        int obj_id   = Integer.parseInt(values[1]);
        int obj_class = Integer.parseInt(values[2]);
//        String color = values[3];

        float xMin = Float.parseFloat(values[4]);
        float yMin = Float.parseFloat(values[5]);
        float xMax = Float.parseFloat(values[6]);
        float yMax = Float.parseFloat(values[7]);

        // this will store the current entity
        BoundingBox objectBox = new BoundingBox(xMin, yMin, xMax, yMax);
        Entity entity;
        entity = new Entity(frame_id, obj_id, obj_class, objectBox);
        return entity;
    }
}

