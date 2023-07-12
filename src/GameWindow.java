import camera.Camera;
import kotlin.Pair;
import labyrinthe.Labyrinthe;
import pictures.PictureManager;
import tools.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {

    public GamePannel gamePannel = new GamePannel(this);
    public GameWindow(){

        this.setTitle("Labyrinthe player");
        this.setVisible(true);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(gamePannel);
        this.pack();

        //pour le mouvement


        mainLoop();
    }

    private void mainLoop(){
        while(true) {
            gamePannel.gameLoop();
        }
    }


    public static void main(String[] args) {
        new GameWindow();
    }
}