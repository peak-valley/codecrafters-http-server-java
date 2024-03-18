import com.zyf.http.framework.HttpContext;
import com.zyf.http.framework.RequestHandler;
import com.zyf.http.framework.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        serverSocket = new ServerSocket(4221);
        serverSocket.setReuseAddress(true);
        clientSocket = serverSocket.accept(); // Wait for connection from client.
        System.out.println("accepted new connection");

        OutputStream outputStream = clientSocket.getOutputStream();
        InputStream inputStream = clientSocket.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        HttpContext httpContext = new HttpContext(outputStream);
        // parse request line
        httpContext.parseRequestMethod(reader.readLine());
        // prase request headers
        while (true) {
            line = reader.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            httpContext.parseHttpHeaders(line);
        }


        RequestHandler requestHandler = new RequestHandler();
        requestHandler.handle(httpContext);
    }



}
