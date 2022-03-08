package view;

import network.client.ControllerClient;

import javax.swing.*;
import java.awt.*;

public class ViewConnect {
    public JTextField valueLogin;
    public JPasswordField valuePassword;
    public JFrame window;

    public ViewConnect(ControllerClient controller){
        this.window = new JFrame("Bomberman");
        JPanel infoPanel = new JPanel();
        JPanel globalPanel = new JPanel();

        GridLayout infoGridlayout = new GridLayout(3, 2);
        BoxLayout globalboxLayout = new BoxLayout(globalPanel, BoxLayout.Y_AXIS);


        JLabel labelLogin = new JLabel("Login ", SwingConstants.LEFT);
        JLabel labelPassword = new JLabel("Password", SwingConstants.LEFT);
        valueLogin = new JTextField("", SwingConstants.CENTER);
        valuePassword = new JPasswordField("", SwingConstants.CENTER);
        JButton buttonConnect= new JButton("Login");


        buttonConnect.addActionListener(val -> {
            controller.login(valueLogin.getText(), String.valueOf(valuePassword.getPassword()), window);
        });

        infoPanel.setLayout(infoGridlayout);
        globalPanel.setLayout(globalboxLayout);

        infoPanel.add(labelLogin, 0);
        infoPanel.add(valueLogin, 1);
        infoPanel.add(labelPassword, 2);
        infoPanel.add(valuePassword, 3);

        JLabel labelConnect = new JLabel("Please enter your login and password");

        labelConnect.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        buttonConnect.setAlignmentX(JButton.CENTER_ALIGNMENT);

        globalPanel.add(labelConnect);
        globalPanel.add(infoPanel);
        globalPanel.add(buttonConnect);

        window.add(globalPanel);
        window.setResizable(false);
        window.setSize(new Dimension(300, 150));
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void setVisible(boolean visible) {
    this.window.setVisible(visible);
    }

}
