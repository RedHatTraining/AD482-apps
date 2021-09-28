import React, { useEffect, useState } from 'react';
import { PageSection, Title, Grid, GridItem } from '@patternfly/react-core';
import { WindTurbineCard } from './WindTurbineCard';
import { getWindTurbines } from "../services/WindTurbinesService";
import { subscribeToPowerEvents } from "../services/PowerEventsService";
import { subscribeToStatsEvents } from "../services/StatsEventsService ";
import { WindTurbine } from '../models/WindTurbine';
import { WindTurbineStats } from '../models/WindTurbineStats';
import { waitForLiveness } from '../services/LivenessService';


interface ProductionByTurbine {
    [turbineId: number]: number
}

interface StatsByTurbine {
    [turbineId: number]: WindTurbineStats
}


export function Dashboard(): JSX.Element {
    const [turbines, setTurbines] = useState<WindTurbine[]>([]);
    const [powerProductionValues, setPowerProductionValues] = useState<ProductionByTurbine>({});
    const [stats, setStats] = useState<StatsByTurbine>({});


    useEffect(() => {
        waitForLiveness()
            .then(() => getWindTurbines())
            .then((turbines) => {
                setTurbines(turbines);
                getPowerServerEvents();
                getPowerStatsEvents();
            });
    }, []);

    function getPowerServerEvents() {
        subscribeToPowerEvents((event) => {
            setPowerProductionValues(values => ({
                ...values,
                [event.turbineId]: event.megawatts
            }));
        });
    }

    function getPowerStatsEvents() {
        subscribeToStatsEvents((turbineStats) => {
            setStats(stats => ({
                ...stats,
                [turbineStats.turbineId]: turbineStats
            }));
        });
    }

    function getTurbineProduction(id: number) {
        const production = powerProductionValues[id];
        return production ? production : 0;
    }

    function getTurbineStats(id: number) {
        const turbineStats = stats[id];
        return turbineStats || { count: 0 };
    }

    return (
        <PageSection>
            <Title headingLevel="h1" size="lg">Wind Turbines Dashboard</Title>
            <Grid hasGutter>
            {turbines.map(turbine =>
                <GridItem key={turbine.id}>
                    <WindTurbineCard
                    turbine={turbine}
                    production={{ megawatts: getTurbineProduction(turbine.id) }}
                    stats={getTurbineStats(turbine.id)}
                    ></WindTurbineCard>
                </GridItem>
            )}
            </Grid>
        </PageSection>
    )
}

