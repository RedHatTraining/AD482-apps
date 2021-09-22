
"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

import argparse
from time import time, sleep
from ad482.common.kafka import serdes
from kafka import KafkaProducer
from random import randrange
from sys import exit
try:
    from ad482.common.classroom import KafkaClassroomConfig, workspace
except ModuleNotFoundError as e:
    print(e)
    print("Error when trying to use the 'rht-labs-ad482' package.")
    print("Did you forget to activate the Python virtual environment for this course?")
    exit(1)


class TemperatureWasMeasuredInCelsius:
    locationId: int
    temperature: int

    def __init__(self, locationId: int, measure: int):
        self.locationId = locationId
        self.measure = measure


config: KafkaClassroomConfig = workspace.config
server = f"{config.kafka_bootstrap_server}:{config.kafka_bootstrap_port}"
producer = KafkaProducer(
    bootstrap_servers=server,
    value_serializer=serdes.json_serializer,
    security_protocol="SSL",
    ssl_cafile=config.kafka_cert_path
)


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Produce temperature events")
    parser.add_argument('topic', type=str)
    parser.add_argument('num_events', type=int)

    args = parser.parse_args()

    print(f"Sending {args.num_events} temperatures to the '{args.topic}' topic...")

    for x in range(1, args.num_events + 1):
        location_id = randrange(1, 10)
        temperature = randrange(-15, 50)

        event = TemperatureWasMeasuredInCelsius(location_id, temperature)
        print(f'Temperature sent - {temperature}ºC  (Location ID: {location_id})')
        producer.send(args.topic, key=None, value=event.__dict__)
        sleep(1)
