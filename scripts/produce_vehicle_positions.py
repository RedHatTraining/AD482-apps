from random import uniform, choice
from ad482.common import kafka
from ad482.common.kafka import serdes
from ad482.common.classroom import workspace


class Vehicle:

    id: int
    # Degrees as float values
    latitude: float
    longitude: float
    # Elevation in meters
    elevation: float

    def __init__(self, id: int) -> None:
        self.id = id
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
    print(f"Generated vehicle position to '{topic}: {record}")


if __name__ == "__main__":

    TOPIC = "vehicle-positions"

    kafka.produce_many(
        TOPIC,
        generate_vehicle_positions(),
        workspace.config,
        value_serializer=serdes.json_serializer,
        sleep_seconds=0.1,
        callback=print_produced
    )
