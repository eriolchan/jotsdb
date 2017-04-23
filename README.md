# OpenTSDB Java Client
jotsdb is a Java client for OpenTSDB. It provides put and query data points to OpenTSDB by its HTTP API.


# Put Data Points
Here is the API interface

```
public Response putDataPoints(MetricBuilder builder);
```

It use MetricBuilder to build Metric, and Metric is a container of time series data.

```
public class Metric {
    private String metric; // metric name
    private int timestamp;
    private Object value;
    private Map<String, String> tags;
}
```

For each Metric, name, timestamp and value are required.

- *timestamp* is an Integer stands for the second.
- *value* type can be Integer or Double.
- at least 1 *tag* is required for each time series data, and multiple tags are allowed. (no more than 5 according to official documents)

Response is the result object, it contains the status code, error message and the data points.

```
public class Response {
    private int code;
    private String message;
    private DataPoint[] dataPoints;
}
```

## Example

```
private static void testPut(String uri) {
    OpenTsdbClient client = new OpenTsdbClient(uri);
 
    int timestamp = 1491559435;
    String name = "test.demo";
    double value = 123.45;
 
    MetricBuilder builder = new MetricBuilder();
 
    builder.addMetric(name)
            .withValue(timestamp, value)
            .withTag("vid", "15840");
 
    Response response = client.putDataPoints(builder);
    System.out.println(response);
 
    client.shutdown();
}
```


# Query Data Point
Here is the API interface

```
public Response queryDataPoints(QueryBuilder builder)
```

It use QueryBuilder to build query parameters, including metric name, time window, tags and aggregator. Among them, metric and start are required.


```
public class QueryBuilder {
    private int start;
    private int end;
    private String aggregator;
    private String metric;
    private Map<String, String> tags;
}
```

## Example

```
private static void testQuery(String uri) {
    OpenTsdbClient client = new OpenTsdbClient(uri);
 
    int timestamp = 1491559435;
    String name = "test.demo";
 
    QueryBuilder builder = new QueryBuilder(timestamp, name)
            .withTag("vid", "15840");
 
    Response response = client.queryDataPoints(builder);
    System.out.println(response);
 
    client.shutdown();
}
```

[Official Documents](http://opentsdb.net/docs/build/html/api_http/index.html)
