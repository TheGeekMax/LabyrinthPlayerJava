import camera.Camera;
import camera.CameraShow;
import kotlin.Pair;
import labyrinthe.Labyrinthe;
import pictures.PictureManager;
import tools.KeyMovement;
import tools.Vector2Int;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GamePannel extends JPanel implements CameraShow {
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
    public BufferedImage[][] showTableau = new BufferedImage[TILEMAP_WIDTH][TILEMAP_HEIGHT];
    public boolean[][] discovered = new boolean[TILEMAP_WIDTH][TILEMAP_HEIGHT];

    private Labyrinthe lab = new Labyrinthe(USABLE_WIDTH,USABLE_HEIGHT);

    //les objets utilitaire
    public Camera camera = new Camera(CASE_WIDTH,TILEMAP_WIDTH,TILEMAP_HEIGHT,GAME_WIDTH,GAME_WIDTH,PLAYER_RADIUS);
    public KeyMovement camMovement= new KeyMovement(KeyEvent.VK_Z,KeyEvent.VK_Q,KeyEvent.VK_S,KeyEvent.VK_D);


    public PictureManager picManager = new PictureManager(getClass().getResourceAsStream("/pictures/sprites.png"),16);
    GameWindow instance;
    public GamePannel(GameWindow instance){
        //les images
        picManager.addFromPicture("bg_top",0,0);
        picManager.addFromPicture("bg_corner",0,1);
        picManager.addFromPicture("bg_left",0,2);
        picManager.addFromPicture("bg",0,3);

        //fg_NUMOUT_DIR
        picManager.addFromPicture("fg_1_1",1,0);
        picManager.addFromPicture("fg_1_2",1,1);
        picManager.addFromPicture("fg_1_3",1,2);
        picManager.addFromPicture("fg_1_4",1,3);

        picManager.addFromPicture("fg_2_1",2,0);
        picManager.addFromPicture("fg_2_2",2,1);
        picManager.addFromPicture("fg_2_3",2,2);
        picManager.addFromPicture("fg_2_4",2,3);

        picManager.addFromPicture("fg_3_1",3,0);
        picManager.addFromPicture("fg_3_2",3,1);
        picManager.addFromPicture("fg_3_3",3,2);
        picManager.addFromPicture("fg_3_4",3,3);

        picManager.addFromPicture("fg_2_s1",4,0);
        picManager.addFromPicture("fg_2_s2",4,1);
        picManager.addFromPicture("fg_4",4,2);
        picManager.addFromPicture("fg",4,3);

        picManager.addFromPicture("bg_big",5,0);
        picManager.addFromPicture("player",5,1);

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
        reloadAllMap();

        this.instance = instance;

        //parametres de la fenetre
        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Pair value = camera.click((float) e.getPoint().getX(), (float) e.getPoint().getY(), instance.gamePannel);
                camera.updateHitboxAt((int)value.getFirst(),(int)value.getSecond());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });

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
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800,800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,GAME_WIDTH,GAME_WIDTH);
        camera.showView(g,this);

        //on trace le "joueur"
        Pair joueur = camera.getPLayerCanvasCoordinate();
        g.setColor(Color.RED);
        g.drawImage(picManager.getBufferedPictureFromName("player"),
                   (int) (((int) joueur.getFirst())-(CASE_WIDTH*PLAYER_RADIUS)+1),
                   (int) (((int) joueur.getSecond())-(CASE_WIDTH*PLAYER_RADIUS)+1),
                   (int) (CASE_WIDTH*PLAYER_RADIUS*2+2),
                   (int) (CASE_WIDTH*PLAYER_RADIUS*2+2),null);

        //HUD
        boolean isTransparent = true;
        for(int i = 0 ; i < TILEMAP_WIDTH;i++){
            for(int j = 0 ; j < TILEMAP_HEIGHT;j++){
                if(discovered[i][j]) {
                    if (plateau[i][j] == 0) {
                        g.setColor(new Color(0,0,0,120));
                    } else {
                        g.setColor(new Color(255,255,255,120));
                    }
                    g.fillRect(GAME_WIDTH - plateau.length * 4 + i * 4, j * 4, 4, 4);
                }
            }
        }
        Pair player = camera.getGridPlayerPosition();
        g.setColor(Color.RED);
        g.fillRect(GAME_WIDTH - plateau.length * 4 + (int)player.getFirst() * 4, (int)player.getSecond() * 4, 4, 4);
    }

    //fonctions de camera show
    public void showTile(Graphics g, int x, int y,int posX, int posY){
        g.drawImage(showTableau[x][y],posX,posY,CASE_WIDTH,CASE_WIDTH,null);
    }
    public void click(int tabX, int tabY){
        if(plateau[tabX][tabY] == 1){
            plateau[tabX][tabY] = 0;
        }else{
            plateau[tabX][tabY] = 1;
        }

        reloadAllMap();
    }

    public void gameLoop(){
        Vector2Int currentCameraMovement = camMovement.getDirrection();
        if (currentCameraMovement.getX() != 0 || currentCameraMovement.getY() != 0) {
            camera.updateCoors(PLAYER_SPEED * currentCameraMovement.getX(), PLAYER_SPEED * currentCameraMovement.getY());
            Pair player = camera.getGridPlayerPosition();
            discover((int)player.getFirst(),(int)player.getSecond());
            repaint();
        }

        try{
            Thread.sleep(10);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    //fonctions de la minimap

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

    //fonctions pour la carte
    private void reloadAllMap(){
        for(int i = 0 ; i < TILEMAP_WIDTH; i ++){
            for(int j = 0 ; j < TILEMAP_HEIGHT; j ++){
                showTableau[i][j] = getImage(i,j);
            }
        }
    }

    private BufferedImage getImage(int x, int y){
        if(plateau[x][y] == 1){
            return getFg(x,y);
        }
        //bg
        return getBg(x,y);
    }

    private BufferedImage getFg(int x, int y){
        int wallVal = (isWall(x,y-1)?1:0) + (isWall(x+1,y)?1:0)*2 + (isWall(x,y+1)?1:0)*4 + (isWall(x-1,y)?1:0)*8;
        BufferedImage out;
        switch(wallVal){
            case  1 -> out = picManager.getBufferedPictureFromName("fg_1_4");
            case  2 -> out = picManager.getBufferedPictureFromName("fg_1_1");
            case  3 -> out = picManager.getBufferedPictureFromName("fg_2_4");
            case  4 -> out = picManager.getBufferedPictureFromName("fg_1_2");
            case  5 -> out = picManager.getBufferedPictureFromName("fg_2_s2");
            case  6 -> out = picManager.getBufferedPictureFromName("fg_2_1");
            case  7 -> out = picManager.getBufferedPictureFromName("fg_3_1");
            case  8 -> out = picManager.getBufferedPictureFromName("fg_1_3");
            case  9 -> out = picManager.getBufferedPictureFromName("fg_2_3");
            case 10 -> out = picManager.getBufferedPictureFromName("fg_2_s1");
            case 11 -> out = picManager.getBufferedPictureFromName("fg_3_4");
            case 12 -> out = picManager.getBufferedPictureFromName("fg_2_2");
            case 13 -> out = picManager.getBufferedPictureFromName("fg_3_3");
            case 14 -> out = picManager.getBufferedPictureFromName("fg_3_2");
            case 15 -> out = picManager.getBufferedPictureFromName("fg_4");
            default -> out = picManager.getBufferedPictureFromName("fg");
        }
        return out;
    }

    private BufferedImage getBg(int x, int y){
        //3 cas, soit corner, soit top, soit left
        if(!isWall(x,y-1) && isWall(x-1,y-1) && !isWall(x-1,y)){
            return picManager.getBufferedPictureFromName("bg_corner");
        }else if(isWall(x,y-1) && isWall(x-1,y-1) && isWall(x-1,y)){
            return picManager.getBufferedPictureFromName("bg_big");
        }else if(isWall(x,y-1) ){
            return picManager.getBufferedPictureFromName("bg_top");
        }else if(isWall(x-1,y) ) {
            return picManager.getBufferedPictureFromName("bg_left");
        }
        return picManager.getBufferedPictureFromName("bg");
    }

    private boolean isWall(int x, int y){
        if(x < 0 || x >= TILEMAP_WIDTH || y < 0 || y >= TILEMAP_HEIGHT) return false;
        return plateau[x][y] == 1;
    }
}
