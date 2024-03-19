package scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import helpz.LoadSave;
import main.Game;
import managers.EnemyManager;
import managers.ProjecttileManager;
import managers.TileManager;
import managers.TowerManager;
import managers.WaveManager;
import objects.RoadPoint;
import objects.Tower;
import UI.ActionBar;
import enemies.Enemy;

import static helpz.Constants.Tiles.*;




public class Playing extends GameScene implements SceneMethods {

	private int[][] lvl;
	private int[][] initialLevel;
	private ActionBar actionBar;
	private int mouseX, mouseY;
	private EnemyManager enemyManager;
	private TowerManager towerManager;
	private ProjecttileManager projManager;
	private WaveManager waveManager;
	private RoadPoint start, end;
	private Tower selectedTower;
	private int goldTick;
	private boolean gamePaused;
	

	public Playing(Game game) {
		super(game);

		loadDefaultLevel();

		actionBar = new ActionBar(0, 640, 640, 160, this);

		enemyManager = new EnemyManager(this, start, end);
		towerManager = new TowerManager(this);
		projManager = new ProjecttileManager(this);
		waveManager = new WaveManager(this);

	}

	private void loadDefaultLevel() {
	    lvl = LoadSave.GetLevelData();
	    // Збереження початкового стану рівня
	    initialLevel = lvl.clone();
	    for (int i = 0; i < lvl.length; i++) {
	        initialLevel[i] = lvl[i].clone();
	    }
	    ArrayList<RoadPoint> points = LoadSave.GetLevelRoadPoints();
	    start = points.get(0);
	    end = points.get(1);
	}


	public void setLevel(int[][] lvl) {
		this.lvl = lvl;
	}

	public void update() {
		if(!gamePaused) {
			updateTick();
			waveManager.update();

			//credits tick
			goldTick++;
			if(goldTick % (60*3) == 0)
				actionBar.addGold(1);
			
			if (isAllEnemiesDead()) {
				if (isThereMoreWaves()) {
					waveManager.startWaveTimer();
					if (isWaveTimerOver()) {
						waveManager.increaseWaveIndex();
						enemyManager.getEnemies().clear();
						waveManager.resetEnemyIndex();
					}
				}
			}

			if (isTimeForNewEnemy()) {
				spawnEnemy();
			}

			enemyManager.update();
			towerManager.update();
			projManager.update();
		}
		
		

	}

	private boolean isWaveTimerOver() {

		return waveManager.iswaveTimerOver();

	}

	private boolean isThereMoreWaves() {
		return waveManager.isThereMoreWaves();

	}

	private boolean isAllEnemiesDead() {

		if (waveManager.isThereMoreEnemiesInWave()) {
			return false;
		}
		for (Enemy e : enemyManager.getEnemies())
			if (e.isAlive())
				return false;

		return true;
	}

	private void spawnEnemy() {
		enemyManager.spawnEnemy(waveManager.getNextEnemy());

	}

	private boolean isTimeForNewEnemy() {

		if (getWaveManager().isTimeForNewEnemy()) {
			if (waveManager.isThereMoreEnemiesInWave())
				return true;
		}
		return false;
	}

	public void setSeceltedTower(Tower selectedTower) {
		this.selectedTower = selectedTower;

	}

	@Override
	public void render(Graphics g) {

		drawLevel(g);
		actionBar.draw(g);
		enemyManager.draw(g);
		towerManager.draw(g);
		projManager.draw(g);
		
		drawSelectedTower(g);
		drawHighlight(g);
		
		drawWaveInfo(g);

	}

	private void drawWaveInfo(Graphics g) {

	}

	private void drawHighlight(Graphics g) {
		g.setColor(Color.PINK);
		g.drawRect(mouseX, mouseY, 32, 32);

	}

	private void drawSelectedTower(Graphics g) {
		if (selectedTower != null)
			g.drawImage(towerManager.getTowerImgs()[selectedTower.getTowerType()], mouseX, mouseY, null);
	}

	private void drawLevel(Graphics g) {
		for (int y = 0; y < lvl.length; y++) {
			for (int x = 0; x < lvl[y].length; x++) {
				int id = lvl[y][x];
				if (isAnimation(id)) {
					g.drawImage(getSprite(id, animationIndex), x * 32, y * 32, null);
				} else

					g.drawImage(getSprite(id), x * 32, y * 32, null);
			}
		}
	}

	public int getTileType(int x, int y) {
		int xCord = x / 32;
		int yCord = y / 32;

		if (xCord < 0 || xCord > 19)
			return 0;
		if (yCord < 0 || yCord > 19)
			return 0;

		int id = lvl[y / 32][x / 32];
		return game.getTileManager().getTile(id).getTileType();
	}

	@Override
	public void MouseClicked(int x, int y) {
		if (y >= 640)
			actionBar.MouseClicked(x, y);
		else {
			if (selectedTower != null) {
				if (isTilePlate(mouseX, mouseY)) {
					if (getTowerAt(mouseX, mouseY) == null) {
						towerManager.addTower(selectedTower, mouseX, mouseY);
						
						
						removeGold(selectedTower.getTowerType());
						
						selectedTower = null;
						
						
						
					}
				}
			} else {
				Tower t = getTowerAt(mouseX, mouseY);
				actionBar.displayTower(t);
			}
		}
	}

	private void removeGold(int towerType) {
		actionBar.payForTower(towerType);
		
	}
	
	public void upgradeTower(Tower displayedTower) {
		towerManager.upgradeTower(displayedTower);
		
	}

	
	public void removeTower(Tower displayedTower) {
		towerManager.removeTower(displayedTower);
	}




	private Tower getTowerAt(int x, int y) {
		return towerManager.getTowerAt(x, y);
	}

	private boolean isTilePlate(int x, int y) {
		int id = lvl[y / 32][x / 32];
		int tileType = game.getTileManager().getTile(id).getTileType();
		return tileType == TOWER_POINT;
	}


	public void shootEnemy(Tower t, Enemy e) {
		projManager.newProjecttile(t, e);

	}
	
	public void setGamePaused(boolean gamePaused) {
		this.gamePaused = gamePaused;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			selectedTower = null;
		}
	}

	@Override
	public void mouseMoved(int x, int y) {

		if (y >= 640)
			actionBar.mouseMoved(x, y);
		else {
			mouseX = (x / 32) * 32;
			mouseY = (y / 32) * 32;
		}
	}

	@Override
	public void mousePressed(int x, int y) {
		if (y >= 640) {
			actionBar.mousePressed(x, y);
		}
	}

	@Override
	public void mouseReleased(int x, int y) {
		actionBar.mouseReleased(x, y);
	}

	@Override
	public void mouseDragged(int x, int y) {

	}
	
	public void rewardPlayer(int enemyType) {
		actionBar.addGold(helpz.Constants.Enemies.GetReward(enemyType));
	}

	public TowerManager getTowerManager() {
		return towerManager;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public WaveManager getWaveManager() {
		return waveManager;
	}
	
	public boolean isGamePaused() {
		return gamePaused;
	}

	public void removelife() {
		actionBar.removelife();
		
	}

	public void resetEverything() {
		actionBar.resetEverything();
		
		
		//managers
		  enemyManager.reset();
		  towerManager.reset();
		  projManager.reset();
		  waveManager.reset();
		  
		  mouseX = 0;
		  mouseY = 0;
		  
		  selectedTower = null;
		  goldTick = 0;
		  gamePaused = false;
	}




}