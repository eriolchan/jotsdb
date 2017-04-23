package org.opentsdb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OpenTsdbClient {

    private static Logger logger = LoggerFactory.getLogger(OpenTsdbClient.class);

    private static final String PARAM_SYMBOL = "?";
    private static final String QUERY_API = "/api/query";
    private static final String PUT_API = "/api/put";
    private static final int MAX_CONNECTIONS = 20;

    private String uri;
    private PoolingHttpClient httpClient;
    private Gson mapper;

    public OpenTsdbClient(String uri) {
        this(uri, MAX_CONNECTIONS);
    }

    public OpenTsdbClient(String uri, int maxConnections) {
        this.uri = uri;
        this.httpClient = new PoolingHttpClient(maxConnections);
        this.mapper = new GsonBuilder().create();
    }

    public Response putDataPoints(MetricBuilder builder) {
        String url = this.uri + PUT_API;

        Response response = null;
        try {
            SimpleHttpResponse simpleHttpResponse = this.httpClient.post(
                    url,
                    this.mapper.toJson(builder.build()));
            return parseResponse(simpleHttpResponse);
        } catch (IOException e) {
            logger.error("error occurs when putting data points to opentsdb");
        }

        return response;
    }

    public Response queryDataPoints(QueryBuilder builder) {
        String url = this.uri + QUERY_API + PARAM_SYMBOL + builder.build();

        Response response = null;
        try {
            SimpleHttpResponse simpleHttpResponse = this.httpClient.get(url);
            return parseResponse(simpleHttpResponse);
        } catch (IOException e) {
            logger.error("error occurs when querying data points from opentsdb");
        }

        return response;
    }

    private Response parseResponse(SimpleHttpResponse simpleHttpResponse) {
        if (simpleHttpResponse == null) {
            return null;
        }

        Response response = new Response(simpleHttpResponse.getStatusCode());

        if (simpleHttpResponse.isEmptyBody()) {
            return response;
        }

        if (simpleHttpResponse.isSuccess()) {
            DataPoint[] dataPoints = this.mapper.fromJson(simpleHttpResponse.getBody(), DataPoint[].class);
            response.setDataPoints(dataPoints);
        } else {
            ErrorDetail errorDetail = this.mapper.fromJson(simpleHttpResponse.getBody(), ErrorDetail.class);
            response.setMessage(errorDetail.error.message);
        }

        return response;
    }

    public void shutdown() {
        try {
            if (this.httpClient != null) {
                this.httpClient.shutdown();
            }
        } catch (IOException e) {
            logger.warn("error occurs when closing opentsdb client", e);
        }
    }
}