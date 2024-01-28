package Main;

//интерфейс, описывающий поведение слушателя TCP соединения
public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection tcpConnection);//действия, выполнемые при создании соединения
    void onReceiveObject(TCPConnection tcpConnection, Object object);//действия, выполнемые при получении объекта через сетевое соединение
    void onDisconnect(TCPConnection tcpConnection);//действия, выполнемые при разрыве соединения
    void onException(TCPConnection tcpConnection, Exception e);//действия, выполнемые при возникновении исключения
}
