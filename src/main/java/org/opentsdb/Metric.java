package org.opentsdb;

import java.util.HashMap;
import java.util.Map;

public class Metric {

    private String metric;
    private int timestamp;
    private Object value;
    private Map<String, String> tags;

    public Metric(String metric) {
        this.metric = metric;
        this.tags = new HashMap<>();
    }

    public String getMetric() {
        return this.metric;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public Object getValue() {
        return this.value;
    }

    public Map<String, String> getTags() {
        return this.tags;
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

    private Metric setValue(int timestamp, Object value) {
        this.timestamp = timestamp;
        this.value = value;

        return this;
    }
}
