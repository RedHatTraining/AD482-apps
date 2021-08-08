connector_class="org.apache.camel.kafkaconnector.elasticsearchrest.CamelElasticsearchrestSinkConnector" &&
curl -X POST \
  http://localhost:8083/connectors \
  -H 'Content-Type: application/json' \
  -d '{ "name": "elasticsearch-sink-connector",
    "config":
    {
        "connector.class": "'$connector_class'",
        "tasks.max": "1",
        "topics": "github-events",
        "camel.sink.endpoint.hostAddresses": "elasticsearch-es-http:9200",
        "camel.sink.endpoint.indexName": "github_events",
        "camel.sink.endpoint.operation": "Index",
        "camel.sink.path.clusterName": "elasticsearch",
        "key.converter": "org.apache.kafka.connect.storage.StringConverter",
        "value.converter": "org.apache.kafka.connect.storage.StringConverter"
    }
}'