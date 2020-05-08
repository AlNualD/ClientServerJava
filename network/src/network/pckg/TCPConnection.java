package network.pckg;

import java.io.*;
import java.net.Socket;
import user.pckg.UserInf;

/** Класс для универсального взаимодействия с сетью */
public class TCPConnection {
  /** Сокет используемый для серверной и клиентской части */
  private final Socket socket;
  /** Поток для прослушивания новых подключений */
  private final Thread rxThread; // поток слушающий входящие соединения

  private final TCPConnectionListener eventListener;
  /** Обертка для вывода в поток */
  private BufferedWriter out;
  /** Обертка для чтения из потока */
  private BufferedReader in;

  private UserInf user;

  public TCPConnection(TCPConnectionListener eventListener, String ipAdr, int port)
      throws IOException {
    this(eventListener, new Socket(ipAdr, port));
  }

  public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
    this.socket = socket;
    this.eventListener = eventListener;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    rxThread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                try { // отлавливаем исключение
                  // если все хорошо, то происходит событие
                  eventListener.onConnectionReady(TCPConnection.this);
                  while (!rxThread.isInterrupted() )//&& !rxThread.isAlive()) // пока поток не прерван
                  {
                    String msg = in.readLine();
                    eventListener.onReceiveString(TCPConnection.this, msg);
                  }

                } catch (IOException e) {
                  eventListener.onException(TCPConnection.this, e);

                } finally {
                  eventListener.onDisconnect(TCPConnection.this);
                }
              }
            });
    rxThread.start();
  }

  /**
   * метод отправки сообщения
   *
   * @param msg сообщение
   */
  public synchronized void sendMsg(String msg) {
    try {
      out.write(msg + "\r\n"); // r перевод коретки
      out.flush();
    } catch (IOException e) {
      eventListener.onException(TCPConnection.this, e);
      disconnect();
    }
  }

  /** метод обрывания соединения */
  public synchronized void disconnect() { // synchronized для потокобезопасности
    rxThread.interrupt();
    try {
      socket.close();
    } catch (IOException e) {
      eventListener.onException(TCPConnection.this, e);
    }
  }

  @Override
  public String toString() { // переопределяем чтобы получать читабельный лог
    return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
  }

  public synchronized UserInf getUser() {
    return user;
  }

  public synchronized void setUser(UserInf user) {
    this.user = user;
  }
}
