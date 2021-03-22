package com.mygdx.abyssi;

import org.lwjgl.opengl.XRandR.Screen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Abyssi extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture knight, goblin, sword;
	private OrthographicCamera camera;
	private Rectangle player, swordRect, goblinHitbox;
	Sprite knightS;
	
	private int maxMs = 400;
	private int xMs = 0, yMs = 0;
	private boolean xDirectionPlus = false, xDirectionMinus = false, yDirectionPlus = false, yDirectionMinus = false;

	private Array<Enemy> enemies;
	private int maxMonsters = 5;
//	private int currentMonsters = 0;

	@Override
	public void create() {
		
		knight = new Texture("knight.png");
		goblin = new Texture("goblin.png");
		sword = new Texture("sword.png");
		knightS = new Sprite(knight);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();

		player = new Rectangle();
		player.x = 1920 / 2 - 64 / 2;
		player.y = 1080 / 2;
		player.width = 200;
		player.height = 200;

		swordRect = new Rectangle();
		swordRect.x = player.getX() + 160;
		swordRect.x = player.getY() + 100;
		swordRect.width = 200;
		swordRect.height = 200;
		
		goblinHitbox = new Rectangle();
		goblinHitbox.width = 100;
		goblinHitbox.height = 100;

		enemies = new Array<Enemy>();

		for (int i = 0; i < maxMonsters; i++) {
			spawnMonster();
		}
	}

	private void spawnMonster() {
		Enemy enemy = new Enemy(1, 100, 20, MathUtils.random(64, 200), MathUtils.random(0, 800 - 60));
		enemies.add(enemy);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
	//	batch.draw(knight, player.x, player.y);
		//batch.draw(knightS, player.x, player.y);
		knightS.draw(batch);
		batch.draw(sword, swordRect.x, swordRect.y);
		
		for(Enemy enemy: enemies) {
			batch.draw(goblin, enemy.getXPos(), enemy.getYPos() );
			goblinHitbox.setPosition(enemy.getXPos(), enemy.getYPos());
		}
		batch.end();

		if (Gdx.input.isKeyPressed(Keys.A)) {// LEFT
			knightS.setRotation(90f);
			if (xMs > 0) {
				xMs -= 50;
			} else if (xMs > -maxMs) {
				xMs -= 5 - (xMs * 0.5);
			} else if (xMs < -maxMs) {
				xMs = -maxMs;
			}

			xDirectionMinus = true;
			xDirectionPlus = false;

		} else {
			xDirectionMinus = false;
		}

		if (Gdx.input.isKeyPressed(Keys.D)) {
			knightS.setRotation(-90f);
			if (xMs < 0) {
				xMs += 50;
			} else if (xMs < maxMs) {
				xMs += 5 + (xMs * 0.5);
			} else if (xMs > maxMs) {
				xMs = maxMs;
			}

			xDirectionPlus = true;
			xDirectionMinus = false;

		} else {
			xDirectionPlus = false;
		}

		if (Gdx.input.isKeyPressed(Keys.W)) {
			knightS.setRotation(0f);
			if (yMs < 0) {
				yMs += 50;
			} else if (yMs < maxMs) {
				yMs += 5 + (yMs * 0.5);
			} else if (yMs > maxMs) {
				yMs = maxMs;
			}
			yDirectionPlus = true;
			yDirectionMinus = false;
		} else {
			yDirectionPlus = false;
		}

		if (Gdx.input.isKeyPressed(Keys.S)) {
			knightS.setRotation(180f);
			if (yMs > 0) {
				yMs -= 50;
			} else if (yMs > -maxMs) {
				yMs -= 5 - (yMs * 0.5);
			} else if (yMs < -maxMs) {
				yMs = -maxMs;
			}
			yDirectionMinus = true;
			yDirectionPlus = false;
		} else {
			yDirectionMinus = false;
		}

		if (xDirectionPlus == false && xDirectionMinus == false) {
			if (xMs > 0) {
				xMs -= 5 + (xMs * 0.03);
			}

			if (xMs < 0) {
				xMs += 5 + (-xMs * 0.03);
			}

			if (xMs < 0.001 || xMs > -0.001) {
				xMs -= xMs * 0.1;
			}
		}

		if (yDirectionPlus == false && yDirectionMinus == false) {
			if (yMs > 0) {
				yMs -= 5 + (yMs * 0.03);
			}

			if (yMs < 0) {
				yMs += 5 + (-yMs * 0.03);
			}

			if (yMs < 0.001 || yMs > -0.001) {
				yMs -= yMs * 0.1;
			}
		}

		player.x += xMs * Gdx.graphics.getDeltaTime();
		player.y += yMs * Gdx.graphics.getDeltaTime();

		if (player.x < 0)
			player.x = 0;
		if (player.x > 1920 - player.getHeight())
			player.x = 1920 - player.getHeight();
		if (player.y < 0)
			player.y = 0;
		if (player.y > 1080 - player.getHeight())
			player.y = 1080 - player.getHeight();
		
		knightS.setPosition(player.x, player.y);
		swordRect.setPosition(player.getX() + 160, (player.getY() + 100));
		if (swordRect.overlaps(goblinHitbox) && Gdx.input.isTouched()) {
			System.out.println("test");
			
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		knight.dispose();
	}


}