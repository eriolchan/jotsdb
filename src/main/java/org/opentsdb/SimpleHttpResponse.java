package org.opentsdb;

public class SimpleHttpResponse {

    private int statusCode;
    private String body;

    public SimpleHttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public boolean isSuccess() {
        return this.statusCode == 200 || this.statusCode == 204;
    }

    public boolean isEmptyBody() {
        return this.body == null || this.body.isEmpty();
    }
}
