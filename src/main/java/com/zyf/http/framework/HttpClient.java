package com.zyf.http.framework;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class HttpClient {
    private final InputStream is;
    private final OutputStream outputStream;
    Socket clientSocket;

    @SneakyThrows
    public HttpClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.is = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
    }

    public void execute() {

//        InputStreamReader in = new InputStreamReader(is);
//        BufferedReader reader = new BufferedReader(in);
        String line;

        HttpContext httpContext = new HttpContext(outputStream);
        // parse request line
        try {
            httpContext.parseRequestMethod(readLine());
        } catch (IOException e) {
            log.error("parse request line failed, e:{}", e.getMessage());
            return;
        }
        // prase request headers
        while (true) {
            try {
                line = readLine();
            } catch (IOException e) {
                log.error("parse request headers failed, e:{}", e.getMessage());
                return;
            }
            if (line == null || line.isEmpty()) {
                break;
            }
            httpContext.parseHttpHeaders(line);
        }

        if (httpContext.getContentLength() > 0) {
            try {
                byte[] body = new byte[httpContext.getContentLength()];
                int read = is.read(body, 0, httpContext.getContentLength());
                log.info("read length:{}", read);
                httpContext.setBody(body);
            } catch (IOException e) {
                log.error("read body failed, e:{}, stack:{}", e.getMessage(), e.getStackTrace());
            }
        }

        RequestHandler requestHandler = new RequestHandler(httpContext);
        try {
            requestHandler.handle();
        } catch (IOException e) {
            log.error("handle request failed, e:{}, stack:{}", e.getMessage(), e.getStackTrace());
        }
    }

    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[1];
        int read;
        while ((read = is.read(bytes, 0, 1)) > 0) {
            if (bytes[0] == '\r') {
                continue;
            }
            if (bytes[0] == '\n') {
                break;
            }
            sb.append(new String(bytes));
        }
        return sb.toString();
    }
}
