package bsu.rfe.java.group6.Ozhich.own.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class MainFrame extends JFrame {

    private static final String FRAME_TITLE = "Клиент";

    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 700;

    private static final int FROM_FIELD_DEFAULT_COLUMNS = 10;

    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;

    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;

    private static int MY_PORT = 4444;
    private final String MY_ADDRESS = InetAddress.getLocalHost().getHostAddress();
    private Address SERVER_ADDRESS = new Address("192.168.43.159", 8080);

    private final JTextField textFieldLogin;
    private final JPasswordField textFieldPassword;
    private final JTextField textFieldAddress;

    final JButton registrationButton;
    final JButton authorizationButton;
    final JButton sendButton;

    private final JTextArea textAreaIncoming;
    private final JTextArea textAreaOutgoing;

    private boolean enter = false;
    private boolean ctrl = false;

    private final MainFrame THIS = this;

    public MainFrame() throws UnknownHostException {
        super(FRAME_TITLE);
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        // Центрирование окна
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
        // Текстовая область для отображения полученных сообщений
        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEnabled(false);
        textAreaIncoming.setSelectedTextColor(Color.BLACK);
        textAreaIncoming.setDisabledTextColor(Color.BLACK);
        // Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);

        JLabel labelLogin = new JLabel("Login");
        JLabel labelPassword = new JLabel("Password");
        JLabel labelAddress = new JLabel("Address");

        textFieldLogin = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldPassword = new JPasswordField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldAddress = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldAddress.setText("192.168.43.159:8080");

        // Текстовая область для ввода сообщений
        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
        textAreaOutgoing.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        enter = true;
                        break;
                    case KeyEvent.VK_CONTROL:
                        ctrl = true;
                        break;
                }
                if (ctrl && enter) {
                    sendMessage("SEND_ALL");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        enter = false;
                        break;
                    case KeyEvent.VK_CONTROL:
                        ctrl = false;
                        break;
                }
            }
        });
        // Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);
        // Кнопка отправки сообщения
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendMessage("SEND_ALL");
            }
        });
        authorizationButton = new JButton("Authorization");
        authorizationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendMessage("AUTHORIZATION");
            }
        });
        registrationButton = new JButton("Registration");
        registrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendMessage("REGISTRATION");
            }
        });
        // компановка элементов
        final JPanel messagePanel = new JPanel();
        messagePanel.setBorder(
                BorderFactory.createTitledBorder("Сообщение"));

        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelLogin)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldLogin)
                                .addGap(LARGE_GAP)
                                .addComponent(authorizationButton))
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelPassword)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldPassword)
                                .addGap(LARGE_GAP)
                                .addComponent(registrationButton))
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelAddress)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldAddress))
                        .addComponent(scrollPaneOutgoing)
                        .addComponent(sendButton))
                .addContainerGap());

        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addContainerGap().addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelLogin)
                        .addComponent(textFieldLogin)
                        .addComponent(authorizationButton))
                .addGap(MEDIUM_GAP)
                .addContainerGap().addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelPassword)
                        .addComponent(textFieldPassword)
                        .addComponent(registrationButton))
                .addGap(MEDIUM_GAP)
                .addContainerGap().addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelAddress)
                        .addComponent(textFieldAddress))
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneOutgoing)
                .addGap(MEDIUM_GAP)
                .addComponent(sendButton)
                .addContainerGap());
        // Компоновка элементов фрейма
        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                        .addComponent(scrollPaneIncoming)
                        .addComponent(messagePanel))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(messagePanel)
                .addContainerGap());

        //

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(MY_PORT);
                    while (!Thread.interrupted()) {
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        // Читаем из потока
                        final String type = in.readUTF();
                        if (type.toUpperCase().equals("AUTHORIZATION")) {
                            if (!in.readBoolean()) {
                                JOptionPane.showMessageDialog(THIS, "Введены неверные данные или пользователь не существует", "Ошибка",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                sendButton.setEnabled(true);
                                registrationButton.setEnabled(false);
                                authorizationButton.setEnabled(false);
                                textFieldLogin.setEnabled(false);
                                textFieldPassword.setEnabled(false);
                                textFieldAddress.setEnabled(false);
                            }
                        } else if (type.toUpperCase().equals("REGISTRATION")) {
                            if (!in.readBoolean()) {
                                JOptionPane.showMessageDialog(THIS, "Такой пользователь уже существует", "Ошибка",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                sendButton.setEnabled(true);
                                registrationButton.setEnabled(false);
                                authorizationButton.setEnabled(false);
                                textFieldLogin.setEnabled(false);
                                textFieldPassword.setEnabled(false);
                                textFieldAddress.setEnabled(false);
                            }
                        } else if (type.toUpperCase().equals("SEND_ALL")) {
                            // Выводим сообщение в текстовую область
                            final String name = in.readUTF();
                            final String message = in.readUTF();
                            textAreaIncoming.append(">>>" + name + "<<<\n" + message + "\n");
                        }
                        // Закрываем соединение
                        socket.close();
                        // почему-то не скролит до конца
                        scrollPaneIncoming.revalidate();
                        scrollPaneIncoming.getVerticalScrollBar().setValue(
                                scrollPaneIncoming.getVerticalScrollBar().getMaximum());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в работе сервера",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    private void sendMessage(String type) {
        try {
            SERVER_ADDRESS = new Address(textFieldAddress.getText().split(":")[0],
                    Integer.parseInt(textFieldAddress.getText().split(":")[1]));

            // Получаем необходимые параметры
            final String login = textFieldLogin.getText();
            final String password = new String(textFieldPassword.getPassword());
            final String message = textAreaOutgoing.getText();
            // Убеждаемся, что поля не пустые
            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите имя отправителя", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите пароль", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (type.toUpperCase().equals("AUTHORIZATION")) {
                // Создадим сокет для соединения
                final Socket socket = new Socket(SERVER_ADDRESS.getIP(), SERVER_ADDRESS.getPort());
                // Открываем поток вывода данных
                final DataOutputStream out =
                        new DataOutputStream(socket.getOutputStream());
                out.writeUTF("AUTHORIZATION");
                out.writeUTF(InetAddress.getLocalHost().getHostAddress());
                out.writeUTF(String.valueOf(MY_PORT));
                out.writeUTF(login);
                out.writeUTF(password);
                socket.close();
            } else if (type.toUpperCase().equals("REGISTRATION")) {
                // Создадим сокет для соединения
                final Socket socket = new Socket(SERVER_ADDRESS.getIP(), SERVER_ADDRESS.getPort());
                // Открываем поток вывода данных
                final DataOutputStream out =
                        new DataOutputStream(socket.getOutputStream());
                out.writeUTF("REGISTRATION");
                out.writeUTF(InetAddress.getLocalHost().getHostAddress());
                out.writeUTF(String.valueOf(MY_PORT));
                out.writeUTF(login);
                out.writeUTF(password);
                socket.close();
            } else if (type.toUpperCase().equals("SEND_ALL")) {
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Введите текст сообщения", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Создадим сокет для соединения
                final Socket socket = new Socket(SERVER_ADDRESS.getIP(), SERVER_ADDRESS.getPort());
                // Открываем поток вывода данных
                final DataOutputStream out =
                        new DataOutputStream(socket.getOutputStream());
                out.writeUTF("SEND_ALL");
                out.writeUTF(InetAddress.getLocalHost().getHostAddress());
                out.writeUTF(String.valueOf(MY_PORT));
                out.writeUTF(login);
                out.writeUTF(message);
                socket.close();
            }
            // Очищаем текстовую область ввода сообщения
            textAreaOutgoing.setText("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Не удалось отправить сообщение: узел-адресат не найден",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Не удалось отправить сообщение",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainFrame frame;
                try {
                    frame = new MainFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
