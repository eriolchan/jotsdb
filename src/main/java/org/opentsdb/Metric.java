package org.opentsdb;

import java.util.HashMap;
import java.util.Map;

public class Metric {
    private String metric;
    private int timestamp;
    private Object value;
    private Map<String, String> tags;

    Metric(String metric) {
        this.metric = metric;
        this.tags = new HashMap<>();
    }

    public Metric withTag(String name, String value) {
        this.tags.put(name, value);

        return this;
    }

    public Metric withValue(int timestamp, int value) {
        return setValue(timestamp, value);
    }

    public Metric withValue(int timestamp, double value) {
        return setValue(timestamp, value);
    }

    String getMetric() {
        return this.metric;
    }

    int getTimestamp() {
        return this.timestamp;
    }

    Object getValue() {
        return this.value;
    }

    Map<String, String> getTags() {
        return this.tags;
    }

    private Metric setValue(int timestamp, Object value) {
        this.timestamp = timestamp;
        this.value = value;

        return this;
    }
}
