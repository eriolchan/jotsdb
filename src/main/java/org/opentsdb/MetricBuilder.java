package org.opentsdb;

import java.util.ArrayList;
import java.util.List;

public class MetricBuilder {
    private List<Metric> metrics;

    public MetricBuilder() {
        this.metrics = new ArrayList<>();
    }

    public Metric addMetric(String metricName) {
        Metric metric = new Metric(metricName);
        this.metrics.add(metric);

        return metric;
    }

    public void clear() {
        this.metrics.clear();
    }

    public int size() {
        return this.metrics.size();
    }

    List<Metric> build() {
        for (Metric metric : this.metrics) {
            if (metric.getTags().size() < 1) {
                throw new IllegalArgumentException(
                        String.format("%s must contain at least 1 tag", metric.getMetric()));
            }
        }

        return this.metrics;
    }
}
