import com.zyf.constant.Constants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

    public class Main {
    private static List<String> paths = List.of("/");
    public static void main(String[] args) {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
        serverSocket = new ServerSocket(4221);
        serverSocket.setReuseAddress(true);
        clientSocket = serverSocket.accept(); // Wait for connection from client.
        System.out.println("accepted new connection");

        OutputStream outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String line = reader.readLine();
        String[] chunk = line.split(" ");
        String path = chunk[1];
        String response;
        if (paths.contains(path)) {
            response = Constants.OK;
      } else {
            response = Constants.NOT_FOUND;
        }

        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    } catch (IOException e) {
        System.out.println("IOException: " +e.getMessage());
    }
    }
}
