package org.opentsdb;

public class Response {
    private int code;
    private String message;
    private DataPoint[] dataPoints;

    Response(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return this.code == 200 || this.code == 204;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public DataPoint[] getDataPoints() {
        return this.dataPoints;
    }

    void setDataPoints(DataPoint[] dataPoints) {
        this.dataPoints = dataPoints;
    }

    @Override
    public String toString() {
        String sb = "Response{" + "code=" + this.code +
                ", message=" + this.message +
                "}";

        return sb;
    }
}
