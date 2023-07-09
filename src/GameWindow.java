import camera.Camera;
import tools.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {
    public static final int GAME_WIDTH = 800;

    public static final int TILEMAP_WIDTH = 200;
    public static final int TILEMAP_HEIGHT = 200;
    public static final int CASE_WIDTH = 50;
    public static final float PLAYER_SPEED = .05f;

    public static final float PLAYER_RADIUS = .4f;

    //variables du plateau
    public int[][] plateau = new int[TILEMAP_WIDTH][TILEMAP_HEIGHT];

    //les objets utilitaire
    public Camera camera = new Camera(CASE_WIDTH,TILEMAP_WIDTH,TILEMAP_HEIGHT,GAME_WIDTH,GAME_WIDTH,PLAYER_RADIUS);
    public KeyMovement camMovement= new KeyMovement(KeyEvent.VK_Z,KeyEvent.VK_Q,KeyEvent.VK_S,KeyEvent.VK_D);
    public GamePannel gamePannel = new GamePannel(this);
    public GameWindow(){
        //on init le plateau
        for(int i = 0; i < TILEMAP_WIDTH; i ++){
            for(int j = 0; j < TILEMAP_WIDTH; j ++){
                if(Math.random() > 0.7) {
                    plateau[i][j] = 1;
                    camera.setHitboxAt(i, j, true);
                }else{
                    plateau[i][j] = 0;
                }
            }
        }
        this.setTitle("Labyrinthe player");
        this.setVisible(true);
        this.setFocusable(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(gamePannel);
        this.pack();

        //pour le mouvement
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                camMovement.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                camMovement.keyReleased(e);
            }
        });

        mainLoop();
    }

    private void mainLoop(){
        while(true) {
            Vector2Int currentCameraMovement = camMovement.getDirrection();
            if (currentCameraMovement.getX() != 0 || currentCameraMovement.getY() != 0) {
                camera.updateCoors(PLAYER_SPEED * currentCameraMovement.getX(), PLAYER_SPEED * currentCameraMovement.getY());
                gamePannel.repaint();
            }

            try{
                Thread.sleep(10);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new GameWindow();
    }
}