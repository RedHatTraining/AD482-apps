package com.redhat.energy;

import java.util.Random;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.utils.Time;

/**
 * Emits (produces) generated power and wind measurements to Kafka
 */
public class Emitter<T> {

    private String topic;
    private Producer<Integer, T> powerProducer;

    public Emitter(String topic, Properties props) {
        this.topic = topic;
        powerProducer = new KafkaProducer<>(props);
    }

    public void emit(Integer turbineId, T value, Long timestamp) {
        produce(new ProducerRecord<>(
            topic,
            null,
            timestamp,
            turbineId,
            value
        ));
    }

    private void produce(ProducerRecord<Integer, T> record) {
        Callback callback = new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null) {
                    System.out.println(e);
                } else {
                    System.out.println("(" + topic + ") " + "Turbine: " + record.key() + ", Value:  " + record.value());
                }
            }
        };



        powerProducer.send(record, callback);

        // CompletableFuture.supplyAsync(() -> {

        //     Random r = new Random();
        //     if (r.nextInt(10) < 5) {
        //         System.out.println("Delay!");
        //         try {
        //             Thread.sleep(10000);
        //         } catch (InterruptedException e1) {
        //             e1.printStackTrace();
        //         }
        //     }

        //     powerProducer.send(record, callback);

        //     return null;
        // });
    }

}
