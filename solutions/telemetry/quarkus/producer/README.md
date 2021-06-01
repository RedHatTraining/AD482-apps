# Kafka Quarkus Producer App

Set the Kafka connection configuration in the `application.properties` file.

Run the Quarkus app

```
./mvnw clean quarkus:dev
```

Send a post request to produce a new temperature value

```
curl -XPOST -H "Content-Type:application/json"  http://localhost:8080/temperature --data '{"value": "40"}'
```
