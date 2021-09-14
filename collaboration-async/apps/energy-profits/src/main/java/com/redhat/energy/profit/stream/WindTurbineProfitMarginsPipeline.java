package com.redhat.energy.profit.stream;

import com.redhat.energy.profit.event.WindTurbineEarningWasAdded;
import com.redhat.energy.profit.event.WindTurbineExpenseWasAdded;
import com.redhat.energy.profit.event.WindTurbineProfitMarginWasCalculated;
import com.redhat.energy.profit.model.AverageData;
import com.redhat.energy.profit.stream.common.StreamProcessor;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class WindTurbineProfitMarginsPipeline extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(WindTurbineProfitMarginsPipeline.class);

    // Reading topics
    static final String WIND_TURBINE_EARNINGS_TOPIC = "wind-turbine-earnings";
    static final String WIND_TURBINE_EXPENSES_TOPIC = "wind-turbine-expenses";

    // Writing topics
    static final String WIND_TURBINE_PROFIT_MARGINS_TOPIC = "wind-turbine-profit-margins";

    // State stores
    static final String AGGREGATED_EARNINGS_STORE = "earnings-aggregated-store";
    static final String AVERAGE_EARNINGS_STORE = "wind-turbine-average-earnings-store";
    static final String AGGREGATED_EXPENSES_STORE = "expenses-aggregated-store";
    static final String AVERAGE_EXPENSES_STORE = "wind-turbine-average-expenses-store";

    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<WindTurbineEarningWasAdded> earningEventSerde
                = new ObjectMapperSerde<>(WindTurbineEarningWasAdded.class);

        ObjectMapperSerde<WindTurbineExpenseWasAdded> expenseEventSerde
                = new ObjectMapperSerde<>(WindTurbineExpenseWasAdded.class);

        ObjectMapperSerde<AverageData> averageDataSerde
                = new ObjectMapperSerde<>(AverageData.class);

        ObjectMapperSerde<WindTurbineProfitMarginWasCalculated> profitEventsSerde
                = new ObjectMapperSerde<>(WindTurbineProfitMarginWasCalculated.class);


        // TODO: Build the stream topology for the earnings

        // TODO: Build the stream topology for the expenses

        // TODO: Build the stream topology for the profit margins

        streams = new KafkaStreams(
            builder.build(),
            generateStreamConfig()
        );

        // Starting from a clean state
        streams.cleanUp();
        streams.start();
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        streams.close();
    }
}
