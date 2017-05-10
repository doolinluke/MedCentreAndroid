package com.medcentre.app.com.medcentre.app.http;

import android.os.AsyncTask;

/**
 * Created by n00134696 on 01/03/2016.
 */
public class HttpRequestTask extends AsyncTask<HttpRequest, Integer, HttpResponse> {

    private HttpResponseListener listener;

    public synchronized void setHttpResponseListener(HttpResponseListener lstnr) {
        this.listener = lstnr;
    }

    protected synchronized void fireEvent(HttpResponseEvent event) {
        if (this.listener != null) {
            this.listener.onHttpResponse(event);
        }
    }

    @Override
    protected HttpResponse doInBackground(HttpRequest... params) {
        HttpResponse response = null;
        try {
            HttpRequest request = params[0];
            HttpClient client = new HttpClient();
            response = client.execute(request);
        }
        catch (HttpException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(HttpResponse response) {
        HttpResponseEvent event;

        if (response != null) {
            event = new HttpResponseEvent(this, response);
            fireEvent(event);
        }
    }

}
