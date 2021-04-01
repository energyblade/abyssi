/*
 * Rhys, Greg
 * Abyssi
 * April 1st, 2021
 */
package com.mygdx.abyssi;
//IMPORT PACKAGES
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.abyssi.Abyssi;

public class MainMenuScreen implements Screen {

	final Abyssi game;

	OrthographicCamera camera;
	private Texture logo;
	
	public MainMenuScreen(final Abyssi game) {

		this.game = game;
		logo = new Texture("logo.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}


	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		//DRAW THE MAIN MENU ONTO THE SCREEN
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(logo, 175, 200);
		game.font.getData().setScale(1,1);
		game.font.draw(game.batch, "WELCOME ", 375, 150);
		game.font.draw(game.batch, "CLICK ANYWHERE TO ENTER THE ABYSS", 275, 100);
		game.batch.end();

		//START THE GAME
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
