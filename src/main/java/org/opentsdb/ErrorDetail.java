package org.opentsdb;

class ErrorDetail {
    Error error;

    class Error {
        int code;
        String message;
        String details;
        String trace;
    }
}
