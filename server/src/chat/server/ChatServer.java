package chat.server;
import network.pckg.TCPConnection;
import network.pckg.TCPConnectionListener;

import java.io.*;
import  java.net.*;

/**
 * основной класс сервера для чата
 */
public class ChatServer implements TCPConnectionListener {
    public static void main(String[] args) {
        new ChatServer();


    }

    private ChatServer(){
    System.out.println("Server Running...");
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while (true){
                try{
                    new TCPConnection(this,serverSocket.accept());

                } catch (IOException e){
                    System.out.println("TCPConnection exception" + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {

    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
}
