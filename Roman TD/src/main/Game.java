package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import helpz.LoadSave;
import inputs.KeyboardListener;
import inputs.MyMouseListener;
import managers.TileManager;
import scenes.Editing;
import scenes.GameOver;
import scenes.Menu;
import scenes.Playing;
import scenes.Replay;

public class Game extends JFrame implements Runnable {
	
	private GameScreen gameScreen;


	private final double FPS_SET = 120.0;
	private final double UPS_SET = 60.0;

	private int updates;
	private long lastTimeUPS;
	
	private Thread gameThread;
	

	
	//Classes
    private Render render;
    private Menu menu;
    private Playing playing;
    private Replay replay;
    private Editing editing;
    private GameOver gameOver;
  
	private TileManager tileManager;
	
	public Game() {
		
		createDefaultLevel();
		initClasses();
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("TOWER DEFENSE STAR DUNE");
		add(gameScreen);
		pack();
		
		setVisible(true);
		
	}
	
	private void createDefaultLevel() {
		int[] arr = new int[400];
		for (int i = 0; i < arr.length; i++)
			arr[i] = 0;

		LoadSave.CreateLevel(arr);

	}

	
	private void initClasses() {
		tileManager = new TileManager();
		render = new Render(this);
		gameScreen = new GameScreen(this);
		menu = new Menu(this);
		playing = new Playing(this);
		replay = new Replay(this);
		editing = new Editing(this);
		gameOver = new GameOver(this);
	}



	private void start() {
		
		gameThread = new Thread(this) {};
		
		gameThread.start();
	}
	
	private void callUPS() {
		if(System.currentTimeMillis() - lastTimeUPS >= 1000) {
			System.out.println("UPS: " + updates);
			updates = 0;
			lastTimeUPS = System.currentTimeMillis();
		}
	}
	
	private void updateGame() {
			switch(GameStates.gameState) {
			case EDIT:
				editing.update();
				break;
			case MENU:
				break;
			case PLAYING:
				playing.update();
				break;
			case REPLAY:
				break;
			default:
				break;
			
			}
	}
	
	public static void main(String[] args) {	
		Game game = new Game();
		game.gameScreen.initInputs();
		game.start();	
	}

	@Override
	public void run() {
		
		 double timePerFrame = 1000000000.0 / FPS_SET;
		 double timePerUpdate = 1000000000.0 / UPS_SET;
		 
		 long lastFrame = System.nanoTime();
		 long lastUpdate = System.nanoTime();
		 long lastTimeCheck = System.currentTimeMillis();
		 
		 int frames = 0;
		 int updates = 0;
		 
		 long now;
		
		
		while(true) {
			
			now = System.nanoTime();
			
			//Render
			if(now - lastFrame >= timePerFrame) {
	        	repaint();
	        	lastFrame = now;
	        	frames++;
	        }
			
			//Update
			if(now - lastUpdate >= timePerUpdate) {
				updateGame();
				lastUpdate = now;
				updates++;
			}
			
//			if(System.currentTimeMillis() - lastTimeCheck >=1000) {
//				System.out.println("FPS: " + frames +  " | UPS: " + updates);
//				frames = 0;
//				updates = 0;
//				lastTimeCheck = System.currentTimeMillis();
//			}
			
		}
		
		
		//Update
		
		
		//checking FPS & UPS
		
	}
	
	//Getters and Setters;
	public Render getRender() {
		return render;
	}

	public Menu getMenu() {
		return menu;
	}


	public Playing getPlaying() {
		return playing;
	}


	public Replay getReplay() {
		return replay;
	}
	
	public Editing getEditor() {
		return editing;
	}

	public TileManager getTileManager() {
		return tileManager;
	}
	
	public GameOver getGameOver() {
		return gameOver;
	}
	
	
	
	
}
