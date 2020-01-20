package chat.server;
import network.pckg.TCPConnection;
import network.pckg.TCPConnectionListener;

import java.io.*;
import  java.net.*;
import java.util.ArrayList;

/**
 * основной класс сервера для чата
 */
public class ChatServer implements TCPConnectionListener {
    public static void main(String[] args) {
        new ChatServer();


    }
    //список подключенных соединений
    private final ArrayList<TCPConnection> connections  = new ArrayList<>();
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
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllClients("Client Connected: " + tcpConnection);

    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllClients(value);

    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllClients("Client Disconnected " + tcpConnection);

    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection Exception " + e);

    }

    private void sendToAllClients(String msg){
        System.out.println("Msg: " + msg);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) {
            connections.get(i).sendMsg(msg);
        }
    }
}
