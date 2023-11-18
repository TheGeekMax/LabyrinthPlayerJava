package dev.toastcie.labyrinthplayer;

import dev.toastcie.labyrinthplayer.pictures.PictureManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainMenu extends JPanel {
    public static final int CASE_WIDTH = 32;
    private static final int BLINK_RATE = 10;
    public PictureManager picManager = new PictureManager(getClass().getResourceAsStream("/pictures/mainmenu.png"), 16);
    Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/pixels.ttf"));
    private int frame = 0;
    private boolean blinkState = true;

    public MainMenu(GameWindow instance) throws IOException, FontFormatException {
        picManager.addFromPicture("bg_lt", 0, 0);
        picManager.addFromPicture("bg_lm", 0, 1);
        picManager.addFromPicture("bg_lb", 0, 2);

        picManager.addFromPicture("bg_mt", 1, 0);
        picManager.addFromPicture("bg_mm", 1, 1);
        picManager.addFromPicture("bg_mb", 1, 2);

        picManager.addFromPicture("bg_rt", 2, 0);
        picManager.addFromPicture("bg_rm", 2, 1);
        picManager.addFromPicture("bg_rb", 2, 2);

        picManager.addFromPicture("space_1", 0, 3);
        picManager.addFromPicture("space_2", 1, 3);

        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    instance.setState(GameState.PLAYING);
                }
            }
        });
    }

    public void loop() {
        repaint();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    @Override
    protected void paintComponent(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);
        int width = 800 / CASE_WIDTH;
        String xPos = "l";
        String yPos = "t";
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0) xPos = "l";
                else if (i == width - 1) xPos = "r";
                else xPos = "m";

                if (j == 0) yPos = "t";
                else if (j == width - 1) yPos = "b";
                else yPos = "m";

                g.drawImage(picManager.getBufferedPictureFromName("bg_" + xPos + yPos), i * CASE_WIDTH, j * CASE_WIDTH, CASE_WIDTH, CASE_WIDTH, null);
            }
        }

        if (frame++ == BLINK_RATE) {
            frame = 0;
            blinkState = !blinkState;
        }

        if (blinkState) {
            g.drawImage(picManager.getBufferedPictureFromName("space_1"), 400 - 32, 368, CASE_WIDTH, CASE_WIDTH, null);
            g.drawImage(picManager.getBufferedPictureFromName("space_2"), 400, 368, CASE_WIDTH, CASE_WIDTH, null);

            g.setFont(pixelFont.deriveFont(32f));
            g.setColor(Color.WHITE);

            g.drawString("press", 400 - 48 - g.getFontMetrics().stringWidth("press"), 400);
            g.drawString("to start", 400 + 48, 400);
        }
    }
}
