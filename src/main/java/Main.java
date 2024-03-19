import com.zyf.http.framework.HttpClient;
import com.zyf.http.framework.HttpContext;
import com.zyf.http.framework.RequestHandler;
import com.zyf.http.framework.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        serverSocket = new ServerSocket(4221);
        serverSocket.setReuseAddress(true);

        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");
            HttpClient httpClient = new HttpClient(clientSocket);
            executorService.execute(httpClient::execute);
        }
    }



}
