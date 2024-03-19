package com.zyf.http.framework;

import com.zyf.http.framework.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class RequestHandler {
    private final OutputStream outputStream;
    private final HttpContext httpContext;

    public RequestHandler(HttpContext httpContext) {
        this.httpContext = httpContext;
        this.outputStream = httpContext.getOutputStream();
    }

    public void handle() throws IOException {
        String url = httpContext.getUrl();

        if (url.startsWith("/echo/")) {
            String pathParam = url.replace("/echo/", "");
            out(buildTextPlainResponse(pathParam));
        } else if ("/".equals(url)) {
            out(Constants.OK_RN);
        } else if(url.startsWith("/user-agent")){
            String userAgent = httpContext.getUserAgent();
            out(buildTextPlainResponse(userAgent));
        } else if(url.startsWith("/files/")) {
            String filename = url.replace("/files/", "");
            String filepath = HttpRepository.getConfig(Constants.DIRECTORY);
            Path requestPath = Paths.get(filepath + "\\" + filename);
            boolean exists = Files.exists(requestPath, LinkOption.NOFOLLOW_LINKS);
            if (exists) {
                byte[] bytes = Files.readAllBytes(requestPath);
                out(buildOctetStreamResponse(bytes));
            } else {
                log.info("not found file:{}", filepath + "\\" + filename);
            }
        }
        out(Constants.NOT_FOUND);
    }

    private void out(String data) throws IOException {
        log.info("return {}", data);
        out(data.getBytes(StandardCharsets.UTF_8));
    }

    private void out(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    private static String buildTextPlainResponse(String response) {
        int len = response.getBytes().length;
        String rst = Constants.OK + "Content-Type: " + Constants.TEXT_PLAIN + "\r\n"
                + "Content-Length: " + len + "\r\n\r\n" + response;
        return rst;
    }

    private static byte[] buildOctetStreamResponse(byte[] fileByte) {

        String pre = Constants.OK + "Content-Type: " + Constants.APPLICATION_OCTET_STREAM + "\r\n"
                + "Content-Length: " + fileByte.length + "\r\n\r\n";
        byte[] preBYtes = pre.getBytes();
        byte[] ret = new byte[preBYtes.length + fileByte.length];
        System.arraycopy(preBYtes, 0, ret, 0, preBYtes.length);
        System.arraycopy(fileByte, 0, ret, preBYtes.length, fileByte.length);
        return ret;
    }
}
