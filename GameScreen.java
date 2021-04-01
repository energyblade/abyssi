/*
 * Rhys, Greg
 * Abyssi
 * April 1st, 2021
 */

package com.mygdx.abyssi;
//IMPORT PACKAGES
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen{
	final Abyssi game;
	
	//Images/imaging
	private Texture knight, goblin, sword, door, openDoor, instructions, gameover, BG;
	private OrthographicCamera camera;
	
	//Sprites and entities
	public SpriteBatch batch; 
	public Sprite knightS;
	private Array<Enemy> enemies;
	private int maxMonsters = 5;
	private int currentMonsters = 0;
	public Player P = new Player();
	public Rectangle tempP, tempS;
	
	//Event variables
	public int gracePeriod = 0;
	public boolean grace = false;
	public boolean monstersSpawned = false;
	public boolean fightingRoom = false, startRoom = true, isFighting = false, doorOpened = true;
	
	//Health bar variables
	public Pixmap pixmap;
	public TextureRegionDrawable drawable;
	private HealthBar healthBar;
	private Stage stage;
	BitmapFont font;
	CharSequence str;
	
	//Sound
	private Sound hitSound, doorOpen, doorClose, hurtSound;
	private Music music;
	
	public GameScreen(final Abyssi game) {
		this.game = game;
			
		//INITIALIZE TEXTURES AND VARIABLES
		
		//Crosshair texture
		Pixmap pm = new Pixmap(Gdx.files.internal("crosshair.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		pm.dispose();
		
		//Images
		knight = new Texture("knight.png");
		goblin = new Texture("goblin.png");
		sword = new Texture("sword.png");
		font = new BitmapFont();
		door = new Texture("door.png");
		openDoor = new Texture("opendoor.png");
		instructions = new Texture("instructions.png");
		gameover = new Texture("deathScreen.png");
		BG = new Texture("BG.png");
		knightS = new Sprite(knight);

		//Setting up camera and batch for rendering
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();

		stage = new Stage();
		
		//Initializing health bar object (Health bar visuals taken from sboychen from libgdx stacks overflow)
		healthBar = new HealthBar(960, 50);
		healthBar.setPosition(480, 20);
		stage.addActor(healthBar);
		
		//Initializze sound files and set them to their respected names;
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hitSound.mp3"));
		doorOpen = Gdx.audio.newSound(Gdx.files.internal("doorOpen.mp3"));
		doorClose = Gdx.audio.newSound(Gdx.files.internal("doorClose.mp3"));
		hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurtSound.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		
		//Start the music and have it loop so it will always be playing 
		music.setLooping(true);
		music.play();
		
		//INITIALIZE ENEMIES
		enemies = new Array<Enemy>();
		for (int i = 0; i < maxMonsters; i++) {
			spawnMonster();
		}

	}
	
	//SPAWN MONSTERS
	private void spawnMonster() {
		Enemy enemy = new Enemy(1, 100, 40, MathUtils.random(64, 1800), MathUtils.random(0, 500), true, 0);
		enemy.setHealth(0);
		enemies.add(enemy);
	}
	//RESPAWN MONSTERS
	private void respawnMonsters() {
		for (Enemy enemy : enemies) {
			System.out.println("reviving...");
			enemy.setHealth(100);
			enemy.setSpeed(0, 0);
			enemy.setPos(MathUtils.random(64, 1800), MathUtils.random(400, 900));
		}
	}
	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		//Begin batch for taking in images to send to openGl renderer
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);//clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Background texture
		batch.draw(BG, 960, 540);
		batch.draw(BG, 960, 0);
		batch.draw(BG, 0, 540);
		batch.draw(BG, 0, 0);
		
//		Health bar visual code taken from sboychen from stacks overflow for making healthbar/progress bar
		healthBar.setValue(P.getHealth() / P.getMaxHealth());
		str = P.getHealth() + "/" + P.getMaxHealth();
		
		
		font.getData().setScale(3);
		font.draw(batch, str, 840, 60);
		
		stage.draw();
		stage.act();

		P.move(mousePos.x, mousePos.y);
		roomEvents();
		death();
		
		//Drawing enemies if they are alive and showing their health underneath their body
		for (Enemy enemy : enemies) {
			if (enemy.getIsAlive()) {
				batch.draw(goblin, enemy.getXPos(), enemy.getYPos());
				str = enemy.getHealth() + "";
				font.getData().setScale(1);
				font.draw(batch, str, enemy.getXPos() + 20, enemy.getYPos() - 5);
			}
		}
		
		//SET POSITION OF PLAYER AND SWORD
		knightS.setPosition(P.getPlayerRectX(), P.getPlayerRectY());
		//Getting a rotational value based on the position of the knight sprite to the mouse
		P.setDegrees(mousePos.x, mousePos.y);
		knightS.setRotation(P.getDegrees());
		knightS.draw(batch);
		batch.draw(sword, P.getSwordRectX(), P.getSwordRectY());
		batch.end();//Send the batch of images to the renderer to display
		
	}
	
	//DEATH SCREEN
	private void death() {
		//Checking to see if the player is still alive
		if (!P.getIsAlive()) {
			batch.draw(gameover,0, 0);
			if (Gdx.input.justTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
			}
			P.setPlayerRectX(-100);
			P.setPlayerRectY(-100);
			P.swordRect.setPosition(-100,-100);
			for (Enemy enemy : enemies) {
			enemy.setPos(-100,-100);	
			}
		}
	}
	
	//Room events such as when to respawn enemies and move them. This is also for interacting with doors and how they close and open
	private void roomEvents() {
		if(grace != true && fightingRoom) {
		enemyPhysics();
		batch.draw(door, 850, 880);
		
		//While the grace period is still happening, run the grace period clock and close the door/spawn enemies when grace finishes
		}else if(gracePeriod > 0 && fightingRoom) {
			gracePeriod --;
			if(gracePeriod == 0) {
				grace = false;
				doorClose.play();
				doorOpened = false;
				isFighting = true;
				respawnMonsters();
			}
		}
		//NO ENEMIES
		if(fightingRoom && isFighting == false || startRoom) {
			if(doorOpened != true) {
				doorOpen.play();
				doorOpened = true;
			}
			
			//When player is within the bondaries of the door and they interact, they will change rooms
			batch.draw(openDoor, 850, 880);
			if(P.getPlayerRectY() > 880 && P.getPlayerRectX() < 1160 && P.getPlayerRectX() > 760 && Gdx.input.justTouched()) {
				P.setPlayerRectX(900);
				P.setPlayerRectY(100);
				System.out.println("Changing room");
				doorOpened = true;
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

	//ENEMY MOVEMENT/attacking/being attacked properties
	public void enemyPhysics() {
		
		//Enemies search for player if they are alive and set their movement to a direction towards them
			for (Enemy enemy : enemies) {
			if (enemy.getIsAlive()) {
				enemy.searchForPlayer(knightS);
				enemy.move();
			}
		}
		
		//getting temporary rectangle objects
		tempP = P.getPlayerRect();
		tempS = P.getSwordRect();
		
		//ATTACKING THE PLAYER
		for (Enemy enemy : enemies) {
			//If the sword hitbox is touching the enemy and the player clicks, it will attack them
			if (tempS.overlaps(enemy.getRectangle()) && Gdx.input.justTouched() && enemy.getInvincible() != true) {
				enemy.damage(P.getAttack(), knightS);
				hitSound.play();
			}
			
			//If enemy is touching player hurtbox, damage the player/give them temporary invincibility and knock the away from the enemy
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
				hurtSound.play();
				//System.out.println(P.getHealth());
			}
		}
		
		//Checking through each enemy to see if they are alive and clocking their invincibility if they are currently in iFrame time
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
		font.dispose();
	}
}
