import java.io.*;
import java.net.*;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(8000);
    int counter = 0;
    while (true) {
      Socket clientSocket = serverSocket.accept();
      System.out.println("Client accepted " + (counter++));

      OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      String req = reader.readLine();
      String res = "#" + counter + ", Mess length " + req.length();
      writer.write(res + "\n");
      writer.flush();

      reader.close();
      writer.close();
      clientSocket.close();
    }
    //    serverSocket.close();

  }
}
