package com.congruence.start;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.GameConfiguration;
import com.congruence.util.GameStartListener;

import java.util.ArrayList;

public class StartScreen implements Screen {

    private OrthographicCamera camera;

    private Texture coverImage;

    private Texture coverImageWide;

    private Texture coverImageFull;

    private Stage stage;

    private ArrayList<GameStartListener> gameStartListeners;

    public StartScreen() {
        gameStartListeners = new ArrayList<>();
    }

    @Override
    public void show() {
        GameConfiguration.width = Gdx.graphics.getWidth();
        GameConfiguration.height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ScreenViewport viewport = new ScreenViewport(camera);

        coverImage = new Texture(Gdx.files.internal("Cover Image.png"), true);
        coverImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        coverImageWide = new Texture(Gdx.files.internal("Cover Image Wide.png"), true);
        coverImageWide.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        coverImageFull = new Texture(Gdx.files.internal("Cover Image.png"), true);
        coverImageFull.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        stage = new Stage();
        stage.setViewport(viewport);

        System.out.println(GameConfiguration.width + " " + GameConfiguration.height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.getBatch().begin();

        if (GameConfiguration.width <= 1300) {
            stage.getBatch().draw(coverImage, 0, 0, GameConfiguration.width, GameConfiguration.height);
        }
        else if (GameConfiguration.width <= 2000) {
            stage.getBatch().draw(coverImageWide, 0, 0, GameConfiguration.width, GameConfiguration.height);
        }

        stage.getBatch().end();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F) || Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (!Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
            else {
                Gdx.graphics.setWindowedMode(1280, 720);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.graphics.setWindowedMode(1280, 720);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isTouched()) {
            for (GameStartListener e: gameStartListeners) {
                e.gameStarted();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        GameConfiguration.width = width;
        GameConfiguration.height = height;
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
        System.out.println(GameConfiguration.width + " " + GameConfiguration.height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        coverImage.dispose();
        coverImageWide.dispose();
        coverImageFull.dispose();
    }

    public ArrayList<GameStartListener> getGameStartListeners() {
        return gameStartListeners;
    }

    public void addGameStartListeners(GameStartListener gameStartListener) {
        gameStartListeners.add(gameStartListener);
    }

}
