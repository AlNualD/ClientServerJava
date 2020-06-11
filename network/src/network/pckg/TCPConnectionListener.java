package network.pckg;

/** Интерфейс событий */
public interface TCPConnectionListener {

  void onConnectionReady(TCPConnection tcpConnection);

  void onReceiveString(TCPConnection tcpConnection, String value);

  void onDisconnect(TCPConnection tcpConnection);

  void onException(TCPConnection tcpConnection, Exception e);
}

/**
 * Соединение установлено
 *
 * @param tcpConnection класс соединения
 */

/**
 * Получена строка
 *
 * @param tcpConnection Само соединение
 * @param value строка
 */