/*
 * Rhys, Greg
 * Abyssi
 * April 1st, 2021
 */
package com.mygdx.abyssi;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Abyssi extends Game {


		public SpriteBatch batch;
		public BitmapFont font;

		public void create() {
			batch = new SpriteBatch();
			// Use LibGDX's default Arial font.
			font = new BitmapFont();
			this.setScreen(new MainMenuScreen(this));
		}

		public void render() {
			super.render(); // important!
		}

		public void dispose() {
			batch.dispose();
			font.dispose();
		}
}