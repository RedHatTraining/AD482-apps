import React, { useEffect, useState } from "react";
import { PageSection, Title, PageSectionVariants, Card, CardBody, CardTitle, Grid, GridItem, Gallery, GalleryItem, Text, TextVariants, CardHeader, Skeleton, Page } from "@patternfly/react-core";
import { subscribeToGardenEvents, subscribeToGardenStatuses, subscribeToSensorMeasurements } from "../services/GardenServerEvents";
import { waitForLiveness } from "../services/LivenessService";
import { GardenStatus } from "@app/models/GardenStatus";
import { GardenStatusCard } from "./GardenStatusCard";
import { BullseyeSpinner } from "./BullseyeSpinner";
import { SensorMeasurement } from "@app/models/SensorMeasurement";
import { RecentList } from "@app/models/RecentList";
import { Caption, TableComposable, Tbody, Td, Th, Thead, Tr } from "@patternfly/react-table";
import { GardenEvent } from "@app/models/GardenEvent";

// Icons list: https://patternfly-react.surge.sh/icons/
import LeafIcon from "@patternfly/react-icons/dist/esm/icons/leaf-icon";
import OutLinedCharBarIcon from "@patternfly/react-icons/dist/esm/icons/outlined-chart-bar-icon";
import ThermometerHalfIcon from "@patternfly/react-icons/dist/esm/icons/thermometer-half-icon";
import InfoCircleIcon from "@patternfly/react-icons/dist/esm/icons/info-circle-icon";


interface StatusByGarden {
    [gardenId: number]: GardenStatus
}


export function Dashboard(): JSX.Element {
    const [ready, setReady] = useState<boolean>(false);
    const [gardenStatuses, setGardenStatuses] = useState<StatusByGarden>({});
    const [sensorMeasurements, setSensorMeasurements] = useState<RecentList<SensorMeasurement>>(new RecentList());
    const [gardenEvents, setGardenEvents] = useState<RecentList<GardenEvent>>(new RecentList());

    useEffect(() => {
        waitForLiveness()
            .then(() => {
                getGardenStatuses();
                getGardenEvents();
                getSensorMeasurements();
                setReady(true);
            });
    }, []);

    function getGardenStatuses() {
        subscribeToGardenStatuses((gardenStatus) => {
            console.log(gardenStatus);
            setGardenStatuses(previous => ({
                ...previous,
                [gardenStatus.id]: gardenStatus
            }));
        });
    }

    function getGardenEvents() {
        subscribeToGardenEvents((event) => {
            setGardenEvents(previous => RecentList.createFrom(previous).add(event));
        });
    }

    function getSensorMeasurements() {
        subscribeToSensorMeasurements((measurement) => {
            setSensorMeasurements(previous => RecentList.createFrom(previous).add(measurement));
        });
    }

    function renderGardenEventsTable() {
        return <TableComposable
            aria-label="Events table"
            variant="compact"
            borders={true}>
            <Caption>Real-time events generated after processing sensor measurements</Caption>
            <Thead>
                <Tr>
                    <Th key={0}>Event</Th>
                    <Th key={2}>Garden</Th>
                    <Th key={3}>Sensor id</Th>
                    <Th key={4}>Timestamp</Th>
                </Tr>
            </Thead>
            <Tbody>
                {gardenEvents.getItems().map(renderGardenEventRow)}
            </Tbody>
        </TableComposable>;
    }

    function renderSensorMeasurementsTable() {
        return <TableComposable
            aria-label="Measurements table"
            variant="compact"
            borders={true}>
            <Caption>Real-time measurements produced by garden sensors</Caption>
            <Thead>
                <Tr>
                    <Th key={0}>Type</Th>
                    <Th key={1}>Value</Th>
                    <Th key={2}>Garden</Th>
                    <Th key={3}>Sensor id</Th>
                    <Th key={4}>Timestamp</Th>
                </Tr>
            </Thead>
            <Tbody>
                {sensorMeasurements.getItems().map(renderSensorMeasurementRow)}
            </Tbody>
        </TableComposable>;
    }

    function renderGardenStatusGallery() {
        return <Gallery hasGutter minWidths={{
            md: "300px",
            lg: "300px",
            xl: "400px"
        }}>
            {Object.values(gardenStatuses).map(gardenStatus => <GalleryItem key={gardenStatus.id}>
                <GardenStatusCard gardenStatus={gardenStatus}></GardenStatusCard>
            </GalleryItem>)}
        </Gallery>;
    }

    function renderSensorMeasurementRow(m: SensorMeasurement) {
        const tableIndex = `${m.garden}_${m.sensorId}_${m.timestamp}`;
        return (<Tr key={m.timestamp.toString()}>
            <Td key={`${tableIndex}_type`} dataLabel="Type">
                {m.type}
            </Td>
            <Td key={`${tableIndex}_value`} dataLabel="Value">
                {m.value}
            </Td>
            <Td key={`${tableIndex}_garden`} dataLabel="Garden">
                {m.garden}
            </Td>
            <Td key={`${tableIndex}_sensor`} dataLabel="Sensor id">
                {m.sensorId}
            </Td>
            <Td key={`${tableIndex}_timestamp`} dataLabel="Type">
                {m.timestamp}
            </Td>
        </Tr>);
    }


    function renderGardenEventRow(m: GardenEvent) {
        const tableIndex = `${m.garden}_${m.sensorId}_${m.timestamp}`;
        return (<Tr key={m.timestamp.toString()}>
            <Td key={`${tableIndex}_name`} dataLabel="Type">
                {m.name}
            </Td>
            <Td key={`${tableIndex}_garden`} dataLabel="Garden">
                {m.garden}
            </Td>
            <Td key={`${tableIndex}_sensor`} dataLabel="Sensor id">
                {m.sensorId}
            </Td>
            <Td key={`${tableIndex}_timestamp`} dataLabel="Type">
                {m.timestamp}
            </Td>
        </Tr>);
    }

    return (<React.Fragment>
        <PageSection variant={PageSectionVariants.light}>
            <Title headingLevel="h1" size="lg">
                <LeafIcon size="md" color="#22aa22" />&nbsp;
                Garden Status&nbsp;
            </Title>
            <Text component={TextVariants.small}>
                General data for each garden, aggregated in time windows.
            </Text>
        </PageSection>

        <PageSection variant={PageSectionVariants.light}>
            {ready ? renderGardenStatusGallery() : <Skeleton />}
        </PageSection>

        <PageSection >
            <Title headingLevel="h1" size="lg">
                <OutLinedCharBarIcon size="md" color="#2222aa" />&nbsp;
                Data&nbsp;
            </Title>
            <Text component={TextVariants.small}>
                Real-time data processed or generated by the application.
            </Text>
        </PageSection>

        <PageSection>
            <Grid hasGutter>
                <GridItem span={6}>
                    <Card>
                        <CardTitle>
                            <ThermometerHalfIcon />
                            Sensor Measurements
                        </CardTitle>
                        <CardBody>
                            {ready ? renderSensorMeasurementsTable() : <Skeleton />}
                        </CardBody>
                    </Card>
                </GridItem>
                <GridItem span={6}>
                    <Card>
                        <CardTitle>
                            <InfoCircleIcon />
                            &nbsp;Garden Events
                        </CardTitle>
                        <CardBody>
                            {ready ? renderGardenEventsTable() : <Skeleton />}
                        </CardBody>
                    </Card>
                </GridItem>
            </Grid>
        </PageSection>
    </React.Fragment >);
}

