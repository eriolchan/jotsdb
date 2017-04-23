package org.opentsdb;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PoolingHttpClient {

    private static final String CHARSET = "UTF-8";
    private static final int MILLI_PER_SECOND = 1000;
    private static final int TIMEOUT = 10 * MILLI_PER_SECOND;
    private static final int KEEP_ALIVE = 30 * MILLI_PER_SECOND;

    private PoolingHttpClientConnectionManager connManager;
    private CloseableHttpClient httpClient;

    private ConnectionKeepAliveStrategy strategy = (response, context) -> {
        HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));

        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * MILLI_PER_SECOND;
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }

        return KEEP_ALIVE;
    };

    ResponseHandler<SimpleHttpResponse> handler = (httpResponse) -> {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        SimpleHttpResponse simpleHttpResponse = new SimpleHttpResponse(statusCode);

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            simpleHttpResponse.setBody(EntityUtils.toString(entity, CHARSET));
        }

        return simpleHttpResponse;
    };

    public PoolingHttpClient(int maxConnections) {
        this.connManager = new PoolingHttpClientConnectionManager();
        this.connManager.setMaxTotal(maxConnections);
        this.connManager.setDefaultMaxPerRoute(maxConnections);

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();

        this.httpClient = HttpClients.custom()
                .setKeepAliveStrategy(this.strategy)
                .setConnectionManager(this.connManager)
                .setDefaultRequestConfig(config).build();

        IdleConnectionMonitorThread staleMonitor = new IdleConnectionMonitorThread(this.connManager);
        staleMonitor.start();
    }

    public SimpleHttpResponse post(String url, String data) throws IOException {
        StringEntity requestEntity = new StringEntity(data);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(requestEntity);

        return this.httpClient.execute(httpPost, this.handler);
    }

    public SimpleHttpResponse get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        return this.httpClient.execute(httpGet, this.handler);
    }

    public void shutdown() throws IOException {
        this.httpClient.close();
        this.connManager.shutdown();
    }

    private static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!this.shutdown) {
                    synchronized (this) {
                        wait(5 * MILLI_PER_SECOND);
                        // Close expired connections
                        this.connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 30 sec
                        this.connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                shutdown();
            }
        }

        public void shutdown() {
            this.shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}