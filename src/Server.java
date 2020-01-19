import java.net.*;
import  java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        int counter = 0;
        while (true)
        {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client accepted " + (counter++));

            OutputStreamWriter writer =  new OutputStreamWriter(clientSocket.getOutputStream());
            writer.write("<h1>header1</h1>");
            writer.flush();
            clientSocket.close();


        }
    //    serverSocket.close();

    }
}
