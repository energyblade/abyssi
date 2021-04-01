package com.badlogic.Abyssi;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.abyssi.Abyssi;
import com.mygdx.abyssi.Enemy;
import com.mygdx.abyssi.HealthBar;
import com.mygdx.abyssi.Player;

public class GameScreen implements Screen{
	final Abyssi game;
	
	public SpriteBatch batch; 
	public Sprite knightS;
	private Texture knight, goblin, sword, door, openDoor, instructions;
	private OrthographicCamera camera;
	
	
	public Rectangle tempP, tempS;

	
	BitmapFont font;
	CharSequence str;
	
	
	private Array<Enemy> enemies;
	private int maxMonsters = 5;
	private int currentMonsters = 0;
	public Player P = new Player();
	
	public int gracePeriod = 0;
	public boolean grace = false;
	public boolean monstersSpawned = false;
	public boolean fightingRoom = false, startRoom = true, isFighting = false;
	
	public Pixmap pixmap;
	public TextureRegionDrawable drawable;
	private HealthBar healthBar;
	private Stage stage;
	
	public ProgressBarStyle progressBarStyle;
	
	public GameScreen(final Abyssi game) {
		this.game = game;
		
		font = new BitmapFont();

		Pixmap pm = new Pixmap(Gdx.files.internal("crosshair.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		pm.dispose();

		knight = new Texture("knight.png");
		goblin = new Texture("goblin.png");
		sword = new Texture("sword.png");
		door = new Texture("door.png");
		openDoor = new Texture("opendoor.png");
		instructions = new Texture("instructions.png");
		knightS = new Sprite(knight);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();

		stage = new Stage();
		
		healthBar = new HealthBar(400, 40);
		healthBar.setPosition(10, 20);
		stage.addActor(healthBar);
		
		// temp = new Rectangle();

		enemies = new Array<Enemy>();
//		goblins = new Array<Rectangle>();

		for (int i = 0; i < maxMonsters; i++) {
			spawnMonster();
		}

	}

	private void spawnMonster() {
		Enemy enemy = new Enemy(1, 100, 10, MathUtils.random(64, 1800), MathUtils.random(0, 500), true, 0);
		enemy.setHealth(0);
		enemies.add(enemy);
	}

	private void respawnMonsters() {
		for (Enemy enemy : enemies) {
			System.out.println("reviving...");
			enemy.setHealth(100);
			enemy.setPos(MathUtils.random(64, 1800), MathUtils.random(400, 900));
		}
	}
	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		healthBar.setValue(P.getHealth() / P.getMaxHealth());
		str = P.getHealth() + "/" + P.getMaxHealth();
		font.draw(batch, str, 170, 45);
		
		stage.draw();
		stage.act();

		
		
		P.move(mousePos.x, mousePos.y);
		roomEvents();
		 
		for (Enemy enemy : enemies) {
			if (enemy.getIsAlive()) {
				batch.draw(goblin, enemy.getXPos(), enemy.getYPos());
				str = enemy.getHealth() + "";
				font.draw(batch, str, enemy.getXPos() + 20, enemy.getYPos() - 5);
			}

		}

		knightS.setPosition(P.getPlayerRectX(), P.getPlayerRectY());
		P.setDegrees(mousePos.x, mousePos.y);
		knightS.setRotation(P.getDegrees());
		knightS.draw(batch);
		batch.draw(sword, P.getSwordRectX(), P.getSwordRectY());
		// batch.draw(knight, player.x, player.y);
		batch.end();
		
		
		
		
		
	}
	
	private void roomEvents() {
		if(grace != true && fightingRoom) {
		enemyPhysics();
		batch.draw(door, 850, 880);
		
		}else if(gracePeriod > 0 && fightingRoom) {
			gracePeriod --;
			if(gracePeriod == 0) {
				grace = false;
				isFighting = true;
				respawnMonsters();
			}
		}
		
		if(fightingRoom && isFighting == false || startRoom) {
			batch.draw(openDoor, 850, 880);
			if(P.getPlayerRectY() > 900 && P.getPlayerRectX() < 1000 && P.getPlayerRectX() > 600 && Gdx.input.justTouched()) {
				P.setPlayerRectX(900);
				P.setPlayerRectY(100);
				System.out.println("Changing room");
				fightingRoom = true;
				startRoom = false;
				grace = true;
				gracePeriod = 60;
			}
		}
		
		if(startRoom) {
			batch.draw(instructions, 100, 500);
		}
	}

	public void enemyPhysics() {
			for (Enemy enemy : enemies) {
			if (enemy.getIsAlive()) {
				enemy.searchForPlayer(knightS);
				enemy.move();
			}
		}
		
		tempP = P.getPlayerRect();
		tempS = P.getSwordRect();
		

		for (Enemy enemy : enemies) {
			if (tempS.overlaps(enemy.getRectangle()) && Gdx.input.justTouched() && enemy.getInvincible() != true) {
				//System.out.println("test");
				enemy.damage(P.getAttack(), knightS);
				//System.out.println("hit enemy");
			}
			
			if(tempP.overlaps(enemy.getRectangle()) && P.getIsInvincible() != true) {
				if (tempP.getX() > enemy.getXPos()) {
					P.setXMs(500);
					P.setIsInvincible(true);
					P.setHealth(P.getHealth() - enemy.getAttack()/2);
				} else if (tempP.getX() < enemy.getXPos()) {
					P.setIsInvincible(true);
					P.setHealth(P.getHealth() - enemy.getAttack()/2);
					P.setXMs(-500);
				}

				if (tempP.getY() > enemy.getYPos()) {
					P.setIsInvincible(true);
					P.setHealth(P.getHealth() - enemy.getAttack()/2);
					P.setYMs(500);
				} else if (tempP.getY() < enemy.getYPos()) {
					P.setIsInvincible(true);
					P.setHealth(P.getHealth() - enemy.getAttack()/2);
					P.setYMs(-500);
				}
				System.out.println(P.getHealth());
			}
		}

		currentMonsters = 0;
		for(Enemy enemy : enemies) {
			if(enemy.getIsAlive()) {
				currentMonsters ++;
				enemy.clockInvincibility();
			}
		}
		
		if (currentMonsters == 0) {
			isFighting = false;
		}
		
		
		
		
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		knight.dispose();
	}
}