import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private ServerSocket serverSocket;

    public ConnectionHandler(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); 
                new Thread(new ReadHandler(clientSocket)).start(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}