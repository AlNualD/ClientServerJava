import java.net.*;
import  java.io.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("127.0.0.1",8000);

        BufferedWriter writer =  new BufferedWriter(
                new OutputStreamWriter(
                        clientSocket.getOutputStream()));
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));

        writer.write("Hello");
        writer.newLine(); //вместо \n
        writer.flush();

       String mess = reader.readLine();
       System.out.println(mess);

       writer.close();
       reader.close();
       clientSocket.close();
    }
}
