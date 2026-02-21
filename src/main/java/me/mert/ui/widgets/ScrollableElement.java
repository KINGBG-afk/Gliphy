package me.mert.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import me.mert.ui.panel.RoundedPanel;

public class ScrollableElement extends RoundedPanel {

    private final JLabel worldNameLabel;
    private final JLabel coinsLabel;
    private final JLabel levelLabel;
    private final  RoundedButton playButton;

    public ScrollableElement(int arc, String worldName, String coins, String level) {
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

        add(worldNameLabel);
        add(coinsLabel);
        add(levelLabel);
        add(playButton);
    }

    public void setPlayAction(ActionListener listener) {
        playButton.addActionListener(listener);
    }

}
