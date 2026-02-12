package me.mert.core;

import java.awt.Image;

import javax.swing.ImageIcon;


public class GliphyUtilities {
    public static ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(
                GliphyUtilities.class.getResource(path));

        Image scaled = icon.getImage().getScaledInstance(
                width,
                height,
                Image.SCALE_SMOOTH);

        return new ImageIcon(scaled);
    }
}
