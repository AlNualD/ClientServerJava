package network.pckg;

/**
 * Интерфейс событий
 */
public interface TCPConnectionListener {
    /**
     * Соединение установлено
     * @param tcpConnection класс соединения
     */
    void onConnectionReady(TCPConnection tcpConnection);

    /**
     * Получена строка
     * @param tcpConnection Само соединение
     * @param value строка
     */
    void onReceiveString(TCPConnection tcpConnection, String value);
    void onDisconnect(TCPConnection tcpConnection);
    void onException(TCPConnection tcpConnection, Exception e);
}
