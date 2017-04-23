package org.opentsdb;

public class Response {

    private int code;
    private String message;
    private DataPoint[] dataPoints;

    public Response(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDataPoints(DataPoint[] dataPoints) {
        this.dataPoints = dataPoints;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public DataPoint[] getDataPoints() {
        return this.dataPoints;
    }

    public boolean isSuccess() {
        return this.code == 200 || this.code == 204;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Response{");
        sb.append("code=").append(this.code);
        sb.append(", message=").append(this.message);
        sb.append("}");

        return sb.toString();
    }
}
