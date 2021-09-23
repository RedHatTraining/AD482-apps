package com.redhat.energy;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

/**
 * This class simulates a wind turbine reporting wind a power data
 */
public class Turbine {

    private Integer id;
    private Integer power;
    private Integer powerCapacity;

    public Turbine(Integer id, Integer powerCapacity) {
        this.id = id;
        Random r = new Random();
        this.power = r.nextInt(1000000) + 1000000;
        this.powerCapacity = powerCapacity;
    }

    public Integer getId() {
        return id;
    }

    public Multi<TurbineData> start() {
        return Multi
            .createFrom().ticks().every(Duration.ofSeconds(1))
            .map(tick -> produceData());
    }

    private TurbineData produceData() {
        Long timestamp = Instant.now().getEpochSecond() * 1000;

        Random r = new Random();
        if (r.nextInt(10) < 5) {
            timestamp -= 10000;
            System.out.println("Late!!");
        }

        return new TurbineData(
            id,
            producePowerMeasurement(),
            produceWindMeasurement(),
            timestamp
        );
    }

    private Integer producePowerMeasurement() {
        Random r = new Random();
        Integer delta = r.nextInt(200) - 100;

        Integer nextProduction = power + delta;

        if (nextProduction < 0) {
            power = 0;
        } else if (nextProduction > powerCapacity) {
            power = powerCapacity;
        } else {
            power = nextProduction;
        }

        return power;
    }

    private Integer produceWindMeasurement() {
        Random r = new Random();
        return power / 100000 + r.nextInt(10);
    }
}
