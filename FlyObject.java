package com.donald.shoot;

import java.awt.image.BufferedImage;

public abstract class FlyObject {
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	protected BufferedImage image;

	public abstract void step();

	public boolean shootBy(Bullet b) {

		return b.x > this.x && x < this.x + width && b.y > this.y && y < this.y + height;

	}
	public abstract boolean outofBound();
}