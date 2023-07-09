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
        g.drawRect((int) (((int) joueur.getFirst())-(GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS)),
                   (int) (((int) joueur.getSecond())-(GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS)),
                   (int) (GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS*2),
                   (int) (GameWindow.CASE_WIDTH*GameWindow.PLAYER_RADIUS*2));
    }

    public void showTile(Graphics g, int x, int y,int posX, int posY){
        g.setColor(instance.plateau[x][y]==1 ? Color.WHITE:Color.BLACK);
        g.fillRect(posX,posY,GameWindow.CASE_WIDTH,GameWindow.CASE_WIDTH);
    }

    public void click(int tabX, int tabY){
        if(instance.plateau[tabX][tabY] == 1){
            instance.plateau[tabX][tabY] = 0;
        }else{
            instance.plateau[tabX][tabY] = 1;
        }
    }
}
