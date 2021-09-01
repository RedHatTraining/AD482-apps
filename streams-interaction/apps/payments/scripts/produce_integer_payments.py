"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

from random import randint
from ad482.common import kafka
from ad482.common.kafka import serdes


def generate_payments():
    while True:
        yield randint(1, 2000)


def print_payment(topic, value):
    print(f"\tPayment sent: {value} USD")


if __name__ == "__main__":

    kafka.produce_many(
        "payments",
        generate_payments(),
        value_serializer=serdes.int_serializer,
        callback=print_payment
    )
