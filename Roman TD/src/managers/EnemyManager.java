package managers;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enemies.Atlasdog;
import enemies.Enemy;
import enemies.Glados;
import enemies.Pbody;
import enemies.Stalker;
import helpz.LoadSave;
import helpz.Utilz;
import objects.RoadPoint;
import scenes.Playing;

import static helpz.Constants.Direction.*;
import static helpz.Constants.Tiles.*;
import static helpz.Constants.Enemies.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[] enemyImgs;
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private RoadPoint start, end;
	private int HPBarWidth = 20;
	private BufferedImage slowEffect;
	private int[][] roadDirArr;

	public EnemyManager(Playing playing, RoadPoint start, RoadPoint end) {
		this.playing = playing;
		enemyImgs = new BufferedImage[4];
		this.start = start;
		this.end = end;

		loadEffectImg();
		loadEnemyImgs();
		loadRoadDirArr();
		tempMethod();
	}

	private void loadRoadDirArr() {
		roadDirArr = Utilz.GetRoadDirArr(playing.getGame().getTileManager().getTypeArr(), start, end);	}

	private void tempMethod() {
		int[][] arr = Utilz.GetRoadDirArr(playing.getGame().getTileManager().getTypeArr(), start, end);

		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < arr[j].length; i++) {
				System.out.print(arr[j][i] + "|");
			}
			System.out.println();
		}
	}

	private void loadEffectImg() {
		slowEffect = LoadSave.getSpriteAtlas().getSubimage(32 * 9, 32 * 2, 32, 32);

	}

	private void loadEnemyImgs() {
		BufferedImage atlas = LoadSave.getSpriteAtlas();

		for (int i = 0; i < 4; i++)
			enemyImgs[i] = atlas.getSubimage(i * 32, 32, 32, 32);

	}

	public void update() {

		for (Enemy e : enemies)
			if (e.isAlive()) {
//				updateEnemyMove(e);
				updateEnemyMoveNew(e);
			}

	}

	private void updateEnemyMoveNew(Enemy e) {
		RoadPoint currTile = getEnemyTile(e);
		int dir = roadDirArr[currTile.getyCord()][currTile.getxCord()];

		e.move(GetSpeed(e.getEnemyType()), dir);

		RoadPoint newTile = getEnemyTile(e);

		if (!isTilesTheSame(currTile, newTile)) {
			if (isTilesTheSame(newTile, end)) {
				e.kill();
				playing.removelife();
				return;
			}
			int newDir = roadDirArr[newTile.getyCord()][newTile.getxCord()];
			if (newDir != dir) {
				e.setPos(newTile.getxCord() * 32, newTile.getyCord() * 32);
				e.setLastDir(newDir);
			}
		}
	}

	private RoadPoint getEnemyTile(Enemy e) {
		switch (e.getLastDir()) {
		case LEFT:
			return new RoadPoint((int) ((e.getX() + 31) / 32), (int) (e.getY() / 32));
		case UP:
			return new RoadPoint((int) (e.getX() / 32), (int) ((e.getY() + 31) / 32));
		case RIGHT:
		case DOWN:
			return new RoadPoint((int) (e.getX() / 32), (int) (e.getY() / 32));

		}

		return new RoadPoint((int) (e.getX() / 32), (int) (e.getY() / 32));
	}

	private boolean isTilesTheSame(RoadPoint currTile, RoadPoint newTile) {
		if (currTile.getxCord() == newTile.getxCord())
			if (currTile.getyCord() == newTile.getyCord())
				return true;
		return false;
	}

	public void updateEnemyMove(Enemy e) {
		if (e.getLastDir() == -1)
			setNewDirectionAndMove(e);

		int newX = (int) (e.getX() + getSpeedAndWidth(e.getLastDir(), e.getEnemyType()));
		int newY = (int) (e.getY() + getSpeedAndHeight(e.getLastDir(), e.getEnemyType()));

		if (getTileType(newX, newY) == ROAD_TILE) {
			e.move(GetSpeed(e.getEnemyType()), e.getLastDir());
		} else if (isAtEnd(e)) {
			e.kill();
			playing.removelife();
		} else {
			setNewDirectionAndMove(e);
		}
	}

	private void setNewDirectionAndMove(Enemy e) {
		int dir = e.getLastDir();

		int xCord = (int) (e.getX() / 32);
		int yCord = (int) (e.getY() / 32);

		fixEnemyOffsetTile(e, dir, xCord, yCord);

		if (isAtEnd(e))
			return;

		if (dir == LEFT || dir == RIGHT) {
			int newY = (int) (e.getY() + getSpeedAndHeight(UP, e.getEnemyType()));
			if (getTileType((int) e.getX(), newY) == ROAD_TILE)
				e.move(GetSpeed(e.getEnemyType()), UP);
			else
				e.move(GetSpeed(e.getEnemyType()), DOWN);
		} else {
			int newX = (int) (e.getX() + getSpeedAndWidth(RIGHT, e.getEnemyType()));
			if (getTileType(newX, (int) e.getY()) == ROAD_TILE)
				e.move(GetSpeed(e.getEnemyType()), RIGHT);
			else
				e.move(GetSpeed(e.getEnemyType()), LEFT);
		}
	}

	private void fixEnemyOffsetTile(Enemy e, int dir, int xCord, int yCord) {
		switch (dir) {
		case RIGHT:
			if (xCord < 19)
				xCord++;
			break;
		case DOWN:
			if (yCord < 19)
				yCord++;
			break;
		}
		e.setPos(xCord * 32, yCord * 32);
	}

	private boolean isAtEnd(Enemy e) {
		return e.getX() == end.getxCord() * 32 && e.getY() == end.getyCord() * 32;
	}

	private int getTileType(int x, int y) {
		return playing.getTileType(x, y);
	}

	private float getSpeedAndHeight(int dir, int enemyType) {
		if (dir == UP)
			return -GetSpeed(enemyType);
		else if (dir == DOWN)
			return GetSpeed(enemyType) + 32;
		return 0;
	}

	private float getSpeedAndWidth(int dir, int enemyType) {
		if (dir == LEFT)
			return -GetSpeed(enemyType);
		else if (dir == RIGHT)
			return GetSpeed(enemyType) + 32;
		return 0;
	}

	public void spawnEnemy(int nextEnemy) {
		addEnemy(nextEnemy);

	}

	public void addEnemy(int enemyType) {
		int x = start.getxCord() * 32;
		int y = start.getyCord() * 32;

		switch (enemyType) {
		case STALKER:
			enemies.add(new Stalker(x, y, 0, this));
			break;
		case GLADOS:
			enemies.add(new Glados(x, y, 0, this));
			break;
		case PBODY:
			enemies.add(new Pbody(x, y, 0, this));
			break;
		case ATLASDOG:
			enemies.add(new Atlasdog(x, y, 0, this));
			break;
		}
	}

	public void draw(Graphics g) {
		for (Enemy e : enemies) {
			if (e.isAlive()) {
				drawEnemy(e, g);
				drawHealthBar(e, g);
				drawEffects(e, g);
			}
		}
	}

	private void drawEffects(Enemy e, Graphics g) {
		if (e.isSlowed())
			g.drawImage(slowEffect, (int) e.getX(), (int) e.getY(), null);

	}

	private void drawHealthBar(Enemy e, Graphics g) {
		Color healthColor;

		if (e.getHealth() <= 25) {
			healthColor = Color.RED;
		} else if (e.getHealth() <= 50) {
			healthColor = Color.YELLOW;
		} else {
			healthColor = Color.GREEN;
		}

		
		g.setColor(healthColor);
		g.fillRect((int) e.getX() + 16 - (getNewBarWidth(e) / 2), (int) e.getY() - 10, getNewBarWidth(e), 5);

		
		g.setColor(Color.BLACK);
		g.drawRect((int) e.getX() + 16 - (getNewBarWidth(e) / 2), (int) e.getY() - 10, getNewBarWidth(e), 5);
	}

	private int getNewBarWidth(Enemy e) {
		return (int) (HPBarWidth * e.getHealthBarFloat());
	}

	private void drawEnemy(Enemy e, Graphics g) {
		g.drawImage(enemyImgs[e.getEnemyType()], (int) e.getX(), (int) e.getY(), null);
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public int getAmountOfAliveEnemies() {
		int size = 0;
		for (Enemy e : enemies)
			if (e.isAlive())
				size++;

		return size;
	}

	public void rewardPlayer(int enemyType) {
		playing.rewardPlayer(enemyType);

	}

	public void reset() {
		enemies.clear();
	}

}
