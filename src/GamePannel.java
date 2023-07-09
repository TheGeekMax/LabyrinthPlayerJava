import camera.CameraShow;
import kotlin.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePannel extends JPanel implements CameraShow {
    GameWindow instance;
    public GamePannel(GameWindow instance){
        this.instance = instance;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Pair value = instance.camera.click((float) e.getPoint().getX(), (float) e.getPoint().getY(), instance.gamePannel);
                instance.camera.updateHitboxAt((int)value.getFirst(),(int)value.getSecond());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800,800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,GameWindow.GAME_WIDTH,GameWindow.GAME_WIDTH);
        instance.camera.showView(g,this);

        //on trace le "joueur"
        Pair joueur = instance.camera.getPLayerCanvasCoordinate();
        g.setColor(Color.RED);
        g.fillRect((int) (((int) joueur.getFirst())-(GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS)+2),
                   (int) (((int) joueur.getSecond())-(GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS)+2),
                   (int) (GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS*2),
                   (int) (GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS*2));

        //HUD
        boolean isTransparent = true;
        for(int i = 0 ; i < GameWindow.TILEMAP_WIDTH;i++){
            for(int j = 0 ; j < GameWindow.TILEMAP_HEIGHT;j++){
                if(instance.discovered[i][j]) {
                    if (instance.plateau[i][j] == 0) {
                        g.setColor(new Color(0,0,0,120));
                    } else {
                        g.setColor(new Color(255,255,255,120));
                    }
                    g.fillRect(GameWindow.GAME_WIDTH - instance.plateau.length * 4 + i * 4, j * 4, 4, 4);
                }
            }
        }
        Pair player = instance.camera.getGridPlayerPosition();
        g.setColor(Color.RED);
        g.fillRect(GameWindow.GAME_WIDTH - instance.plateau.length * 4 + (int)player.getFirst() * 4, (int)player.getSecond() * 4, 4, 4);
    }

    public void showTile(Graphics g, int x, int y,int posX, int posY){
        if(instance.plateau[x][y]==1){
            g.drawImage(instance.picManager.getBufferedPictureFromName("foreground"),posX,posY,GameWindow.CASE_WIDTH,GameWindow.CASE_WIDTH,null);
        }else{
            g.drawImage(instance.picManager.getBufferedPictureFromName("background"),posX,posY,GameWindow.CASE_WIDTH,GameWindow.CASE_WIDTH,null);
        }

    }

    public void click(int tabX, int tabY){
        if(instance.plateau[tabX][tabY] == 1){
            instance.plateau[tabX][tabY] = 0;
        }else{
            instance.plateau[tabX][tabY] = 1;
        }
    }
}
