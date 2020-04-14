package bsu.rfe.java.group6.Ozhich.own.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class AuthorizationFrame extends JFrame {

    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 250;

    private final JTextField textFieldLogin;
    private final JTextField textFieldPassword;
    private final JTextField textFieldAddress;

    private int MY_PORT = 4444;

    private Address SERVER_ADDRESS;

    private static final int FROM_FIELD_DEFAULT_COLUMNS = 25;

    public AuthorizationFrame() {
        super("Авторизация");
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        setResizable(false);
        // Центрирование окна
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
        textFieldLogin = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldLogin.setMaximumSize(textFieldLogin.getPreferredSize());
        textFieldPassword = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldPassword.setMaximumSize(textFieldPassword.getPreferredSize());
        textFieldAddress = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldAddress.setText("192.168.0.106:8080");
        SERVER_ADDRESS = new Address("192.168.0.106", 8080);
        textFieldAddress.setMaximumSize(textFieldAddress.getPreferredSize());
        final JLabel labelLogin = new JLabel("Логин");
        final JLabel labelPassword = new JLabel("Пароль");
        final JLabel labelAddress = new JLabel("Адрес");
        JButton authorizationButton = new JButton("Проверка адреса");
        authorizationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                checkConnection();
            }
        });
        // подписи
        Box vBoxLabels = Box.createVerticalBox();
        vBoxLabels.add(Box.createVerticalGlue());
        vBoxLabels.add(labelLogin);
        vBoxLabels.add(Box.createVerticalStrut(15));
        vBoxLabels.add(labelPassword);
        vBoxLabels.add(Box.createVerticalStrut(15));
        vBoxLabels.add(labelAddress);
        vBoxLabels.add(Box.createVerticalGlue());
        // текстовые поля
        Box vBoxTextFields = Box.createVerticalBox();
        vBoxTextFields.add(Box.createVerticalGlue());
        vBoxTextFields.add(textFieldLogin);
        vBoxTextFields.add(Box.createVerticalStrut(15));
        vBoxTextFields.add(textFieldPassword);
        vBoxTextFields.add(Box.createVerticalStrut(15));
        vBoxTextFields.add(textFieldAddress);
        vBoxTextFields.add(Box.createVerticalGlue());
        // кнопки
        Box vBoxButton = Box.createVerticalBox();
        vBoxButton.add(Box.createVerticalGlue());
        vBoxButton.add(authorizationButton);
        vBoxButton.add(Box.createVerticalGlue());
        // горизонтальное выравнивание
        Box hBoxContent = Box.createHorizontalBox();
        hBoxContent.add(Box.createHorizontalGlue());
        hBoxContent.add(vBoxLabels);
        hBoxContent.add(Box.createHorizontalStrut(0));
        hBoxContent.add(vBoxTextFields);
        hBoxContent.add(Box.createHorizontalStrut(0));
        hBoxContent.add(vBoxButton);
        hBoxContent.add(Box.createHorizontalGlue());
        // вертикальное выравнивание
        Box vBoxContent = Box.createVerticalBox();
        vBoxContent.add(Box.createVerticalGlue());
        vBoxContent.add(hBoxContent);
        vBoxContent.add(Box.createVerticalGlue());
        getContentPane().add(vBoxContent, BorderLayout.CENTER);
    }

    private void checkConnection() {
        try {
            FileWriter fileWriter = new FileWriter(new File("src/checkServerConnection"));
            fileWriter.write(InetAddress.getLocalHost().getHostAddress() + "\n");
            fileWriter.write(MY_PORT + "\n");
            fileWriter.write("CHECK_CONNECTION");
            fileWriter.close();
        } catch (Exception e) {
        }
        try {
            SERVER_ADDRESS.setIP(textFieldAddress.getText().toString().split(":")[0]);
            SERVER_ADDRESS.setPort(Integer.parseInt(textFieldAddress.getText().toString().split(":")[1]));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(AuthorizationFrame.this, "Ошибка в формате записи адреса", "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Socket socket = new Socket(SERVER_ADDRESS.getIP(), SERVER_ADDRESS.getPort());
            OutputStream out =
                    new DataOutputStream(socket.getOutputStream());
            FileInputStream in = new FileInputStream(new File("src/checkServerConnection.txt"));
            byte[] bt = new byte[1024];
            while (in.read(bt) > 0) {
                out.write(bt);
            }
            out.close();
            in.close();
            JOptionPane.showMessageDialog(AuthorizationFrame.this,
                    "Сервер доступен",
                    "Успешно", JOptionPane.INFORMATION_MESSAGE);
            if (textFieldLogin.getText().toString().equals("")) {
                JOptionPane.showMessageDialog(AuthorizationFrame.this,
                        "Введите логин",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            startChat();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(AuthorizationFrame.this,
                    "Сервер недоступен или неправильно указан адрес",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startChat() {
        this.setVisible(false);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainFrame mainFrame;
                try {
                    mainFrame = new MainFrame(MY_PORT, SERVER_ADDRESS, textFieldLogin.getText().toString());
                    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mainFrame.setVisible(true);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        final AuthorizationFrame authorizationFrame = new AuthorizationFrame();
        authorizationFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        authorizationFrame.setVisible(true);
    }
}
