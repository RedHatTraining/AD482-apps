
"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

from time import sleep
import argparse
from kafka import KafkaConsumer

from ad482.common.kafka.serdes import int_deserializer
try:
    from ad482.common.classroom import KafkaClassroomConfig, workspace
except ModuleNotFoundError as e:
    print(e)
    print("Error when trying to use the 'rht-labs-ad482' package.")
    print("Did you forget to activate the Python virtual environment for this course?")
    exit(1)


config: KafkaClassroomConfig = workspace.config
server = f"{config.kafka_bootstrap_server}:{config.kafka_bootstrap_port}"
print(server)

consumer = KafkaConsumer(
    bootstrap_servers=server,
    security_protocol="SSL",
    ssl_cafile=config.kafka_cert_path,
    group_id='simple-consumera-group',
    auto_offset_reset='latest',
    enable_auto_commit=False,
    value_deserializer=int_deserializer
)


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Consume integers")
    parser.add_argument('topic', type=str)
    args = parser.parse_args()

    consumer.subscribe("retries1")

    print(f"Consuming messages from '{args.topic}'...\n")

    measures = []

    for message in consumer:

        # print(f"\tKey: {message.key.decode('utf-8')}")
        # print(f"\tPartition: {message.partition}")
        # print(f"\tMeasurement: {message.value}")

        if len(measures) % 10 == 0:
            print(len(measures), "received")

        if message.value in measures:
            print("DUPLICATED!!!!!!!!!")

        sleep(1)

        measures.append(message.value)




