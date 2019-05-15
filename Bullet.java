package com.donald.shoot;

import java.util.Random;

public class Bullet extends FlyObject{
	private int speed = 3;
	public Bullet(int x, int y) {
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}
	@Override
	public void step() {
		y -= speed;
		
	}
	@Override
	public boolean outofBound() {
		return y<-height;
		// TODO Auto-generated method stub
		
	}
}
