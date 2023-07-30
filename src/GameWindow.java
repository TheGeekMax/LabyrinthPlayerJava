import camera.Camera;
import kotlin.Pair;
import labyrinthe.Labyrinthe;
import pictures.PictureManager;
import tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class GameWindow extends JFrame {

    private GameState state = GameState.MAIN_MENU;

    private SceneManager sceneManager = new SceneManager();

    public GamePannel gamePannel = new GamePannel(this);
    public MainMenu mainMenu = new MainMenu(this);
    public GameWindow() throws IOException, FontFormatException {
        //les differentes scenes
        sceneManager.addScene("game", gamePannel);
        sceneManager.addScene("menu", mainMenu);

        this.setTitle("Labyrinthe player");
        this.setVisible(true);
        this.setIconImage(gamePannel.picManager.getBufferedPictureFromName("logo"));

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setState(GameState.MAIN_MENU);

        mainLoop();
    }

    private void mainLoop(){
        while(true) {
            switch(state) {
                case MAIN_MENU -> mainMenu.loop();
                case PLAYING -> gamePannel.gameLoop();
            }
        }
    }

    public void setState(GameState state) {
        this.state = state;
        switch (state){
            case PLAYING :
                gamePannel.reset();
                sceneManager.setActiveScene("game",this);
                break;

            case MAIN_MENU:
                sceneManager.setActiveScene("menu", this);
                break;
        }
    }

    public static void main(String[] args) throws IOException, FontFormatException {
        new GameWindow();
    }
}