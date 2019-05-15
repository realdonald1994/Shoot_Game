package com.donald.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyObject {
	private BufferedImage[] images;
	private int index;
	private int doubleFire;
	private int life;

	public Hero() {
		image = ShootGame.hero1;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
		doubleFire = 0;
		life = 3;
		images = new BufferedImage[] { ShootGame.hero1, ShootGame.hero0 };
	}

	@Override
	public void step() {
		image = images[index++ / 10 % images.length];

	}

	public Bullet[] shoot() {
		int xstep = this.width / 4;
		int ystep = 20;
		if (doubleFire > 0) {
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(this.x + xstep, this.y - ystep);
			bullets[1] = new Bullet(this.x + 3 * xstep, this.y - ystep);
			return bullets;
		} else {
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(this.x + 2 * xstep, this.y - ystep);
			return bullets;
		}

	}

	public void moveTo(int x, int y) {
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}

	public void addDoubleFire() {
		doubleFire += 40;
	}

	public void addLife() {
		life++;
	}

	public int getLife() {
		return life;
	}

	@Override
	public boolean outofBound() {
		return false;
		// TODO Auto-generated method stub

	}

	public boolean hit(FlyObject object) {
		int x1 = object.x - this.width / 2;
		int x2 = object.x + object.width + this.width / 2;
		int y1 = object.y - this.height / 2;
		int y2 = object.y + object.height + this.height / 2;
		int heroX = this.x + this.width / 2;
		int heroY = this.y + this.height / 2;
		return heroX > x1 && heroX < x2 && heroY > y1 && heroY < y2;
	}
	public void removeDoubleFire() {
		doubleFire=0;
	}
	public void removeLife() {
		life--;
	}
}
