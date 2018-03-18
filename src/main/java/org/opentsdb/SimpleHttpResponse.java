package org.opentsdb;

class SimpleHttpResponse {
    private int statusCode;
    private String body;

    SimpleHttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    boolean isSuccess() {
        return this.statusCode == 200 || this.statusCode == 204;
    }

    boolean isEmptyBody() {
        return this.body == null || this.body.isEmpty();
    }

    int getStatusCode() {
        return this.statusCode;
    }

    void setBody(String body) {
        this.body = body;
    }

    String getBody() {
        return this.body;
    }
}
