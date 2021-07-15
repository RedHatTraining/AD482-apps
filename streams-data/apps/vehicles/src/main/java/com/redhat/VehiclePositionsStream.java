package com.redhat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class VehiclePositionsStream {

    // Deserializer for message keys.
    private final Serde<String> keySerde = Serdes.String();

    // Serializer for message values
    ObjectMapperSerde<VehiclePosition> valueSerde = new ObjectMapperSerde<>(VehiclePosition.class);

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        // TODO: Create the stream from the "vehicle-positions" topic
        KStream<String, VehiclePosition> stream = builder.stream("vehicle-positions", Consumed.with(keySerde, valueSerde));

        // TODO: print stream values
        // stream.foreach((key, value) -> System.out.println("Received vehicle position: " + value));

        // TODO: map elevation to feet and send to "vehicle-feet-elevations" topic
        stream
            .map((key, value) -> {
                Double feet = value.elevation * 3.28084;
                return KeyValue.pair(value.id, feet);
            })
            .to("vehicle-feet-elevations", Produced.with(Serdes.Integer(), Serdes.Double()));

        // Explain windowing when aggregatting data
        KTable<Integer, Long> positionsCountByVehicle = stream
            .groupBy(
                (key, value) -> value.id,
                Grouped.with(Serdes.Integer(),valueSerde)
            )
            .count();

        // https://stackoverflow.com/questions/45314215/kafka-streams-explain-the-reason-why-ktable-and-its-associated-store-only-get
        // https://stackoverflow.com/questions/44711499/apache-kafka-streams-materializing-ktables-to-a-topic-seems-slow
        positionsCountByVehicle
            .toStream()
            .foreach((vehicleId, count) -> System.out.println("Vehicle: " + vehicleId + " Positions reported: " + count) );

        // // TODO: process the stream and send the result to the "large-payments" topic
        // stream
        //     .filter((key, amount) -> amount > 1000)
        //     .to("large-payments", Produced.with(keySerde, valueSerde));

        return builder.build();
    }
}
