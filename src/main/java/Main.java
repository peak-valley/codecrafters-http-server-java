import com.zyf.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class Main {
    private static List<String> paths = List.of("/echo");
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
        String params;
        String data = null;
        for (int i = 0; i < 3; i++) {
            line = reader.readLine();
            log.info("read line:{}", line);
            if (i == 0) {
                String[] chunk = line.split(" ");
                path = chunk[1];
                boolean exist = false;
                for (String s : paths) {
                    if (path.startsWith(s)) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    int i1 = path.lastIndexOf("/");
                    String pathParam = path.substring(i1 + 1);
                    if (!pathParam.isEmpty()) {
                        data = pathParam;
                        break;
                    }
                } else if ("/".equals(path)) {
                    data = Constants.OK_RN;
                } else {
                    data = Constants.NOT_FOUND;
                }
            } else if (i == 1) {
            } else if (i == 2) {

            }
        }
        String response = buildResponse(data);
        log.info("return {}", response);
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    private static String buildResponse(String response) {
        String rst = Constants.OK + "Content-Type: " + Constants.TEXT_PLAIN + "\r\n"
                + "Content-Length: " + response.length() + "\r\n\r\n" + response;
        return rst;
    }

}
