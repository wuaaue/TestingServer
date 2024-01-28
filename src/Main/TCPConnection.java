package Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPConnection {
    private final Socket socket;
    private final Thread thread;
    private final TCPConnectionListener eventListener;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;//инициализирем слушателя событий
        this.socket = socket;//инициализирем сокет
        out = new ObjectOutputStream(socket.getOutputStream());//получаем поток вывода для сетевого соединения
        in = new ObjectInputStream(socket.getInputStream());//получаем поток ввода для сетевого соединения
        thread = new Thread(new Runnable() {//создаем отдельный поток для каждого клиента (многопоточное соединение)
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while (!thread.isInterrupted()) {
                        eventListener.onReceiveObject(TCPConnection.this, in.readObject());
                    }
                } catch (ClassNotFoundException e) {
                    eventListener.onException(TCPConnection.this, e);
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendObject(Object object) {
        try {
            out.writeObject(object);
            // out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "создано TCP соединение: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
