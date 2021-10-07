import { GardenStatus } from "@app/models/GardenStatus";
import { Card, CardTitle, CardBody, DescriptionList, DescriptionListGroup, DescriptionListTerm, DescriptionListDescription, Grid, GridItem } from "@patternfly/react-core";
import React from "react";

import garden0 from "@app/images/garden_0.jpg";
import garden1 from "@app/images/garden_1.jpg";
import garden2 from "@app/images/garden_2.jpg";
import garden3 from "@app/images/garden_3.jpg";

const images = [garden0, garden1, garden2, garden3];


interface GardenStatusCardProps {
    gardenStatus: GardenStatus
}

export function GardenStatusCard(props: GardenStatusCardProps): JSX.Element {
    const { gardenStatus } = props;
    return (<Card isFlat>
        <Grid md={6}>
            <GridItem
                style={{
                    minHeight: "200px",
                    backgroundPosition: "center",
                    backgroundSize: "cover",
                    backgroundImage: `url(${images[gardenStatus.id]})`
                }}
            />
            <GridItem>
                <CardTitle>{gardenStatus.name}</CardTitle>
                <CardBody>
                    <DescriptionList>
                        <DescriptionListGroup>
                            <DescriptionListTerm>Sensor</DescriptionListTerm>
                            <DescriptionListDescription>{gardenStatus.sensorId}</DescriptionListDescription>
                        </DescriptionListGroup>
                        <DescriptionListGroup>
                            <DescriptionListTerm>Timestamp</DescriptionListTerm>
                            <DescriptionListDescription>{gardenStatus.lastUpdate}</DescriptionListDescription>
                        </DescriptionListGroup>
                        <DescriptionListGroup>
                            <DescriptionListTerm>Temperature</DescriptionListTerm>
                            <DescriptionListDescription>{gardenStatus.temperature} ºC</DescriptionListDescription>
                        </DescriptionListGroup>
                        <DescriptionListGroup>
                            <DescriptionListTerm>Garden</DescriptionListTerm>
                            <DescriptionListDescription>{gardenStatus.garden}</DescriptionListDescription>
                        </DescriptionListGroup>
                    </DescriptionList>
                </CardBody>
            </GridItem>
        </Grid>
    </Card>);
}