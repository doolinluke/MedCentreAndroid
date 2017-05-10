package com.medcentre.app.com.medcentre.app.http;;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse implements Serializable {

    private int status;
    private String description;
    private Map<String, String> headers;
    private String body;

    public HttpResponse() {
        this.headers = new HashMap<String, String>();
    }
    
    public int getStatus() {
    	return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getDescription() {
    	return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Map<String, String> getHeaders() {
    	return this.headers;
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }
    
    public String getBody() {
        return body;
    }

    public void setBody(String is) {
        this.body = is;
    }
}
