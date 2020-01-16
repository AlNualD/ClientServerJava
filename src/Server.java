import java.net.*;
import  java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);

        serverSocket.accept();

        serverSocket.close();
    }
}
