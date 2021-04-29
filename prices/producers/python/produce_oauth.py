import os
from time import sleep
from random import randint
from dotenv import load_dotenv
from kafka import KafkaProducer
from oauthlib.oauth2 import BackendApplicationClient
from requests_oauthlib import OAuth2Session

# Read environment variables from .env
load_dotenv()


def int_serializer(x):
    return x.to_bytes(4, 'big')


class TokenProvider():
    def __init__(self, client_id, client_secret):
        self.client_id = client_id
        self.client_secret = client_secret

    def token(self):
        token_url = os.environ["KAFKA_TOKEN_URL"]
        client = BackendApplicationClient(client_id=self.client_id)
        oauth = OAuth2Session(client=client)
        token_json = oauth.fetch_token(
            token_url=token_url,
            client_id=self.client_id,
            client_secret=self.client_secret)
        token = token_json['access_token']
        return token


producer = KafkaProducer(
    bootstrap_servers=os.environ["KAFKA_BOOTSTRAP_SERVER"],
    security_protocol="SASL_SSL",
    sasl_mechanism="OAUTHBEARER",
    sasl_oauth_token_provider=TokenProvider(
        os.environ["KAFKA_CLIENT_ID"],
        os.environ["KAFKA_CLIENT_SECRET"]),
    value_serializer=int_serializer
)


while True:
    price = randint(1, 100)
    producer.send('prices', price)
    print("Sent", price)
    sleep(1)
