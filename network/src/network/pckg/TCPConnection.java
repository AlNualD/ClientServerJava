package network.pckg;

import java.io.*;
import java.net.Socket;

public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread; //поток слушающий входящие соединения
    private BufferedWriter out;
    private BufferedReader in;

    public TCPConnection(Socket socket) throws IOException {
        this.socket = socket;
       in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
       rxThread = new Thread(new Runnable() {
           @Override
           public void run() {
               try { //отлавливаем исключение
                   String msg = in.readLine();
               } catch (IOException e) {

               } finally {
                   
               }
           }
       });
       rxThread.start();
    }

}
