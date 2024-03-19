package com.zyf.http.framework;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class HttpClient {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    Socket clientSocket;

    @SneakyThrows
    public HttpClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
    }

    public void execute() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        HttpContext httpContext = new HttpContext(outputStream);
        // parse request line
        try {
            httpContext.parseRequestMethod(reader.readLine());
        } catch (IOException e) {
            log.error("parse request line failed, e:{}", e.getMessage());
            return;
        }
        // prase request headers
        while (true) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                log.error("parse request headers failed, e:{}", e.getMessage());
                return;
            }
            if (line == null || line.isEmpty()) {
                break;
            }
            httpContext.parseHttpHeaders(line);
        }


        RequestHandler requestHandler = new RequestHandler(httpContext);
        try {
            requestHandler.handle();
        } catch (IOException e) {
            log.error("handle request failed, e:{}, stack:{}", e.getMessage(), e.getStackTrace());
        }
    }
}
