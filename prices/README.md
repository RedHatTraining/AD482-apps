# Prices app

This app is a basic message streaming example, using Kafka.
The app includes the following services:

- A Node.js price producer
- A Python price producer
- A Quarkus consumer, which consumes the prices produced by the other 2 services, and shows these prices in http://localhost:8080/prices.html.

The configuration files are adjusted to run this app in a local environment with a remote Kafka cluster.


# Local certificate setup


1. Extract the cluster certificate.
```sh
oc extract secret/my-cluster-cluster-ca-cert --keys=ca.crt \
    --to=- > ca.crt
```

2. Get the bootstrap route.

```sh
oc get routes my-cluster-kafka-bootstrap \
    -o=jsonpath='{.status.ingress[0].host}{"\n"}'
```

3. Generate a `truststore` file.
```sh
keytool -import -trustcacerts -alias root -file ca.crt \
    -keystore truststore.jks -storepass password -noprompt
```
4. Generate a `keystore` file.

```sh
keytool -import -trustcacerts -alias root -file ca.crt \
    -keystore keystore.jks -storepass password -noprompt
```

5. Move `ca.crt`, `truststore.jks`, and `keystore.jks` to the `AD482-apps/prices/certs` folder.


# Run the Quarkus consumer


```sh
cd consumer
KAFKA_BOOTSTRAP_SERVER=your-kafka-cluster.apps.na46.prod.nextcle.com:443 CERTS_DIR=/path/to/AD482-apps/prices/certs CERTS_PASSWORD=password ./mvnw quarkus:dev
```

Open `http://localhost:8080/prices.html` in your browser.


# Run the Python producer

Create the virtual environment, activate it, and install dependencies:

```sh
cd producers/python
source .venv/bin/activate
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

Create a `.env` file to specify your configuration:

```conf
KAFKA_BOOTSTRAP_SERVER=your-kafka-cluster.apps.na46.prod.nextcle.com:443
KAFKA_SSL_CAFILE=/path/to/AD482-apps/prices/certs/ca.crt
```

Run the producer:

```sh
python produce.py
```

# Run the Node.js producer

Install dependencies

```sh
cd producers/node
npm ci
```

Create a `.env` file to specify your configuration (or copy the one from the python folder):

```conf
KAFKA_BOOTSTRAP_SERVER=your-kafka-cluster.apps.na46.prod.nextcle.com:443
KAFKA_SSL_CAFILE=/path/to/AD482-apps/prices/certs/ca.crt
```

Run the producer:

```sh
node produce.py
```