package com.redhat.vehicles;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class EventDeserializer<T> implements Deserializer<T> {

    private Class<T> targetClass;

    public EventDeserializer(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public T deserialize(String s, byte[] b) {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(b, targetClass);
        } catch (Exception error) {
            System.out.println("Error deserializing json: " + error);
            return null;
        }
    }
}