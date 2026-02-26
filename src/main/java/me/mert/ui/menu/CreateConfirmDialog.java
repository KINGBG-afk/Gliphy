package me.mert.ui.menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import me.mert.ui.panel.RoundedPanel;
import me.mert.ui.widgets.RoundedButton;

public class CreateConfirmDialog extends JDialog {
    private boolean answer;

    private final RoundedButton cancelButton;
    private final RoundedButton deleteButton;

    public CreateConfirmDialog(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setSize(400, 180);
        setLocationRelativeTo(parent);
        setBackground(new Color(215, 215, 215));

        RoundedPanel panel = new RoundedPanel(20);
        panel.setLayout(null);
        panel.setBackground(new Color(215, 215, 215));

        setContentPane(panel);

        JLabel title = new JLabel("Do you want to delete");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setBounds(70, 20, 300, 30);

        JLabel title1 = new JLabel("this world?");
        title1.setForeground(Color.BLACK);
        title1.setFont(new Font("SansSerif", Font.BOLD, 25));
        title1.setBounds(130, 45, 300, 30);

        deleteButton = new RoundedButton("Delete", Color.WHITE, new Color(230, 230, 230));
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        deleteButton.setBounds(70, 120, 120, 40);
        deleteButton.addActionListener(e -> {
            answer = true;
            dispose();
        });

        cancelButton = new RoundedButton("Cancel", Color.WHITE, new Color(230, 230, 230));
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        cancelButton.setBounds(210, 120, 120, 40);
        cancelButton.addActionListener(e -> {
            answer = false;
            dispose();
        });

        panel.add(title);
        panel.add(title1);
        panel.add(cancelButton);
        panel.add(deleteButton);
    }

    public boolean getAnswer() {
        return answer;
    }

}
