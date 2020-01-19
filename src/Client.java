import java.net.*;
import  java.io.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("127.0.0.1",8000);

    BufferedReader reader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
       String mess = reader.readLine();
       System.out.println(mess);

       clientSocket.close();
    }
}
