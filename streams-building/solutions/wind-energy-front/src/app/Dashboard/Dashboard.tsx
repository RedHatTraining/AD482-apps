import React, { useEffect, useState } from 'react';
import { PageSection, Title } from '@patternfly/react-core';
import { WindTurbineCard } from '../components/WindTurbineCard';
import { getWindTurbines } from "../services/WindTurbinesService";
import { WindTurbine } from '../models/WindTurbine';
import { WindTurbineProduction } from '../models/WindTurbineProduction';


export function Dashboard(): JSX.Element {
    const [turbines, setTurbines] = useState<WindTurbine[]>([]);
    const [powerProduction, setPowerProduction] = useState<WindTurbineProduction[]>();


    useEffect(() => {
        getWindTurbines().then((turbines) => {
            setTurbines(turbines);
            const eventSource = new EventSource("http://localhost:8080/turbines/generated-power");
            eventSource.onmessage = (message) => {
                const production = JSON.parse(message.data);
                setPowerProduction(production);
            };
        });
    }, []);


    useEffect(() => {
        if (!(lastMeasurement && turbines.length)) {
            return;
        }

        // console.log(turbines);
        // const updatedTurbines = turbines.map(t => {
        //     if (t.id === lastMeasurement.turbineId) {
        //         return {
        //             ...t,
        //             power: lastMeasurement.megawatts
        //         }
        //     }
        //     return t;
        // });

        // setTurbines(updatedTurbines);

    }, [lastMeasurement, turbines])




    return (
      <PageSection>
        <Title headingLevel="h1" size="lg">Dashboard Page Title!</Title>
        {turbines.map(turbine =>
            <WindTurbineCard key={turbine.id} {...turbine}></WindTurbineCard>
        )}
      </PageSection>
    )
}

