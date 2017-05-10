package com.medcentre.app.com.medcentre.app.http;

import java.util.EventListener;

/**
 * Created by n00134696 on 01/03/2016.
 */
public interface HttpResponseListener extends EventListener {
    public void onHttpResponse(HttpResponseEvent event);
}
