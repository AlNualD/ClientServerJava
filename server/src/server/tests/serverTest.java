package server.tests;

//import junit.framework.TestCase;

import network.pckg.TCPConnection;
import network.pckg.TCPConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class serverTest implements TCPConnectionListener{
 TCPConnection connection;
  public static void main(String[] args) {
    //
        new serverTest();
  }


  private serverTest (){
      try {
          connection = new TCPConnection(this, "127.0.0.1", 8000); // "192.168.99.101", 8080);
          Scanner in = new Scanner(System.in);
          while (true){
              String msg = in.next();
              System.out.println("ur msg: " + msg + "$");
              if(msg.equals("EXIT")) break;
              connection.sendMsg(msg);
          }
          in.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }


      @Override
      public void onConnectionReady(TCPConnection tcpConnection) {
          System.out.println("Connection ready " + tcpConnection.toString());

      }

      @Override
      public void onReceiveString(TCPConnection tcpConnection, String value) {
            System.out.println("Receive string: " + value);
      }

      @Override
      public void onDisconnect(TCPConnection tcpConnection) {
          System.out.println("Disconnection");

      }

      @Override
      public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("Exception: " + tcpConnection + e.getMessage());
      }

}
