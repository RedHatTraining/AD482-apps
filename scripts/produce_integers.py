
"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

import argparse
from time import sleep
from random import randint
from kafka import KafkaProducer
try:
    from ad482.common.classroom import KafkaClassroomConfig, workspace
except ModuleNotFoundError as e:
    print(e)
    print("Error when trying to use the 'rht-labs-ad482' package.")
    print("Did you forget to activate the Python virtual environment for this course?")
    exit(1)


def int_serializer(x):
    return x.to_bytes(4, "big")


config: KafkaClassroomConfig = workspace.config
server = f"{config.kafka_bootstrap_server}:{config.kafka_bootstrap_port}"
producer = KafkaProducer(
    bootstrap_servers=server,
    value_serializer=int_serializer,
    security_protocol="SSL",
    ssl_cafile=config.kafka_cert_path
)


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Produce random integers")
    parser.add_argument('topic', type=str)
    args = parser.parse_args()

    while True:
        value = randint(1, 100)
        producer.send(args.topic, value)
        print("Sent", value)
        sleep(1)
