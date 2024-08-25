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
        for (int i = 0; i < frame_history.size(); i++) {
            nfa.currFrame.put(frame_history.get(i).entity_id, frame_history.get(i));
        }
        for (int i = 0; i < frame_history.size(); i++){ // Main hardcoded area
            for (int j = 0; j < frame_history.size(); j++){
                if (i != j) {
                    Vector<Entity> newEvent = new Vector<>();
                    newEvent.add(frame_history.get(i));
                    newEvent.add(frame_history.get(j));
                    nfa.states.getFirst().satisfyingEvents.add(newEvent);
                }
            }
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
                    int obj_id   = Integer.parseInt(values[1]);
                    int obj_class = Integer.parseInt(values[2]);
                    String color = values[3];

                    float xmin = Float.parseFloat(values[4]);
                    float ymin = Float.parseFloat(values[5]);
                    float xmax = Float.parseFloat(values[6]);
                    float ymax = Float.parseFloat(values[7]);

                    // this will store the current entity
                    BoundingBox objectBox = new BoundingBox(xmin, ymin, xmax, ymax);
                    Entity entity = new Entity(frame_id, obj_id, obj_class, objectBox);

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
}

