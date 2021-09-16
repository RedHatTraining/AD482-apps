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
import org.apache.kafka.streams.kstream.*;
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


        // TODO: Create a KStream for the earning events
        KStream<Integer, WindTurbineEarningWasAdded> earningsStream = builder.stream(
            WIND_TURBINE_EARNINGS_TOPIC,
            Consumed.with(Serdes.Integer(), earningEventSerde)
        );

        // TODO: Aggregate the earnings
        KTable<Integer, AverageData> aggregatedEarnings = earningsStream
        .groupByKey()
        .aggregate(
            AverageData::new,
            (key, value, aggregate) -> {
                aggregate.increaseCount(1);
                aggregate.increaseSum(value.amount);

                return aggregate;
            },
            Materialized.<Integer, AverageData, KeyValueStore<Bytes, byte[]>>
                            as(AGGREGATED_EARNINGS_STORE)
                .withKeySerde(Serdes.Integer())
                .withValueSerde(averageDataSerde)
        );

        // TODO: Calculate the average earnings
        KTable<Integer, Double> averageEarningsTable = aggregatedEarnings.mapValues(
            value -> value.sum / value.count,
            Materialized.<Integer, Double, KeyValueStore<Bytes, byte[]>>
                            as(AVERAGE_EARNINGS_STORE)
                .withKeySerde(Serdes.Integer())
                .withValueSerde(Serdes.Double())
        );

        // TODO: Create a KStream for the expense events
        KStream<Integer, WindTurbineExpenseWasAdded> expensesStream = builder.stream(
            WIND_TURBINE_EXPENSES_TOPIC,
            Consumed.with(Serdes.Integer(), expenseEventSerde)
        );

        // TODO: Calculate the average expenses
        KTable<Integer, AverageData> aggregatedExpenses = expensesStream
        .groupByKey()
        .aggregate(
            AverageData::new,
            (key, value, aggregate) -> {
                aggregate.increaseCount(1);
                aggregate.increaseSum(value.amount);

                return aggregate;
            },
            Materialized.<Integer, AverageData, KeyValueStore<Bytes, byte[]>>
                            as(AGGREGATED_EXPENSES_STORE)
                .withKeySerde(Serdes.Integer())
                .withValueSerde(averageDataSerde)
        );

        // TODO: Calculate the average expenses
        KTable<Integer, Double> averageExpensesTable = aggregatedExpenses
        .mapValues(
            value -> value.sum / value.count,
            Materialized.<Integer, Double, KeyValueStore<Bytes, byte[]>>
                            as(AVERAGE_EXPENSES_STORE)
                .withKeySerde(Serdes.Integer())
                .withValueSerde(Serdes.Double())
        );

        // TODO: Calculate the profit margins
        averageEarningsTable.join(
            averageExpensesTable,
            WindTurbineProfitMarginWasCalculated::new
        ).toStream()
        .to(
            WIND_TURBINE_PROFIT_MARGINS_TOPIC,
            Produced.with(Serdes.Integer(), profitEventsSerde)
        );

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
