package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import UI.MyButton;
import main.Game;

import static main.GameStates.*;

public class Replay extends GameScene implements SceneMethods {

    private MyButton bMenu;
    private MyButton bReplay;

    public Replay(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons() {
        int w = 150;
        int h = w / 3;
        int x = 640 / 2 - w / 2;
        int y = 300;
        int yOffset = 100;

        bMenu = new MyButton("НІ", x, y, w, h);
        bReplay = new MyButton("ТАК", x, y + yOffset, w, h);
    }

    @Override
    public void render(Graphics g) {
        // Replay text
        g.setFont(new Font("Monospaced", Font.BOLD, 50));
        g.setColor(Color.BLACK);
        g.drawString("Переграти?", 160, 80);

        // Buttons
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        bMenu.draw(g);
        bReplay.draw(g);
    }

    public void replayGame() {
        // Reset everything
        resetAll();

        // Change state to playing
        SetGameState(PLAYING);
    }

    private void resetAll() {
        game.getPlaying().resetEverything();
    }

    @Override
    public void MouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y)) {
            SetGameState(MENU);
        } else if (bReplay.getBounds().contains(x, y)) {
            replayGame();
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        bReplay.setMouseOver(false);

        if (bMenu.getBounds().contains(x, y))
            bMenu.setMouseOver(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMouseOver(true);
    }

    @Override
    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMousePressed(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMousePressed(true);
    }

    @Override
    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
        bReplay.resetBooleans();
    }

    @Override
    public void mouseDragged(int x, int y) {
        // Do nothing
    }
}
