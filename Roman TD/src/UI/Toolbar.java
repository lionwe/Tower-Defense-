package UI;

import static main.GameStates.MENU;
import static main.GameStates.SetGameState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helpz.LoadSave;
import objects.Tile;
import scenes.Editing;

public class Toolbar extends Bar {
	private Editing editing;
	private MyButton bMenu, bSave;
	private MyButton bRoadStart, bRoadEnd;
	private BufferedImage roadStart, roadEnd;
	private Tile selectedTile;

// private ArrayList<MyButton> tileButtons = new ArrayList<>();
	private Map<MyButton, ArrayList<Tile>> map = new HashMap<MyButton, ArrayList<Tile>>();

	private MyButton bPlate, bWater, bRoadS, bRoadC, bWaterC, bWaterB, bWaterI, bFloor, bTowerPoint;
	private MyButton currentButton;
	private int currentIndex = 0;

	public Toolbar(int x, int y, int width, int height, Editing editing) {
		super(x, y, width, height);
		this.editing = editing;
		initPathImgs();
		initButtons();
		
	}


	private void initPathImgs() {
		roadStart = LoadSave.getSpriteAtlas().getSubimage(7 * 32, 2 * 32, 32, 32);
		roadEnd = LoadSave.getSpriteAtlas().getSubimage(8 * 32, 2 * 32, 32, 32);
		
	}

	private void initButtons() {

		bMenu = new MyButton("Menu", 2, 642, 100, 30);
		bSave = new MyButton("Save", 2, 674, 100, 30);
		

		int w = 50;
		int h = 50;
		int xStart = 110;
		int yStart = 650;
		int xOffset = (int) (w * 1.1f);
		int i = 0;

		bPlate = new MyButton("Plate", xStart, yStart, w, h, i++);
		bWater = new MyButton("Water", xStart + xOffset, yStart, w, h, i++);
		

		initMapButton(bPlate, editing.getGame().getTileManager().getRoadsS(), xStart, yStart, xOffset, w, h, i++);
		initMapButton(bRoadC, editing.getGame().getTileManager().getRoadsC(), xStart, yStart, xOffset, w, h, i++);
		initMapButton(bWaterC, editing.getGame().getTileManager().getCorners(), xStart, yStart, xOffset, w, h, i++);
		initMapButton(bWaterB, editing.getGame().getTileManager().getBeaches(), xStart, yStart, xOffset, w, h, i++);
		initMapButton(bWaterI, editing.getGame().getTileManager().getIslands(), xStart, yStart, xOffset, w, h, i++);
		initMapButton(bFloor, editing.getGame().getTileManager().getFloor(), xStart, yStart, xOffset, w, h, i++);
		initMapButton(bTowerPoint, editing.getGame().getTileManager().getTowerP(), xStart - 6 * xOffset, yStart + 1*  xOffset, xOffset, w, h, i++);


		bRoadStart = new MyButton("PathStart", xStart, yStart + xOffset, w, h, i++);
		bRoadEnd = new MyButton("PathEnd", xStart + xOffset, yStart + xOffset, w, h, i++);
		

		


	}

	private void initMapButton(MyButton b, ArrayList<Tile> list, int x, int y, int xOff, int w, int h, int id) {
		b = new MyButton("", x + xOff * id, y, w, h, id);
		map.put(b, list);
	}

	private void saveLevel() {
		editing.saveLevel();
	}

	public void rotateSprite() {

		currentIndex++;
		if (currentIndex >= map.get(currentButton).size())
			currentIndex = 0;
		selectedTile = map.get(currentButton).get(currentIndex);
		editing.setSelectedTile(selectedTile);

	}

	public void draw(Graphics g) {

		// Background
		g.setColor(new Color(181, 201, 235));
		g.fillRect(x, y, width, height);

		// Buttons
		drawButtons(g);
	}

	private void drawButtons(Graphics g) {
		bMenu.draw(g);
		bSave.draw(g);

		drawPathButton(g, bRoadStart, roadStart);
		drawPathButton(g, bRoadEnd, roadEnd);

//		bRoadStart.draw(g);
//		bRoadEnd.draw(g);

		drawNormalButton(g, bPlate);
		drawNormalButton(g, bWater);
		drawSelectedTile(g);
		drawMapButtons(g);

	}

	private void drawTowerPButton(Graphics g, MyButton b, BufferedImage img) {
		g.drawImage(img, b.x, b.y, b.width, b.height, null);
		drawButtonFeedback(g, b);
		
	}

	private void drawPathButton(Graphics g, MyButton b, BufferedImage img) {
		g.drawImage(img, b.x, b.y, b.width, b.height, null);
		drawButtonFeedback(g, b);
	

	}

	private void drawNormalButton(Graphics g, MyButton b) {
		g.drawImage(getButtImg(b.getId()), b.x, b.y, b.width, b.height, null);
		drawButtonFeedback(g, b);

	}

	private void drawMapButtons(Graphics g) {
		for (Map.Entry<MyButton, ArrayList<Tile>> entry : map.entrySet()) {
			MyButton b = entry.getKey();
			BufferedImage img = entry.getValue().get(0).getSprite();

			g.drawImage(img, b.x, b.y, b.width, b.height, null);
			drawButtonFeedback(g, b);
		}

	}

	private void drawSelectedTile(Graphics g) {

		if (selectedTile != null) {
			g.drawImage(selectedTile.getSprite(), 550, 650, 50, 50, null);
			g.setColor(Color.black);
			g.drawRect(550, 650, 50, 50);
		}

	}

	public BufferedImage getButtImg(int id) {
		return editing.getGame().getTileManager().getSprite(id);
	}

	public void mouseClicked(int x, int y) {
		if (bMenu.getBounds().contains(x, y))
			SetGameState(MENU);
		else if (bSave.getBounds().contains(x, y))
			saveLevel();
		else if (bWater.getBounds().contains(x, y)) {
			selectedTile = editing.getGame().getTileManager().getTile(bWater.getId());
			editing.setSelectedTile(selectedTile);
			return;
		} else if (bPlate.getBounds().contains(x, y)) {
			selectedTile = editing.getGame().getTileManager().getTile(bPlate.getId());
			editing.setSelectedTile(selectedTile);
			return;

		} else if (bRoadStart.getBounds().contains(x, y)) {
			selectedTile = new Tile(roadStart, -1, -1);
			editing.setSelectedTile(selectedTile);

		} else if (bRoadEnd.getBounds().contains(x, y)) {
			selectedTile = new Tile(roadEnd, -2, -2);
			editing.setSelectedTile(selectedTile);
			
			
		} else {
			for (MyButton b : map.keySet())
				if (b.getBounds().contains(x, y)) {
					selectedTile = map.get(b).get(0);
					editing.setSelectedTile(selectedTile);
					currentButton = b;
					currentIndex = 0;
					return;
				}
		}
	}

	public void mouseMoved(int x, int y) {
		bMenu.setMouseOver(false);
		bSave.setMouseOver(false);
		bWater.setMouseOver(false);
		bPlate.setMouseOver(false);
		bRoadStart.setMouseOver(false);
		bRoadEnd.setMouseOver(false);
		for (MyButton b : map.keySet())
			b.setMouseOver(false);

		if (bMenu.getBounds().contains(x, y))
			bMenu.setMouseOver(true);
		else if (bSave.getBounds().contains(x, y))
			bSave.setMouseOver(true);
		else if (bWater.getBounds().contains(x, y))
			bWater.setMouseOver(true);
		else if (bPlate.getBounds().contains(x, y))
			bPlate.setMouseOver(true);
		else if (bRoadStart.getBounds().contains(x, y))
			bRoadStart.setMouseOver(true);
		else if (bRoadEnd.getBounds().contains(x, y))
			bRoadEnd.setMouseOver(true);
		else {
			for (MyButton b : map.keySet())
				if (b.getBounds().contains(x, y)) {
					b.setMouseOver(true);
					return;
				}
		}

	}

	public void mousePressed(int x, int y) {
		if (bMenu.getBounds().contains(x, y))
			bMenu.setMousePressed(true);
		else if (bSave.getBounds().contains(x, y))
			bSave.setMousePressed(true);
		else if (bWater.getBounds().contains(x, y))
			bWater.setMousePressed(true);
		else if (bPlate.getBounds().contains(x, y))
			bPlate.setMousePressed(true);
		else if (bRoadStart.getBounds().contains(x, y))
			bRoadStart.setMousePressed(true);
		else if (bRoadEnd.getBounds().contains(x, y))
			bRoadEnd.setMousePressed(true);
		else {
			for (MyButton b : map.keySet())
				if (b.getBounds().contains(x, y)) {
					b.setMousePressed(true);
					return;
				}
		}

	}

	public void mouseReleased(int x, int y) {
		bMenu.resetBooleans();
		bSave.resetBooleans();
		bPlate.resetBooleans();
		bWater.resetBooleans();
		bRoadStart.resetBooleans();
		bRoadEnd.resetBooleans();
		for (MyButton b : map.keySet())
			b.resetBooleans();

	}

	public BufferedImage getStartRoadImg() {
		return roadStart;
	}

	public BufferedImage getEndRoadImg() {
		return roadEnd;
	}
}
