package com.zyf.http.framework;

import com.zyf.http.framework.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class RequestHandler {

    private final static List<String> paths = List.of("/echo/", "/user-agent");
    public void handle(HttpContext httpContext) throws IOException {
        OutputStream outputStream = httpContext.getOutputStream();
        String url = httpContext.getUrl();

        if (url.startsWith("/echo/")) {
            String pathParam = url.replace("/echo/", "");
            out(buildResponse(pathParam), outputStream);
        } else if ("/".equals(url)) {
            out(Constants.OK_RN, outputStream);
        } else if(url.startsWith("/user-agent")){
            String userAgent = httpContext.getUserAgent();
            out(buildResponse(userAgent), outputStream);
        }else {
            out(Constants.NOT_FOUND, outputStream);
        }
    }

    private static void out(String data, OutputStream outputStream) throws IOException {
        log.info("return {}", data);
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    private static String buildResponse(String response) {
        int len = response.getBytes().length;
        String rst = Constants.OK + "Content-Type: " + Constants.TEXT_PLAIN + "\r\n"
                + "Content-Length: " + len + "\r\n\r\n" + response;
        return rst;
    }
}
