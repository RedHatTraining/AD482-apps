"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

from pprint import pprint
from random import uniform, choice
from ad482.common import kafka
from ad482.common.kafka import serdes
from ad482.common.classroom import workspace


class Vehicle:

    vehicleId: int # noqa
    # Degrees as float values
    latitude: float
    longitude: float
    # Elevation in meters
    elevation: float

    def __init__(self, vehicle_id: int) -> None:
        self.vehicleId = vehicle_id
        self.latitude = uniform(37, 42)
        self.longitude = uniform(-2, 5)
        self.elevation = 0

    def move(self):
        delta = 0.001
        self.latitude += uniform(-delta, delta)
        self.longitude += uniform(-delta, delta)

        delta = 1
        self.elevation += uniform(-delta, +delta)
        if self.elevation < 0:
            self.elevation = 0


def generate_vehicle_positions():
    vehicles = [Vehicle(i) for i in range(10)]

    while True:
        vehicle = choice(vehicles)
        vehicle.move()
        yield vehicle.__dict__


def print_produced(topic, record):
    print(f"- Generated vehicle position to '{topic}'")
    pprint(record)
    print()


if __name__ == "__main__":

    TOPIC = "vehicle-positions"

    kafka.produce_many(
        TOPIC,
        generate_vehicle_positions(),
        workspace.config,
        value_serializer=serdes.json_serializer,
        sleep_seconds=3,
        callback=print_produced
    )
