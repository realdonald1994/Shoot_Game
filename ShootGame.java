package com.donald.shoot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel {
	private Timer timer;
	private int interval = 10;
	private int flyEnterIndex;
	private int shootIndex;
	private int score = 0;
	private int state;

	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;

	public static final int WIDTH = 400;
	public static final int HEIGHT = 654;

	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage over;
	public static BufferedImage pause;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;

	public Hero hero = new Hero();
	public Bullet[] bullets = {};
	public FlyObject[] flyobjects = {};

	static {
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			over = ImageIO.read(ShootGame.class.getResource("over.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	};

	public ShootGame() {

	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintAirplane(g);
		paintHero(g);
		paintBullet(g);
		paintScore(g);
		paintState(g);

	}

	public void paintAirplane(Graphics g) {
		for (int i = 0; i < flyobjects.length; i++) {
			g.drawImage(flyobjects[i].image, flyobjects[i].x, flyobjects[i].y, null);
		}

	}

	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y, null);
	}

	public void paintBullet(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			g.drawImage(bullets[i].image, bullets[i].x, bullets[i].y, null);
		}
	}

	public void paintScore(Graphics g) {
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		g.setColor(new Color(64, 64, 64));
		g.drawString("SCORE: " + score, 10, 25);
		g.drawString("LIFE: " + hero.getLife(), 10, 45);
	}

	public void paintState(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(over, 0, 0, null);
			break;
		}
	}

	public void action() {
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					hero = new Hero();
					flyobjects = new FlyObject[0];
					bullets = new Bullet[0];
					score = 0;
					state = START;
					break;
				}
			}

			public void mouseExited(MouseEvent e) {
				if (state != GAME_OVER) {
					state = PAUSE;
				}
			}

			public void mouseEntered(MouseEvent e) {
				if (state != GAME_OVER) {
					if (state == PAUSE) {
						state = RUNNING;
					}
				}
			}

		};
		this.addMouseListener(ma);
		this.addMouseMotionListener(ma);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) {
					enterAction();
					stepAction();
					shootAction();
					crashAction();
					outofBoundAction();
					checkGameOver();
				}
				repaint();
			}
		}, interval, interval);
	}

	public void enterAction() {
		flyEnterIndex++;
		if (flyEnterIndex % 40 == 0) {
			FlyObject obj = nextOne();
			flyobjects = Arrays.copyOf(flyobjects, flyobjects.length + 1);
			flyobjects[flyobjects.length - 1] = obj;
		}
	}

	public static FlyObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(20);
		if (type == 0) {
			return new Bee();
		} else {
			return new AirPlane();
		}
	}

	public void stepAction() {
		for (int i = 0; i < flyobjects.length; i++) {
			flyobjects[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
		hero.step();
	}

	public void shootAction() {
		shootIndex++;
		if (shootIndex % 30 == 0) {
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
		}
	}

	public void crashAction() {
		for (int i = 0; i < bullets.length; i++) {
			int index = -1;
			for (int j = 0; j < flyobjects.length; j++) {
				if (flyobjects[j].shootBy(bullets[i])) {
					index = j;
					break;
				}
			}
			if (index != -1) {
				if (flyobjects[index] instanceof Enemy) {
					Enemy e = (Enemy) flyobjects[index];
					score += e.getScore();

				} else if (flyobjects[index] instanceof Award) {
					Award a = (Award) flyobjects[index];
					switch (a.getType()) {
					case Award.DOUBLE_FIRE:
						hero.addDoubleFire();
						break;
					case Award.LIFE:
						hero.addLife();
						break;
					}

				}
				FlyObject f = flyobjects[index];
				flyobjects[index] = flyobjects[flyobjects.length - 1];
				flyobjects[flyobjects.length - 1] = f;
				flyobjects = Arrays.copyOf(flyobjects, flyobjects.length - 1);
			}
		}
	}

	public void outofBoundAction() {
		int index = 0;
		FlyObject[] flylives = new FlyObject[flyobjects.length];
		for (int i = 0; i < flyobjects.length; i++) {
			if (!flyobjects[i].outofBound()) {
				flylives[index++] = flyobjects[i];
			}
		}
		flyobjects = Arrays.copyOf(flylives, index);

		index = 0;
		Bullet[] bulletlives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			if (!bullets[i].outofBound()) {
				bulletlives[index++] = bullets[i];
			}
		}
		bulletlives = Arrays.copyOf(bulletlives, index);
	}

	public void checkGameOver() {
		if (isGameOver()) {
			state = GAME_OVER;
		}
	}

	public boolean isGameOver() {
		for (int i = 0; i < flyobjects.length; i++) {
			int index = -1;
			if (hero.hit(flyobjects[i])) {
				hero.removeLife();
				hero.removeDoubleFire();
				index = i;
			}
			if (index != -1) {
				FlyObject f = flyobjects[index];
				flyobjects[index] = flyobjects[flyobjects.length - 1];
				flyobjects[flyobjects.length - 1] = f;
				flyobjects = Arrays.copyOf(flyobjects, flyobjects.length - 1);
			}
		}

		return hero.getLife() <= 0;

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("SHOOT-GAME");
		ShootGame game = new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.action();

	}

}
