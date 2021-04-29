import os
from time import sleep
from random import randint
from kafka import KafkaProducer
from dotenv import load_dotenv


# Read environment variables from .env
load_dotenv()


def int_serializer(x):
    return x.to_bytes(4, 'big')


producer = KafkaProducer(
    bootstrap_servers=os.environ["KAFKA_BOOTSTRAP_SERVER"],
    value_serializer=int_serializer,
    security_protocol="SSL",
    ssl_cafile=os.environ["KAFKA_SSL_CAFILE"],
)


while True:
    price = randint(1, 100)
    producer.send('prices', price)
    print("Sent", price)
    sleep(1)
