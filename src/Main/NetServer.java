package Main;


import javax.swing.*;

public class NetServer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame frame = new ServerScreen();
            frame.setTitle("Сервер приложения");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

