package org.opentsdb;

public class OpenTsdbClientTest {
    private static final String URI = "http://10.1.1.1:4242";

    public static void main(String[] args) {
        testPut(URI);
        testQuery(URI);
    }

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
}
