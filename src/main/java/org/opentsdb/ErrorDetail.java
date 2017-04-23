package org.opentsdb;

public class ErrorDetail {

    public Error error;

    class Error {
        public int code;
        public String message;
        public String details;
        public String trace;
    }
}
