package Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ServerScreen extends JFrame {
    private JButton btStart;
    private JButton btStop;
    private JTextArea messages;
    private Thread connectThread;

    public ServerScreen()  {

        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);

        final int TEXT_ROWS = 20;
        final int TEXT_COLUMNS = 60;
        messages = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        add(new JScrollPane(messages));

        btStart= new JButton("Старт");
        btStop = new JButton("Стоп");
        btStop.setEnabled(false);

        panel.add(btStart);
        panel.add(btStop);

        btStart.addActionListener(event->{
            btStart.setEnabled(false);
            btStop.setEnabled(true);
           connectThread = new Thread(()->
           {
               try{
                   startServer();
               }catch (IOException e){
                   messages.append("\nСоединение прервано:" +e);
               }
           });
           connectThread.start();
        });

        btStop.addActionListener(event->{
            connectThread.interrupt();
            btStart.setEnabled(true);
            btStop.setEnabled(false);
        });

        pack();

    }


    void startServer () throws IOException{

    }


}
