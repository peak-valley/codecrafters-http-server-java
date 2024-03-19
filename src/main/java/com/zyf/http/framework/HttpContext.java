package com.zyf.http.framework;

import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;

@Slf4j
@Data
public class HttpContext {
    private String url;
    private String userAgent;
    private String host;
    private Method method;
    private int contentLength;
    private OutputStream outputStream;
    @Setter
    private byte[] body;

    public HttpContext(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void parseHttpHeaders(String line) {
        String[] split = line.split(" ");
        String header = split[0];
        if ("HOST:".equals(header)) {
            host = header;
        } else if ("User-Agent:".equals(header)) {
            userAgent = split[1];
        } else if("Content-Length:".equals(header)){
            contentLength = Integer.parseInt(split[1]);
        } else {
            log.info("unsupported parse line:{}", line);
        }
    }

    public void parseRequestMethod(String line) {
        String[] split = line.split(" ");
        String method = split[0];
        this.method = Method.getMethod(method);
        this.url = split[1];
    }

}