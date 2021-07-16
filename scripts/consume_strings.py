
"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

import json
import argparse
from kafka import KafkaConsumer
try:
    from ad482.common.classroom import KafkaClassroomConfig, workspace
except ModuleNotFoundError as e:
    print(e)
    print("Error when trying to use the 'rht-labs-ad482' package.")
    print("Did you forget to activate the Python "
          "virtual environment for this course?")
    exit(1)


def string_deserializer(x: bytes):
    return x.decode('utf-8')


config: KafkaClassroomConfig = workspace.config
server = f"{config.kafka_bootstrap_server}:{config.kafka_bootstrap_port}"
consumer = KafkaConsumer(
    bootstrap_servers=server,
    security_protocol="SSL",
    ssl_cafile=config.kafka_cert_path,
    group_id='simple-consumer-group',
    auto_offset_reset='earliest',
    enable_auto_commit=False,
    value_deserializer=string_deserializer
)


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Consume strings")
    parser.add_argument('topic', type=str)
    args = parser.parse_args()

    consumer.subscribe(args.topic)

    print("Consuming messages...\n")

    for message in consumer:
        if message.key:
            print(f"\tKey: {message.key.decode('utf-8')}")
        print(f"\tPartition: {message.partition}")
        print(f"\tValue: {message.value}")
        print('>>>>')
