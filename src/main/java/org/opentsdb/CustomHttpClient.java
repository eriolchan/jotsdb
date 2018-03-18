package org.opentsdb;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

class CustomHttpClient {
    private final CloseableHttpClient httpClient;

    private ResponseHandler<SimpleHttpResponse> handler = (httpResponse) -> {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        SimpleHttpResponse simpleHttpResponse = new SimpleHttpResponse(statusCode);

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            simpleHttpResponse.setBody(EntityUtils.toString(entity, "UTF-8"));
        }

        return simpleHttpResponse;
    };

    CustomHttpClient() {
        this.httpClient = HttpClients.createDefault();
    }

    SimpleHttpResponse post(String url, String data) throws IOException {
        StringEntity requestEntity = new StringEntity(data, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(requestEntity);

        return this.httpClient.execute(httpPost, this.handler);
    }

    SimpleHttpResponse get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        return this.httpClient.execute(httpGet, this.handler);
    }

    void close() throws IOException {
        this.httpClient.close();
    }
}