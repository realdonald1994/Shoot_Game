package com.donald.shoot;

import java.util.Random;

public class AirPlane extends FlyObject implements Enemy {
	private int speed = 2;
	public AirPlane() {
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
	}
	@Override
	public int getScore() {
		return 5;
	}
	@Override
	public void step() {
		y += speed;
		
	}
	@Override
	public boolean outofBound() {
		return y>ShootGame.HEIGHT;
		
		
	}
	
	
}
