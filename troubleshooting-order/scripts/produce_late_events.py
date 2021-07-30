
"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

import argparse
from time import time, sleep
from ad482.common.kafka import serdes
from kafka import KafkaProducer
try:
    from ad482.common.classroom import KafkaClassroomConfig, workspace
except ModuleNotFoundError as e:
    print(e)
    print("Error when trying to use the 'rht-labs-ad482' package.")
    print("Did you forget to activate the Python virtual environment for this course?")
    exit(1)


class PotentialCustomersWereDetected:
    locationId: str
    amount: int

    def __init__(self, locationId: str, amount: int):
        self.locationId = locationId
        self.amount = amount


config: KafkaClassroomConfig = workspace.config
server = f"{config.kafka_bootstrap_server}:{config.kafka_bootstrap_port}"
producer = KafkaProducer(
    bootstrap_servers=server,
    key_serializer=serdes.string_serializer,
    value_serializer=serdes.json_serializer,
    security_protocol="SSL",
    ssl_cafile=config.kafka_cert_path
)


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Produce out-of-order events")
    parser.add_argument('topic', type=str)
    parser.add_argument('num_events', type=int)

    args = parser.parse_args()

    print(f"Sending {args.num_events} events to the '{args.topic}' topic...")

    for x in range(1, args.num_events + 1):
        event = PotentialCustomersWereDetected('front-area', 1)
        event_timestamp = int(time() * 1000)
        event_status = 'on time'

        if x % 10 == 0:
            event_timestamp = event_timestamp - 11000
            event_status = 'late'

        producer.send(args.topic, key="front-area", value=event.__dict__, timestamp_ms=event_timestamp)
        print(f'Event sent - timestamp {event_timestamp} ({event_status})')
        sleep(1)
