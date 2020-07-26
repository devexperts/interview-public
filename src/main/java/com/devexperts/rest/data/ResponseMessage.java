package com.devexperts.rest.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseMessage {
    private final String message;
    private final int status;
    private final String error;
    private final Date date;
    private final String path;

    public ResponseMessage( String message, int status, String error, String path ) {
        this.message = message;
        this.status = status;
        this.date = new Date(System.currentTimeMillis());;
        this.error = error;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getDate() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");
        return formatter.format( date );
    }

    public String getPath() {
        return path;
    }
}
