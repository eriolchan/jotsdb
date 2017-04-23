package org.opentsdb;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryBuilder {

    private static final String CHARSET = "UTF-8";

    private int start;
    private int end;
    private String aggregator;
    private String metric;
    private Map<String, String> tags;

    public QueryBuilder(int start, String metric) {
        this.start = start;
        this.metric = metric;
        this.end = 0;
        this.aggregator = "sum";
        this.tags = new HashMap<>();
    }

    public QueryBuilder withEnd(int end) {
        this.end = end;
        return this;
    }

    public QueryBuilder withAggregator(String aggregator) {
        this.aggregator = aggregator;
        return this;
    }

    public QueryBuilder withTag(String name, String value) {
        this.tags.put(name, value);
        return this;
    }

    public String build() {
        if (this.tags.size() < 1) {
            throw new IllegalArgumentException("at least 1 tag is required");
        }

        return getEncoded();
    }

    private String getEncoded() {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("start", String.valueOf(this.start)));

        if (this.end != 0) {
            params.add(new BasicNameValuePair("end", String.valueOf(this.end)));
        }

        String tagString = this.tags.entrySet().stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(","));

        params.add(new BasicNameValuePair("m",
                String.format("%s:%s{%s}", this.aggregator, this.metric, tagString)));

        return URLEncodedUtils.format(params, CHARSET);
    }
}
