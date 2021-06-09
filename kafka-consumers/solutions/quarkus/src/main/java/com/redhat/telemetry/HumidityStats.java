package com.redhat.telemetry;

class HumidityStats {
    public float latest = 0;
    public int count = 0;
    public float sum = 0;
    public float average = 0;

    public void add(int humidityValue) {
        float normalizedValue = (float) humidityValue / 100;
        count++;
        latest = normalizedValue;
        sum += normalizedValue;
        average = sum / count;
    }
}
