import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", Integer.parseInt("4221"));
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("GET /echo/abc HTTP/1.1\r\n".getBytes());
        Thread.sleep(100000);
    }
}
