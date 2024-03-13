import com.zyf.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class Main {
    private static List<String> paths = List.of("/");
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
        String method;
        String path;
        String httpVersion;
        String response = null;
        for (int i = 0; i < 3; i++) {
            line = reader.readLine();
            log.info("read line:{}", line);
            if (i == 0) {
                String[] chunk = line.split(" ");
                path = chunk[1];
                if (paths.contains(path)) {
                    response = Constants.OK;
                } else {
                    response = Constants.NOT_FOUND;
                }
            } else if (i == 1) {
            } else if (i == 2) {

            }
        }
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
}
