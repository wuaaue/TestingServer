package Main;

import Model.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

//точка входа в приложение сервера
public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server();
    }

    private final List<TCPConnection> connections = new ArrayList<>();//список, хранящий все соединения сервера (т.е всех клиентов)


    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(Configuration.port)) {//создание сокета, через который можно будет установить соединение
            System.out.println("Сервер запущен на IP адресе " +
                    InetAddress.getLocalHost().getHostAddress());//данные об IP адресе компьютера
            System.out.println("Сервер запущен на IP адресе " +
                    InetAddress.getLocalHost().getHostName());//данные о доменном имени компьютера


            System.out.println("Сервер запущен на порте " +  serverSocket.getLocalPort());
            Base.getInstance();//получение данных, хранящися в файлах
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());//создание отдельного потока для каждого клиента
                } catch (IOException e) {
                    System.out.println("Ошибка TCP соедиения: " + e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //действия, выполнемые при создании соединения
    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);//добавление полученного соединения в список всех соединений
        System.out.println("Подключился клиент :" + tcpConnection);
        sendBase(Base.getInstance().getTests(), tcpConnection);//отправка клиенту данных о тестах
        sendBase(Base.getInstance().getUsers(), tcpConnection);//отправка клиенту данных о пользователях
        sendBase(Base.getInstance().getQuestions(), tcpConnection);//отправка клиенту данных о вопросах
    }

    //действия, выполнемые при получении объекта через сетевое соединение
    @Override
    public synchronized void onReceiveObject(TCPConnection tcpConnection, Object object) {
        if (object instanceof Command) {
            Command command = (Command) object;
            if (command.getObj().getClass() == Test.class) {
                command.setRecords(Base.getInstance().getTests());
                command.execute();
            } else if (command.getObj().getClass() == User.class) {
                command.setRecords(Base.getInstance().getUsers());
                command.execute();
            }
            if (command.getObj().getClass() == Question.class) {
                command.setRecords(Base.getInstance().getQuestions());
                command.execute();
            }
            Base.getInstance().saveBase();
            sendToAllConnections(object);
        }
    }

    //действия, выполнемые при разрыве соединения
    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        System.out.println("Клиент отключился: " + tcpConnection);
    }

    //действия, выполнемые при возникновении исключения
    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
    }

    private <T extends Serializable>
    void sendBase(List<T> records, TCPConnection tcpConnection) {
        if (records.size() != 0) {
            for (T record : records) {
                Serializable object = record;
                tcpConnection.sendObject(new AddRecord((T) object));
            }
        }
    }

    private void sendToAllConnections(Object object) {
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) {
            connections.get(i).sendObject(object);
        }
    }
}
