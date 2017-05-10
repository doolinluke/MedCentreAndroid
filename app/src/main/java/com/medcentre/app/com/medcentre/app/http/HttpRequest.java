package com.medcentre.app.com.medcentre.app.http;;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private URI uri;
    private String method;
    private Map<String, String> headers;
    private String body;

    public HttpRequest(String method, URI uri) {
        this.method = method;
        this.uri = uri;
        this.headers = new HashMap<String, String>();
        this.body = "";
    }

    public URI getUri() {
        return this.uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void addHeader(String name, String value) throws HttpException {
        try {
            name = URLEncoder.encode(name, "UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
            this.headers.put(name, value);
        } catch (UnsupportedEncodingException ex) {
            throw new HttpException(ex.getMessage(), ex);
        }
    }

    public void removeHeader(String name) {
        this.headers.remove(name);
    }

    public String getHeader(String name) {
        return this.headers.remove(name);
    }

    public Map<String, String> getHeaders() {
        return new HashMap<String, String>(this.headers);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
