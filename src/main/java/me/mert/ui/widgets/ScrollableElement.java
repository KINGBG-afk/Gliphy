package me.mert.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import me.mert.game.save.SaveManager;
import me.mert.ui.menu.CreateConfirmDialog;
import me.mert.ui.panel.RoundedPanel;
import me.mert.ui.panel.WorldSelectionMenu;
import me.mert.ui.window.MainWindow;

public class ScrollableElement extends RoundedPanel {

    private final JLabel worldNameLabel;
    private final JLabel coinsLabel;
    private final JLabel levelLabel;
    private final RoundedButton playButton;
    private final RoundedButton deleteButton;

    public ScrollableElement(int arc, String worldName, String coins, String level, MainWindow root,
            WorldSelectionMenu wMenu) {
        super(arc);
        setLayout(null);

        worldNameLabel = new JLabel(worldName);
        worldNameLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        worldNameLabel.setBounds(10, 10, 400, 40);

        coinsLabel = new JLabel("Coins: " + coins);
        coinsLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        coinsLabel.setBounds(20, 50, 400, 40);

        levelLabel = new JLabel("Level: " + level);
        levelLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        levelLabel.setBounds(20, 75, 400, 40);

        playButton = new RoundedButton("PLAY", Color.WHITE, new Color(230, 230, 230));
        playButton.setFont(new Font("SansSerif", Font.BOLD, 30));
        playButton.setBounds(1280, 30, 150, 60);

        deleteButton = new RoundedButton("DELETE", Color.WHITE, new Color(230, 230, 230));
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 30));
        deleteButton.setBounds(1110, 30, 150, 60);
        deleteButton.addActionListener(e -> {
            CreateConfirmDialog dialog = new CreateConfirmDialog(root);
            dialog.setVisible(true);
            boolean asnwer = dialog.getAnswer();
            if (asnwer) {
                SaveManager.delete(worldName);
                wMenu.refreshMenu();
            }
        });

        add(worldNameLabel);
        add(coinsLabel);
        add(levelLabel);
        add(playButton);
        add(deleteButton);
    }

    public void setPlayAction(ActionListener listener) {
        playButton.addActionListener(listener);
    }

}
