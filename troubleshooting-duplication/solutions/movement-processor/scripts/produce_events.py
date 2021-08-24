"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

import argparse
from pprint import pprint
from random import uniform, choice
from ad482.common import kafka
from ad482.common.kafka import serdes
from ad482.common.classroom import workspace



def print_produced(topic, record):
    print(f"- Generated event to '{topic}' topic")
    pprint(record)
    print()


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Produce random integers")
    parser.add_argument('topic', type=str)
    args = parser.parse_args()

    events = [
        { "distance": 10, "time": 5 },
        { "distance": 20, "time": 20 },
        { "distance": 0, "time": 0 },
        { "distance": 40, "time": 10 },
        { "distance": 50, "time": 5 },
    ]

    kafka.produce_many(
        args.topic,
        events,
        workspace.config,
        value_serializer=serdes.json_serializer,
        sleep_seconds=0,
        callback=print_produced
    )
