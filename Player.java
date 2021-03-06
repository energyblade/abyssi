/*
 * Rhys, Greg
 * Abyssi
 * April 1st, 2021
 */
package com.mygdx.abyssi;
//IMPORT PACKAGES
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Player {
	private Rectangle player;
	public Rectangle swordRect;
	private int PWidth = 125, PHeight = 74, SWidth = 125, SHeight = 74;
	private int maxMs = 400;
	private int xMs = 0, yMs = 0;
	private int attack = 50;
	private int acceleration = 50;
	private float health = 200;
	private float maxHealth = 200;
	private int iFrames = 90, invincibility;
	private boolean xDirectionPlus = false, xDirectionMinus = false, yDirectionPlus = false, yDirectionMinus = false, isInvincible = false;
	private float degrees;
	private boolean alive = true;

	public Player() {
		player = new Rectangle();
		setPlayerRectPos(1920 / 2 - 64 / 2, 1080 / 2, PWidth, PHeight);
		swordRect = new Rectangle();
		setSwordRectPos(player.getX() + 160, player.getY() + 100, SWidth, SHeight);
		setDegree(1920 / 2 - 64 / 2, 1080 / 2);
		setXMs(0);
		setYMs(0);
		setAlive(alive);
		
	}

	private void setAlive(boolean a) {
		alive = a;
		
	}

	private void setDegree(float x, float y) {
		degrees = (float) ((Math.atan2(x - player.x, -(y - player.y)) * 180.0d / Math.PI) - 180.0f);		
	}

	private void setSwordRectPos(float x, float y, int w, int h) {
		swordRect.x = x;
		swordRect.x = y;
		swordRect.width = w;
		swordRect.height = h;
		
	}

	private void setPlayerRectPos(float f, float g, int w, int h) {
		player.x = f;
		player.y = g;
		player.width = w;
		player.height = h;
		
	}
	
	public void setPlayerRectX(float x) {
		player.x = x;

	}
	
	public void setPlayerRectY(float y) {
		player.y = y;

	}
	
	public void setXMs(int x) {
		xMs = x;
	}
	
	public void setYMs(int y) {
		yMs = y;
	}
	
	public void setDirection() {
		
	}
	
	private void setXDirectionMinus(boolean d) {
		xDirectionMinus = d;
	}
	
	private void setXDirectionPlus(boolean d) {
		xDirectionPlus = d;
	}
	
	private void setYDirectionMinus(boolean d) {
		yDirectionMinus = d;
	}
	
	private void setYDirectionPlus(boolean d) {
		yDirectionPlus = d;
	}
	
	public void setDegrees(float x, float y) {
		degrees = (float) ((Math.atan2(x - player.x, -(y - player.y)) * 180.0d / Math.PI) - 180.0f);
		
	}
	
	public void setIsInvincible(boolean i) {
		invincibility = iFrames;
		isInvincible = true;
	}
	
	public void setHealth(float h) {
		health = h;
		if (getHealth() <= 0) {
			setAlive(false);
		}
	}
	public boolean getIsAlive() {
		return alive;
	}
	
	public boolean getIsInvincible() {
		return isInvincible;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public float getDegrees() {
		return degrees;
	}
	
	public float getPlayerRectX() {
		return player.x;
	}
	
	public float getPlayerRectY() {
		return player.y;
	}
	
	public float getSwordRectX() {
		return swordRect.x;
	}
	
	public float getSwordRectY() {
		return swordRect.y;
	}
	
	public int getXMs() {
		return xMs;
	}
	
	public int getYMs() {
		return yMs;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public Rectangle getSwordRect() {
		return swordRect;
	}
	
	public Rectangle getPlayerRect() {
		return player;
	}
	
	private boolean getXDirectionMinus() {
		return xDirectionMinus;
	}
	
	private boolean getXDirectionPlus() {
		return xDirectionPlus;
	}
	
	private boolean getYDirectionMinus() {
		return yDirectionMinus;
	}
	
	private boolean getYDirectionPlus() {
		return yDirectionPlus;
	}
	
	public float getHealth() {
		
		return health;
	}
	
	public void move(float x, float y) {
		if (Gdx.input.isKeyPressed(Keys.A)) {// LEFT
//			knightS.setRotation(90f);
			if (getXMs() > 0) {
				setXMs(getXMs() - 50);
			} else if (getXMs() > -maxMs) {
				setXMs(getXMs() - acceleration);
			} else if (getXMs() < -maxMs) {
				setXMs(getXMs() + 1);
			}

			setXDirectionPlus(false);
			setXDirectionMinus(true);

		} else {
			setXDirectionMinus(false);
		}

		if (Gdx.input.isKeyPressed(Keys.D)) {// RIGHT
			if (getXMs() < 0) {
				setXMs(getXMs() + 50);
			} else if (getXMs() < maxMs) {
				setXMs(getXMs()  + acceleration);
			} else if (getXMs() > maxMs) {
				setXMs(getXMs() - 1);
			}

			setXDirectionPlus(true);
			setXDirectionMinus(false);

		} else {
			setXDirectionPlus(false);
		}

		if (Gdx.input.isKeyPressed(Keys.W)) {// UP
			
			
			if (getYMs() < 0) {
				setYMs(getYMs() + 50);
			} else if (getYMs() < maxMs) {
				setYMs(getYMs() + acceleration);
			} else if (getYMs() > maxMs) {
				setYMs(getYMs() - 1);
			}

			setYDirectionPlus(true);
			setYDirectionMinus(false);
		} else {
			setYDirectionPlus(false);
		}

		if (Gdx.input.isKeyPressed(Keys.S)) {// DOWN
			if (getYMs() > 0) {
				setYMs(getYMs() - 50);
			} else if (getYMs() > -maxMs) {
				setYMs(getYMs() - acceleration);
			} else if (getYMs() < -maxMs) {
				setYMs(getYMs() + 1);
			}
			
			setYDirectionPlus(false);
			setYDirectionMinus(true);
		} else {
			setYDirectionMinus(false);
		}
		
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {//EXIT GAME
	        Gdx.app.exit();
		}

		if (getXDirectionPlus() == false && getXDirectionMinus() == false) {
			if (getXMs() > 0) {
				setXMs(getXMs() - acceleration);
			}

			if (getXMs() < 0) {
				setXMs(getXMs() + acceleration);
			}
		}
		
		if (getYDirectionPlus() == false && getYDirectionMinus() == false) {
			if (getYMs() > 0) {
				setYMs(getYMs() - acceleration);
			}

			if (getYMs() < 0) {
				setYMs(getYMs() + acceleration);
			}
		}
		
		//SET PLAYER POSITION
		setPlayerRectPos(getPlayerRectX() + getXMs() * Gdx.graphics.getDeltaTime(), getPlayerRectY() + getYMs()* Gdx.graphics.getDeltaTime() , PWidth, PHeight);

		//SET PLAYER BOUNDS
		if (getPlayerRectX() < 0)
			setPlayerRectX(0);
		if (getPlayerRectX() > 1920 - PWidth)
			setPlayerRectX(1920 - PWidth);
		if (getPlayerRectY() < 0)
			setPlayerRectY(0);
		if (getPlayerRectY() > 1080 - PWidth)
			setPlayerRectY(1080 - PWidth);
		
		//SET BOX FOR SWORD AROUND PLAYER
		if (x > player.x + player.width) {
			swordRect.setPosition(player.getX() + player.width, swordRect.getY());
		} else if (x < player.x - 86) {
			swordRect.setPosition(player.getX() - 86, swordRect.getY());
		} else if (x > player.x - 86 && x < player.x + player.width) {
			swordRect.setPosition(x, swordRect.getY());
		}

		if (y > player.y + player.height) {
			swordRect.setPosition(swordRect.getX(), player.y + player.height + 10);
		} else if (y < player.y - 60) {
			swordRect.setPosition(swordRect.getX(), player.y - 60);
		} else if (y > player.y - 60 && y < player.y + player.height) {
			swordRect.setPosition(swordRect.getX(), y);
		}
		
		
		if(invincibility > 0) {
			invincibility --;
		}else {
			isInvincible = false;
		}
	}
}

	
