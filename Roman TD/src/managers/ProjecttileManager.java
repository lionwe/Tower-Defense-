package managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enemies.Enemy;
import helpz.LoadSave;
import objects.Projecttile;
import objects.Tower;
import scenes.Playing;
import static helpz.Constants.Towers.*;
import static helpz.Constants.Projecttiles.*;

public class ProjecttileManager {
	private Playing playing;
	private ArrayList<Projecttile> projecttiles = new ArrayList<>();
	private ArrayList<Explosion> explosions = new ArrayList<>();
	private BufferedImage[] proj_imgs, explo_imgs;
	private int proj_id = 0;

	public ProjecttileManager(Playing playing) {
		this.playing = playing;
		importImgs();

	}

	private void importImgs() {
		BufferedImage atlas = LoadSave.getSpriteAtlas();
		proj_imgs = new BufferedImage[3];

		for (int i = 0; i < 3; i++)
			proj_imgs[i] = atlas.getSubimage((7 + i) * 32, 32, 32, 32);
		importExplosion(atlas);
	}

	private void importExplosion(BufferedImage atlas) {
		explo_imgs = new BufferedImage[7];

		for (int i = 0; i < 7; i++)
			explo_imgs[i] = atlas.getSubimage(i * 32, 32 * 2, 32, 32);

	}

	public void newProjecttile(Tower t, Enemy e) {
		int type = getProjType(t);

		int xDist = (int) (t.getX() - e.getX());
		int yDist = (int) (t.getY() - e.getY());
		int totalDist = Math.abs(xDist) + Math.abs(yDist);

		float xPer = (float) Math.abs(xDist) / totalDist;

		float xSpeed = xPer * helpz.Constants.Projecttiles.GetSpeed(type);
		float ySpeed = helpz.Constants.Projecttiles.GetSpeed(type) - xSpeed;

		if (t.getX() > e.getX())
			xSpeed *= -1;
		if (t.getY() > e.getY())
			ySpeed *= -1;

		float rotate = 0;

		if (type == BULLET) {
			float arcValue = (float) Math.atan(yDist / (float) xDist);
			rotate = (float) Math.toDegrees(arcValue);

			if (xDist < 0)
				rotate += 180;
		}
		
		for(Projecttile p : projecttiles)
			if(!p.isActive())
				if(p.getProjecttileType() == type) {
					p.reuse(t.getX() + 16, t.getY() + 16, xSpeed, ySpeed, t.getDmg(), rotate);
					return;
				}

		projecttiles.add(
				new Projecttile(t.getX() + 16, t.getY() + 16, xSpeed, ySpeed, t.getDmg(), rotate, proj_id++, type));

	}

	public void update() {
		for (Projecttile p : projecttiles)
			if (p.isActive()) {
				p.move();
				if (isProjHittingEnemy(p)) {
					p.setActive(false);
					if (p.getProjecttileType() == BULLETTUR) {
						explosions.add(new Explosion(p.getPos()));
						explodeOnEnemies(p);
					}
				} else if(isProjOutsideBounds(p)) {
					p.setActive(false);
				}
			}

		for (Explosion e : explosions)
			if (e.getIndex() < 7)
				e.update();
	}



	private void explodeOnEnemies(Projecttile p) {
		for (Enemy e : playing.getEnemyManager().getEnemies()) {
			if (e.isAlive()) {
				float radius = 40.0f;

				float xDist = Math.abs(p.getPos().x - e.getX());
				float yDist = Math.abs(p.getPos().y - e.getY());

				float realDist = (float) Math.hypot(xDist, yDist);

				if (realDist <= radius)
					e.hurt(p.getDmg());

			}

		}
	}

	private boolean isProjHittingEnemy(Projecttile p) {
		for (Enemy e : playing.getEnemyManager().getEnemies()) {
			if (e.isAlive())
				if (e.getBounds().contains(p.getPos())) {
					e.hurt(p.getDmg());
					if(p.getProjecttileType() == CHAINS)
						e.slow();
					
					return true;
				}
		}
		return false;
	}
	
	private boolean isProjOutsideBounds(Projecttile p) {
		if(p.getPos().x >= 0)
			if(p.getPos().x <= 640)
				if(p.getPos().y >=0)
					if(p.getPos().y <=880)
						return false;
		return true;
	}

	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		for (Projecttile p : projecttiles)
			if (p.isActive()) {
				if (p.getProjecttileType() == BULLET) {
					g2d.translate(p.getPos().x, p.getPos().y);
					g2d.rotate(Math.toRadians(p.getRotation()));
					g2d.drawImage(proj_imgs[p.getProjecttileType()], -16, -16, null);
					g2d.rotate(-Math.toRadians(p.getRotation()));
					g2d.translate(-p.getPos().x, -p.getPos().y);
				} else {
					g2d.drawImage(proj_imgs[p.getProjecttileType()], (int) p.getPos().x - 16, (int) p.getPos().y - 16,
							null);

				}

			}

		drawExplosions(g2d);

	}

	private void drawExplosions(Graphics2D g2d) {
		for (Explosion e : explosions)
			if (e.getIndex() < 7)
				g2d.drawImage(explo_imgs[e.getIndex()], (int) e.getPos().x - 16, (int) e.getPos().y - 16, null);

	}

	private int getProjType(Tower t) {
		switch (t.getTowerType()) {
		case TROOPER:
			return BULLET;
		case TURRET:
			return BULLETTUR;
		case MAGE:
			return CHAINS;
		}
		return 0;
	}

	public class Explosion {
		private Point2D.Float pos;
		private int exploTick = 0, exploIndex = 0;

		public Explosion(Point2D.Float pos) {
			this.pos = pos;
		}

		public void update() {

			exploTick++;
			if (exploTick >= 12) {
				exploTick = 0;
				exploIndex++;

			}

		}

		public int getIndex() {
			return exploIndex;
		}

		public Point2D.Float getPos() {
			return pos;
		}

	}
	
	public void reset() {
		projecttiles.clear();
		explosions.clear();
		
		proj_id = 0;
	}

}
