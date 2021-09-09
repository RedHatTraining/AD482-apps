"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

from pprint import pprint
from random import uniform
from typing import List
from ad482.common import kafka
from ad482.common.kafka import serdes
from ad482.common.classroom import workspace


class WindTurbine:

    id: int
    description: str
    # Power generation capacity in watts
    powerCapacity: int  # noqa
    # Max supported wind speed  in Mph
    cutOutWindSpeed: int  # noqa
    # Current produced power in watts
    power: float

    def __init__(
        self, id: int,
        description: str,
        power_capacity: int,
        cutout_wind_speed: int
    ) -> None:
        self.id = id
        self.description = description
        self.powerCapacity = power_capacity
        self.cutOutWindSpeed = cutout_wind_speed
        self.power = 0

    def rotate(self):
        next_production = self.power + int(uniform(-1000, 1000))

        if next_production < 0:
            self.power = 0

        elif next_production > self.powerCapacity:
            self.power = self.powerCapacity

        else:
            self.power = next_production

    def as_record(self):
        return (
            self.id,
            {
                "id": self.id,
                "description": self.description,
                "powerCapacity": self.powerCapacity,
                "cutOutWindSpeed": self.cutOutWindSpeed
            }
        )


def generate_power(turbines: List[WindTurbine]):
    while True:
        for turbine in turbines:
            turbine.rotate()
            yield (turbine.id, turbine.power)


def print_turbine(topic, record):
    print(f"- Registered new turbine in '{topic}' topic")
    pprint(record)
    print()


def print_power_value(topic, record):
    print(f"- Produced power in '{topic}' topic")
    pprint(record)
    print()


if __name__ == "__main__":

    turbines = [
        WindTurbine(1, "Wind Turbine A", 2000000, 50),
        WindTurbine(2, "Wind Turbine B", 2500000, 55),
        WindTurbine(3, "Wind Turbine C", 3000000, 58),
    ]

    kafka.produce_many(
        "turbines",
        [t.as_record() for t in turbines],
        workspace.config,
        key_serializer=serdes.int_serializer,
        value_serializer=serdes.json_serializer,
        sleep_seconds=0,
        callback=print_turbine
    )

    kafka.produce_many(
        "turbine-power-generation",
        generate_power(turbines),
        workspace.config,
        key_serializer=serdes.int_serializer,
        value_serializer=serdes.int_serializer,
        sleep_seconds=0.2,
        callback=print_power_value
    )
