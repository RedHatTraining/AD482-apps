# Telemetry App Examples

This folder contains a basic temperature telemetry Kafka app, in two flavors:

- `console` contains a producer/consumer Kafka app in plain Java.
- `quarkus` contains a producer/consumer Kafka app in Quarkus.

The `certs` folder is for the certificate files necessary to connect to your AMQ Streams cluster.
The apps are already configured to point to this folder.
You're free to choose your own certs folder, as long as you update the configuration of the apps.
