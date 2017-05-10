package com.medcentre.app.com.medcentre.app.http;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.util.Map;
import java.util.Set;

public class HttpClient {

    public HttpResponse execute(HttpRequest request) throws HttpException {
        URI uri;                            // request URI
        String host;                        // web server host
        int port;                           // web server port
        String method;                      // request method
        String path;                        // path to requested resource on web server

        Map<String, String> headers;        // request/response headers name/value pairs
        Set<String> names;                  // request/response header names
        String value;                       // request/response header value

        StringBuffer sbuf;                  // stringbuffer for creating request/response body
        int i;                              // counter for request parameters
        int length;                         // length of com.medcentre.app.http post request body
        String body;                        // com.medcentre.app.http post request body

        Socket socket;                      // socket to web server
        InputStream is;                     // input stream from web server
        InputStreamReader isr;              // input stream reader from web server
        BufferedReader br;                  // buffered reader from web server
        OutputStream os;                    // output stream to web server
        OutputStreamWriter osw;             // output stream writer to server server
        BufferedWriter bw;                  // buffered writer to web server

        String line;                        // line of text from com.medcentre.app.http response
        int status;                         // com.medcentre.app.http response code
        String description;                 // com.medcentre.app.http response code description
        String header;                      // com.medcentre.app.http response header name

        HttpResponse response;              // object encapsulating com.medcentre.app.http response

        try {
            uri = request.getUri();
            host = uri.getHost();
            port = uri.getPort();
            if (port == -1) {
                port = 80;
            }
            method = request.getMethod();
            path = uri.getPath();
            body = request.getBody();
            length = body.length();

            request.addHeader("Host", host);
            request.addHeader("User-Agent", "Java " + System.getProperty("java.version"));
            request.addHeader("Content-Length", "" + length);

            // open a connection to the web server
            socket = new Socket(host, port);
            // get socket input stream and wrap it in a buffered reader so it can be read line-by-line
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            // get socket output stream and wrap it in a buffered writer so it can be written to line-by-line
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os, "UTF-8");
            bw = new BufferedWriter(osw);

            // write the com.medcentre.app.http post request to the web server connection
            bw.write(method + " " + path + " HTTP/1.0\r\n");
            for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                bw.write(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
            bw.write("\r\n");
            bw.write(body);
            bw.flush();

            // create an object to encapsulate the com.medcentre.app.http response data
            response = new HttpResponse();

            // read the com.medcentre.app.http response status line
            line = br.readLine();
            // extract the response status code and its description from the com.medcentre.app.http response status line
            status = Integer.parseInt(line.substring(9, 12));
            description = line.substring(13);
            // store the response status and description
            response.setStatus(status);
            response.setDescription(description);

            // read the com.medcentre.app.http response headers - terminated by a blank line
            line = br.readLine();
            while (line != null && line.length() > 0) {
                // split response header into a header name and value
                int index = line.indexOf(": ");
                if (index != -1) {
                    header = line.substring(0, index);
                    value = line.substring(index + 2);
                    // store the response header name and value
                    response.addHeader(header, value);
                }
                line = br.readLine();
            }

            // read the com.medcentre.app.http response body
            sbuf = new StringBuffer();
            line = br.readLine();
            while (line != null) {
                sbuf.append(line + "\r\n");
                line = br.readLine();
            }
            response.setBody(sbuf.toString());

            // close the connection to the web server
            bw.close();
            br.close();
            socket.close();

            return response;
        } catch (IOException ex) {
            throw new HttpException(ex.getMessage(), ex);
        }
        /*catch (UnsupportedEncodingException e) {
            String errorMessage = "Error encoding JSON string: " + e.getMessage();
            System.out.println("HttpClient: " + errorMessage);
        }*/
    }

}
