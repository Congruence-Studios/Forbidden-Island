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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.GameConfiguration;
import com.congruence.util.GameInitializeListener;
import com.congruence.util.GameStartListener;

import java.util.ArrayList;

public class StartMenu implements Screen {

    private OrthographicCamera camera;

    private Texture backgroundImage;

    private Stage stage;

    private BitmapFont titleFont;

    private BitmapFont startFont;

    private ArrayList<GameInitializeListener> gameInitializeListeners;

    private Skin neonUISkin;

    private Button startGameButton;

    private SelectBox<String> difficultlySelect;

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

        backgroundImage = new Texture("NewWorld_Island.jpg");

        stage = new Stage();
        stage.setViewport(viewport);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Plain_Germanica.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        titleFont = generator.generateFont(parameter);
        generator.dispose();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Abel-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        startFont = generator.generateFont(parameter);
        generator.dispose();

        neonUISkin = new Skin(Gdx.files.internal("./ui/neon/neon-ui.json"));
        startGameButton = new TextButton("Start Game", neonUISkin);
        stage.addActor(startGameButton);

        difficultlySelect = new SelectBox<String>(neonUISkin);
        difficultlySelect.setItems("Novice", "Expert", "FGI");
        difficultlySelect.setSelected("Novice");
        difficultlySelect.setSize(120, 50);
        stage.addActor(difficultlySelect);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.getBatch().begin();

        final String titleText = "Forbidden Island";
        final GlyphLayout titleLayout = new GlyphLayout(titleFont, titleText);

        float fontX = 0 + (GameConfiguration.width - titleLayout.width) / 2;
        float fontY = 0 + (GameConfiguration.height + titleLayout.height) * 5 / 6f;

        titleFont.draw(stage.getBatch(), titleText, fontX, fontY);

        final String startText = "Press Enter to Continue";
        final GlyphLayout startLayout = new GlyphLayout(startFont, startText);

        fontX = 0 + (GameConfiguration.width - startLayout.width) / 2;
        fontY = 0 + (GameConfiguration.height + startLayout.height) + titleLayout.height;

        startFont.draw(stage.getBatch(), startText, fontX, fontY);

        difficultlySelect.setPosition(GameConfiguration.width /2f - difficultlySelect.getWidth()/2, GameConfiguration.height/2f);
        startGameButton.setPosition(GameConfiguration.width /2f - startGameButton.getWidth()/2 , GameConfiguration.height/2f - difficultlySelect.getHeight());

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            for (GameInitializeListener e: gameInitializeListeners) {
                e.onInitialize();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        GameConfiguration.width = width;
        GameConfiguration.height = height;
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
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
        backgroundImage.dispose();
        titleFont.dispose();
    }

    public ArrayList<GameInitializeListener> getGameInitializeListeners() {
        return gameInitializeListeners;
    }

    public void addGameStartListeners(GameInitializeListener gameStartListener) {
        gameInitializeListeners.add(gameStartListener);
    }

}
