"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

from pprint import pprint
from ad482.common import kafka
from ad482.common.kafka import serdes
from ad482.common.classroom import workspace


class Vehicle:
    id: int
    type: str
    model: str

    def __init__(self, id: int, type: str, model: str):
        self.id = id
        self.type = type
        self.model = model


def print_produced(topic, record):
    print(f"- Sent VehicleRegistered event to '{topic}' topic")
    pprint(record)
    print()


if __name__ == "__main__":

    TOPIC = "vehicles"

    vehicles = [
        Vehicle(1, "bike", "Acme Urban Bike"),
        Vehicle(2, "car", "Acme HitTheRoad Hybrid"),
        Vehicle(3, "drone", "Acme SuperFly"),
    ]

    # Keys are vehicle ids
    records = [(vehicle.id, vehicle.__dict__) for vehicle in vehicles]

    kafka.produce_many(
        TOPIC,
        records,
        workspace.config,
        value_serializer=serdes.json_serializer,
        key_serializer=serdes.int_serializer,
        sleep_seconds=0.1,
        callback=print_produced
    )
