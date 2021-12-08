package com.congruence.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;
import com.congruence.ui.AmountOfPlayers;
import com.congruence.ui.GameDifficulty;
import com.congruence.ui.HowToPlayButton;
import com.congruence.ui.StartButton;
import com.congruence.util.GameInitializeListener;
import com.congruence.util.GameStartListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class StartMenu implements Screen {

    private static final Logger logger = LoggerFactory.getLogger(StartMenu.class);

    private OrthographicCamera camera;

    private Texture coverImage;

    private Texture coverImageWide;

    private Texture coverImageFull;

    private Texture gameOptions;

    private Stage stage;

    private ArrayList<GameInitializeListener> gameInitializeListeners;

    private StartButton startGameButton;

    private GameDifficulty gameDifficulty;

    private AmountOfPlayers amountOfPlayers;

    private HowToPlayButton howToPlayButton;

    public StartMenu() {
        gameInitializeListeners = new ArrayList<>();
    }

    @Override
    public void show() {
        GameConfiguration.width = Gdx.graphics.getWidth();
        GameConfiguration.height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ScreenViewport viewport = new ScreenViewport(camera);

        coverImage = new Texture(Gdx.files.internal("Cover Image Plain.png"), true);
        coverImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        coverImageWide = new Texture(Gdx.files.internal("Cover Image Wide Plain.png"), true);
        coverImageWide.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        coverImageFull = new Texture(Gdx.files.internal("Cover Image.png"), true);
        coverImageFull.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        gameOptions = new Texture(Gdx.files.internal("./start/Game Options.png"), true);
        gameOptions.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);



        stage = new Stage();
        stage.setViewport(viewport);
        Gdx.input.setInputProcessor(stage);

        gameDifficulty = new GameDifficulty(10f, 100f, (GameConfiguration.width/1280f)*100f, (GameConfiguration.width/1280f)*320f, 0);
        stage.addActor(gameDifficulty);

        amountOfPlayers = new AmountOfPlayers(10f, 500f, (GameConfiguration.width/1280f)*100f, (GameConfiguration.width/1280f)*320f, 4);
        stage.addActor(amountOfPlayers);

        startGameButton = new StartButton(10f, 10f, (GameConfiguration.width/1280f)*100f, (GameConfiguration.width/1280f)*160f);
        stage.addActor(startGameButton);
        howToPlayButton = new HowToPlayButton(GameConfiguration.width - 10f - ((GameConfiguration.width/1280f)*160f), 10f, (GameConfiguration.width/1280f)*100f, (GameConfiguration.width/1280f)*160f);
        stage.addActor(howToPlayButton);
        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    openWebpage(new URL("https://drive.google.com/file/d/1WvWkzR-j-KeFz34SLsh--BQmBY-fvOG6/view?usp=sharing"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (GameInitializeListener e : StartMenu.this.gameInitializeListeners) {
                    int NumberOfPlayers = amountOfPlayers.getAmountOfPlayers();
                    int Difficulty = gameDifficulty.getDifficulty();
                    e.onInitialize(NumberOfPlayers, Difficulty);
                }
            }
        });
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

        stage.getBatch().draw(gameOptions, 10f, GameConfiguration.height - 10f - (GameConfiguration.width/1280f)*100, (GameConfiguration.width/1280f)*320, (GameConfiguration.width/1280f)*100);

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

    }

    @Override
    public void resize(int width, int height) {
        GameConfiguration.width = width;
        GameConfiguration.height = height;
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);

        gameDifficulty.setPositionX(10f);
        gameDifficulty.setPositiveY(10 + 2 * (GameConfiguration.width/1280f)*100f + 20f);
        gameDifficulty.setHeight((GameConfiguration.width/1280f)*100f);
        gameDifficulty.setWidth((GameConfiguration.width/1280f)*320f);

        amountOfPlayers.setPositionX(10f);
        amountOfPlayers.setPositiveY(10 + 1 * (GameConfiguration.width/1280f)*100f + 10f);
        amountOfPlayers.setHeight((GameConfiguration.width/1280f)*100f);
        amountOfPlayers.setWidth((GameConfiguration.width/1280f)*320f);

        startGameButton.setPositionX(10f);
        startGameButton.setPositiveY(10f);
        startGameButton.setHeight((GameConfiguration.width/1280f)*100f);
        startGameButton.setWidth((GameConfiguration.width/1280f)*160f);

        howToPlayButton.setPositionX(GameConfiguration.width - 10f - ((GameConfiguration.width/1280f)*160f));
        howToPlayButton.setPositiveY(10f);
        howToPlayButton.setHeight((GameConfiguration.width/1280f)*100f);
        howToPlayButton.setWidth((GameConfiguration.width/1280f)*160f);
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
    }

    public ArrayList<GameInitializeListener> getGameInitializeListeners() {
        return gameInitializeListeners;
    }

    public void addGameStartListeners(GameInitializeListener gameStartListener) {
        gameInitializeListeners.add(gameStartListener);
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
