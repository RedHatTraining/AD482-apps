package com.redhat.customers.producer;

import com.redhat.customers.event.PotentialCustomersWereDetected;
import io.quarkus.scheduler.Scheduled;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedList;

@ApplicationScoped
public class PotentialCustomerDetector extends LateProducer {

    private static final Logger LOG = Logger.getLogger(PotentialCustomerDetector.class);
    private static final String AREA = "front-area";

    // Writing topic
    private static final String POTENTIAL_CUSTOMERS_TOPIC = "potential-customers-detected";

    private CircularIterator<Integer> measures;

    private Producer<String, PotentialCustomersWereDetected> producer;

    private int measuresCounter = 0;

    @PostConstruct
    public void init() {
        measures = generateCircularList().iterator();
        producer = new KafkaProducer<>(
                generateConfig()
        );
    }

    @Scheduled(every="1s")
    public void generateEvent() {
        int currentMeasure = measures.next();
        long timestamp = System.currentTimeMillis();
        String status = "on time";

        if (measuresCounter % 11 == 0) {
            status = "late";
            timestamp = timestamp - 11000;
        }

        long window = (timestamp / 10000) * 10000;

        LOG.infov("Customers - Measure: {0}, Window {1} ({2})" ,
                currentMeasure,
                String.valueOf(window),
                status
        );

        measuresCounter++;

        producer.send(
            new ProducerRecord<>(
                    POTENTIAL_CUSTOMERS_TOPIC,
                    null,
                    timestamp,
                    AREA,
                    new PotentialCustomersWereDetected(
                            AREA,
                            currentMeasure
                    )
            )
        );
    }

    private CircularList<Integer> generateCircularList() {
        int[] measures = new int[] {0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 34, 21, 13, 8, 5, 3, 2, 1, 0};
        LinkedList<Integer> linkedList = new LinkedList<>();

        for (int measure : measures) {
            linkedList.add(measure);
        }

        return new CircularList<>(linkedList);
    }
}
