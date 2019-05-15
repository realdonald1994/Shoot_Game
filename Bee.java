package com.donald.shoot;
import java.util.Random;
public class Bee extends FlyObject implements Award{
	private int xSpeed = 1;
	private int ySpeed = 2;
	private int awardType;
	
	public Bee() {
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
		awardType = rand.nextInt(2);
	}
	
	
	@Override
	public int getType() {
		return awardType;
	}


	@Override
	public void step() {
		y += ySpeed;
		x += xSpeed;
		if (x<0) {
			xSpeed = 1;
		}
		if(x>ShootGame.WIDTH-width) {
			xSpeed = -1;
		}
		
	}


	@Override
	public boolean outofBound() {
		return y>ShootGame.HEIGHT;
		// TODO Auto-generated method stub
		
	}


}
