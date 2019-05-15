package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class MainGame extends Game {

	public SpriteBatch batch;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	private AssetManager manager;
	public BaseScreen loadingScreen, menuScreen, gameScreen, gameOverScreen;


	@Override
	public void create() {
		batch = new SpriteBatch();

		manager=new AssetManager();
		manager.load("tai.png",Texture.class);
		manager.load("floor.png",Texture.class);
		manager.load("overfloor.png",Texture.class);
		manager.load("drako.PNG",Texture.class);
		manager.load("logo.png", Texture.class);
		manager.load("audio/die.ogg", Sound.class);
		manager.load("audio/jump.ogg", Sound.class);
		manager.load("audio/song.ogg", Music.class);
		manager.load("gameover.png", Texture.class);
		manager.load("hueso.png", Texture.class);

		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	public void finishLoading() {
		menuScreen = new  MenuScreen(this);
		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
 		setScreen(menuScreen);
	}

	public AssetManager getManager() {
		return manager;

	}
}
