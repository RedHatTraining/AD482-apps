"""
Run this script in the virtual environment where
rht-labs-ad482 is installed
"""

from time import sleep
from sys import exit

try:
    from ad482.common.classroom import KafkaClassroomConfig, workspace
    from ad482.common.kafka import KafkaPublisher, serdes
except ModuleNotFoundError as e:
    print(e)
    print("Error when trying to use the 'rht-labs-ad482' package.")
    print("Did you forget to activate the Python virtual environment for this course?")
    exit(1)


class WindTurbineEarningWasAdded:
    windTurbineId: int
    amount: float

    def __init__(self, windTurbineId: int, amount: float) -> None:
        self.windTurbineId = windTurbineId
        self.amount = amount


class WindTurbineExpenseWasAdded:
    windTurbineId: int
    amount: float

    def __init__(self, windTurbineId: int, amount: float) -> None:
        self.windTurbineId = windTurbineId
        self.amount = amount


def print_event(record):
    print(f"- New {record.__class__.__name__} event")
    print(f"\t Wind Turbine ID: {record.windTurbineId}")
    print(f"\t Amount: {record.amount}")
    print()


def get_topic_from_value(value):
    if isinstance(value, WindTurbineEarningWasAdded):
        return 'wind-turbine-earnings'

    return 'wind-turbine-expenses'


events_data = [
    {'key': 1, 'value': WindTurbineEarningWasAdded(1, 100.00)},
    {'key': 2, 'value': WindTurbineEarningWasAdded(2, 3000.00)},
    {'key': 1, 'value': WindTurbineExpenseWasAdded(1, 50.00)},
    {'key': 2, 'value': WindTurbineExpenseWasAdded(2, 2000.00)},
    {'key': 1, 'value': WindTurbineEarningWasAdded(1, 1000.00)},
    {'key': 2, 'value': WindTurbineEarningWasAdded(2, 3500.00)},
    {'key': 1, 'value': WindTurbineExpenseWasAdded(1, 150.00)},
    {'key': 2, 'value': WindTurbineExpenseWasAdded(2, 3200.00)},
    {'key': 1, 'value': WindTurbineEarningWasAdded(1, 1300.00)},
    {'key': 2, 'value': WindTurbineEarningWasAdded(2, 3800.00)},
    {'key': 1, 'value': WindTurbineExpenseWasAdded(1, 300.00)},
    {'key': 2, 'value': WindTurbineExpenseWasAdded(2, 4200.00)},
]


if __name__ == "__main__":

    print('Sending earning and expense events...')

    producer = KafkaPublisher.from_config(
        workspace.config,
        serdes.json_serializer,
        serdes.int_serializer
    )

    for event in events_data:
        topic = get_topic_from_value(event['value'])
        producer.produce(topic, event['value'].__dict__, event['key'])
        print_event(event['value'])
        sleep(1)
