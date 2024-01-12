import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");
      OutputStream outputStream = clientSocket.getOutputStream();
      String response = "HTTP/1.1 200 OK\r\n\r\n";
      outputStream.write(response.getBytes(StandardCharsets.UTF_8));
      outputStream.flush();
    } catch (IOException e) {
      System.out.println("IOException: " +e.getMessage());
    }
  }
}
