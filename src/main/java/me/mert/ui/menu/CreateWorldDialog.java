package me.mert.ui.menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import me.mert.ui.panel.RoundedPanel;
import me.mert.ui.widgets.RoundedButton;

public class CreateWorldDialog extends JDialog {

    private JTextField nameField;
    private String worldName;

    public CreateWorldDialog(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setSize(400, 220);
        setLocationRelativeTo(parent);
        setBackground(new Color(215, 215, 215));

        RoundedPanel panel = new RoundedPanel(20);
        panel.setLayout(null);
        panel.setBackground(new Color(215, 215, 215));

        setContentPane(panel);

        JLabel title = new JLabel("Enter name");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBounds(146, 20, 200, 30);
        panel.add(title);

        nameField = new JTextField();
        nameField.setBounds(50, 80, 300, 35);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameField.setBackground(Color.WHITE);
        nameField.setForeground(Color.BLACK);
        nameField.setCaretColor(Color.BLACK);
        nameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(nameField);

        RoundedButton createButton = new RoundedButton("Create",
        new Color(194, 194, 194),
                new Color(170, 170, 170));
        createButton.setBounds(125, 140, 150, 35);
        createButton.setFocusPainted(false);
        createButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        createButton.setBorder(BorderFactory.createEmptyBorder());

        createButton.addActionListener(e -> {
            worldName = nameField.getText().trim();
            dispose();
        });

        panel.add(createButton);
    }

    public String getWorldName() {
        return worldName;
    }
}