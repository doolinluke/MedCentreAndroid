package com.medcentre.app.com.medcentre.app.http;

import java.util.EventObject;

/**
 * Created by n00134696 on 01/03/2016.
 */
public class HttpResponseEvent extends EventObject {

    private HttpResponse response;

    public HttpResponseEvent(Object source, HttpResponse response) {
        super(source);

        this.response = response;
    }

    public HttpResponse getResponse() {
        return this.response;
    }
}