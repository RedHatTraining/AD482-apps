
"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

import json
import random
import argparse
from time import sleep
from random import randint
from kafka import KafkaProducer
try:
    from ad482.common.classroom import KafkaClassroomConfig, workspace
except ModuleNotFoundError as e:
    print(e)
    print("Error when trying to use the 'rht-labs-ad482' package.")
    print("Did you forget to activate the Python "
          "virtual environment for this course?")
    exit(1)


def string_serializer(x):
    return json.dumps(x).encode('utf-8')


config: KafkaClassroomConfig = workspace.config
server = f"{config.kafka_bootstrap_server}:{config.kafka_bootstrap_port}"
producer = KafkaProducer(
    bootstrap_servers=server,
    value_serializer=string_serializer,
    security_protocol="SSL",
    ssl_cafile=config.kafka_cert_path
)


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Produce random integers")
    parser.add_argument('topic', type=str)
    args = parser.parse_args()
    currencies = ["EUR", "USD", "GBP"]

    while True:
        source_wallet_id = randint(1, 10000000)
        destination_wallet_id = randint(1, 10000000)
        amount = randint(1, 100000)
        currency = random.choice(currencies)
        event = {
            "sourceWalletId": source_wallet_id,
            "destinationWalletId": destination_wallet_id,
            "destinationWalletId": destination_wallet_id,
            "amount": amount,
            "currency": currency
        }
        producer.send(args.topic, event)
        print("Sent MoneyTransferred event", event)
        sleep(5)
