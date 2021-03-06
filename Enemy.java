/*
 * Rhys, Greg
 * Abyssi
 * April 1st, 2021
 */
package com.mygdx.abyssi;
	//IMPORT PACKAGES
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
	//DECLARE VARIABLES 
	private int Ms = 100;
	private int health;
	private int attack;
	private int type;
	private int iFrames = 45, invincibleTimeLeft;
	private int xSpeed, ySpeed;
	private float xPos, yPos;
	private boolean isAlive;
	private Rectangle hitbox;

	//ENEMY OBJECT
	public Enemy(int attackType, int hp, int ad, float x, float y, boolean alive, int invincibility) {
		setHealth(hp);
		setAttack(ad);
		setAttackType(attackType);
		setPos(x, y);
		setIsAlive(alive);
	}
	//HITBOX
	private void setHitbox(float x, float y) {
		hitbox = new Rectangle();
		hitbox.x = x;
		hitbox.y = y;
		hitbox.width = 60;
		hitbox.height = 80;

	}

	private void setAttackType(int attackType) {
		type = attackType;

	}

	private void setAttack(int ad) {
		attack = ad;

	}

	private void setInvincibility(int i) {
		invincibleTimeLeft = i;

		if (invincibleTimeLeft < 1) {
			invincibleTimeLeft = 0;
		}

	}

	public void setHealth(int hp) {
		health = hp;
		if (getHealth() <= 0) {
			setIsAlive(false);
			setPos(-100, -100);
		} else {
			setIsAlive(true);
		}

	}
	//ATTACKING THE PLAYER
	public void damage(int ad, Sprite player) {
		setHealth(getHealth() - ad);
		setInvincibility(iFrames);

		int x = 0, y = 0;
		if (player.getX() < getXPos()) {
			x = 150;
		} else if (player.getX() > getXPos()) {
			x = -150;
		}

		if (player.getY() < getYPos()) {
			y = 150;
		} else if (player.getY() > getYPos()) {
			y = -150;
		}

		setSpeed(x, y);
	}

	public void setPos(float x, float y) {
		xPos = x;
		yPos = y;
		setHitbox(getXPos(), getYPos());
	}

	private void setIsAlive(boolean a) {
		isAlive = a;
	}
	//SEARCHING AND MOVING TOWARD PLAYER
	public void searchForPlayer(Sprite player) {
		int x;
		int y;
		if (player.getX() > getXPos() && getXSpeed() < 50) {
			x = getXSpeed() + 4;
		} else if (player.getX() < getXPos() && getXSpeed() > -50) {
			x = getXSpeed() - 4;
		} else {
			if (getXSpeed() > 0) {
				x = getXSpeed() - 4;
			} else if (getXSpeed() < 0) {
				x = getXSpeed() + 4;
			} else {
				x = 0;
			}

		}

		if (player.getY() > getYPos() && getYSpeed() < 50) {
			y = getYSpeed() + 4;
		} else if (player.getY() < getYPos() && getYSpeed() > -50) {
			y = getYSpeed() - 4;
		} else {
			if (getYSpeed() > 0) {
				y = getYSpeed() - 4;
			} else if (getYSpeed() < 0) {
				y = getYSpeed() + 4;
			} else {
				y = 0;
			}

		}

		setSpeed(x, y);

	}

	public void setSpeed(int x, int y) {
		xSpeed = x;
		ySpeed = y;

	}

	public void clockInvincibility() {
		setInvincibility(invincibleTimeLeft - 1);
	}

	public void move() {
		xPos += xSpeed * Gdx.graphics.getDeltaTime();
		yPos += ySpeed * Gdx.graphics.getDeltaTime();
		setHitbox(xPos, yPos);
	}

	public int getHealth() {
		return health;
	}

	public int getAttack() {
		return attack;
	}

	public float getXPos() {
		return xPos;
	}

	public float getYPos() {
		return yPos;
	}

	public int getXSpeed() {
		return xSpeed;
	}

	public int getYSpeed() {
		return ySpeed;
	}

	public Rectangle getRectangle() {
		return hitbox;
	}

	public boolean getInvincible() {
		if (invincibleTimeLeft > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getIsAlive() {
		return isAlive;
	}

}
