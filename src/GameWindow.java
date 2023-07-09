import camera.Camera;
import kotlin.Pair;
import labyrinthe.Labyrinthe;
import pictures.PictureManager;
import tools.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {
    public static final int GAME_WIDTH = 800;

    public static final int TILEMAP_WIDTH = 51;
    public static final int TILEMAP_HEIGHT = 51;

    public static final int USABLE_WIDTH = (TILEMAP_WIDTH-1)/2;
    public static final int USABLE_HEIGHT = (TILEMAP_HEIGHT-1)/2;
    public static final int CASE_WIDTH = 32;
    public static final float PLAYER_SPEED = .1f;
    public static final float PLAYER_RADIUS = .4f;
    public static final int DISCOVERY_RADIUS = 2;

    //variables du plateau
    public int[][] plateau = new int[TILEMAP_WIDTH][TILEMAP_HEIGHT];
    public boolean[][] discovered = new boolean[TILEMAP_WIDTH][TILEMAP_HEIGHT];

    private Labyrinthe lab = new Labyrinthe(USABLE_WIDTH,USABLE_HEIGHT);

    //les objets utilitaire
    public Camera camera = new Camera(CASE_WIDTH,TILEMAP_WIDTH,TILEMAP_HEIGHT,GAME_WIDTH,GAME_WIDTH,PLAYER_RADIUS);
    public KeyMovement camMovement= new KeyMovement(KeyEvent.VK_Z,KeyEvent.VK_Q,KeyEvent.VK_S,KeyEvent.VK_D);
    public GamePannel gamePannel = new GamePannel(this);

    public PictureManager picManager = new PictureManager(getClass().getResourceAsStream("/pictures/sprites.png"),16);
    public GameWindow(){
        //les images
        picManager.addFromPicture("foreground",1,0);
        picManager.addFromPicture("background",0,0);

        lab.generate(USABLE_WIDTH/2,USABLE_HEIGHT/2);
        camera.setPLayerPos(1,1);
        //on init le plateau
        for(int i = 0; i < TILEMAP_WIDTH; i ++){
            for(int j = 0; j < TILEMAP_WIDTH; j ++){
                if(lab.getPlateau()[i][j]) {
                    plateau[i][j] = 1;
                    camera.setHitboxAt(i, j, true);
                    discovered[i][j] = false;
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

        Pair player = camera.getGridPlayerPosition();
        discover((int)player.getFirst(),(int)player.getSecond());

        mainLoop();
    }

    private void mainLoop(){
        while(true) {
            Vector2Int currentCameraMovement = camMovement.getDirrection();
            if (currentCameraMovement.getX() != 0 || currentCameraMovement.getY() != 0) {
                camera.updateCoors(PLAYER_SPEED * currentCameraMovement.getX(), PLAYER_SPEED * currentCameraMovement.getY());
                Pair player = camera.getGridPlayerPosition();
                discover((int)player.getFirst(),(int)player.getSecond());
                gamePannel.repaint();
            }

            try{
                Thread.sleep(10);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void discover(int x, int y){
        for(int i = -DISCOVERY_RADIUS; i <= DISCOVERY_RADIUS; i ++){
            for(int j = -DISCOVERY_RADIUS; j <= DISCOVERY_RADIUS; j ++){
                discoverIfPossible(x+i,y+j);
            }
        }
    }

    private void discoverIfPossible(int x, int y){
        if(x < 0 ||x >= TILEMAP_WIDTH || y < 0 || y >= TILEMAP_HEIGHT) return;
        discovered[x][y] = true;
    }
    public static void main(String[] args) {
        new GameWindow();
    }
}