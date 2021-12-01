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
import com.congruence.util.GameInitializeListener;
import com.congruence.util.GameStartListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class StartMenu implements Screen {

    private static final Logger logger = LoggerFactory.getLogger(StartMenu.class);

    private OrthographicCamera camera;

    private Texture backgroundImage;

    private Stage stage;

    private BitmapFont titleFont;

    private BitmapFont subtitleFont;

    private BitmapFont startFont;

    private ArrayList<GameInitializeListener> gameInitializeListeners;

    private Skin neonUISkin;

    private Button startGameButton;

    private SelectBox<String> difficultlySelect;

    private SelectBox<String> numberOfPlayerSelect;

    private TextField seedTextField;

    private Button howToPlayButton;

    private GlyphLayout titleLayout;

    private  GlyphLayout subtitleLayout;

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
        subtitleFont = generator.generateFont(parameter);
        generator.dispose();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Abel-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        startFont = generator.generateFont(parameter);
        generator.dispose();

        titleLayout = new GlyphLayout(titleFont, "Forbidden Island");

        String startText = "Please enter the following information";
        subtitleLayout = new GlyphLayout(startFont, startText);

        neonUISkin = new Skin(Gdx.files.internal("./ui/neon/neon-ui.json"));
        startGameButton = new TextButton("Start Game", neonUISkin);
        stage.addActor(startGameButton);
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (GameInitializeListener e : StartMenu.this.gameInitializeListeners) {
                    int NumberOfPlayers = Integer.parseInt(StartMenu.this.numberOfPlayerSelect.getSelected());
                    int Difficulty = GameState.NOVICE;
                    String SelectedDifficulty = StartMenu.this.difficultlySelect.getSelected();
                    logger.info(SelectedDifficulty);
                    switch (SelectedDifficulty) {
                        case "Normal":
                            Difficulty = GameState.NORMAL;
                            break;
                        case "Elite":
                            Difficulty = GameState.ELITE;
                            break;
                        case "Legendary":
                            Difficulty = GameState.LEGENDARY;
                            break;
                    }
                    logger.info(Difficulty + "");
                    e.onInitialize(NumberOfPlayers, Difficulty);
                }
            }
        });

        float headingHeight =  (GameConfiguration.height * 5 / 6f) - titleLayout.height - subtitleLayout.height - 100f;

        difficultlySelect = new SelectBox<>(neonUISkin);
        stage.addActor(difficultlySelect);
        difficultlySelect.setItems("Novice", "Normal", "Elite", "Legendary");
        difficultlySelect.setSelected("Novice");
        difficultlySelect.setSize(difficultlySelect.getPrefWidth(), difficultlySelect.getPrefWidth());
        difficultlySelect.setX((GameConfiguration.width - difficultlySelect.getPrefWidth()) / 2f);
        difficultlySelect.setY(headingHeight - 50f);

        numberOfPlayerSelect = new SelectBox<>(neonUISkin);
        stage.addActor(numberOfPlayerSelect);
        numberOfPlayerSelect.setItems("2", "3", "4");
        numberOfPlayerSelect.setSelected("4");
        numberOfPlayerSelect.setSize(numberOfPlayerSelect.getPrefWidth(), numberOfPlayerSelect.getPrefWidth());
        numberOfPlayerSelect.setX((GameConfiguration.width - numberOfPlayerSelect.getPrefWidth()) / 2f);
        numberOfPlayerSelect.setY(headingHeight - 50f - difficultlySelect.getPrefHeight() - 25f);

        seedTextField = new TextField("Seed", neonUISkin);
        stage.addActor(seedTextField);
        seedTextField.setSize(seedTextField.getPrefWidth(), seedTextField.getPrefHeight());
        seedTextField.setX((GameConfiguration.width - seedTextField.getPrefWidth()) / 2f);
        seedTextField.setY(headingHeight - 50f - numberOfPlayerSelect.getPrefHeight() - difficultlySelect.getPrefHeight() - 50f);

        startGameButton.setX((GameConfiguration.width - startGameButton.getPrefWidth()) / 2f);
        startGameButton.setY(headingHeight - 50f - numberOfPlayerSelect.getPrefHeight() - difficultlySelect.getPrefHeight() - 75f - seedTextField.getPrefHeight() - startGameButton.getPrefHeight());

        howToPlayButton = new TextButton("How to Play", neonUISkin);
        stage.addActor(howToPlayButton);
        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.getBatch().begin();

        float titleFontX = 0 + (GameConfiguration.width - titleLayout.width) / 2;
        float titleFontY = 0 + (GameConfiguration.height * 5 / 6f);

        titleFont.draw(stage.getBatch(), titleLayout, titleFontX, titleFontY);

        float subtitleFontX = 0 + (GameConfiguration.width - subtitleLayout.width) / 2;
        float subtitleFontY = 0 + titleFontY - titleLayout.height - 50f;

        subtitleFont.draw(stage.getBatch(), subtitleLayout, subtitleFontX, subtitleFontY);

        final String numberOfPlayerSelectText = "Number of Players:";
        final GlyphLayout numberOfPlayerSelectLayout = new GlyphLayout(startFont, numberOfPlayerSelectText);

        float fontX = 0 + (GameConfiguration.width /2f - numberOfPlayerSelect.getWidth()/2) - numberOfPlayerSelectLayout.width;
        float fontY = 0 + GameConfiguration.height/2f + numberOfPlayerSelect.getHeight() * 7 / 4;

        //startFont.draw(stage.getBatch(), numberOfPlayerSelectText, fontX, fontY);

        final String difficultySelectText = "Difficulty:";
        final GlyphLayout difficultySelectLayout = new GlyphLayout(startFont, difficultySelectText);

        fontX = 0 + (GameConfiguration.width /2f - difficultlySelect.getWidth()/2) - difficultySelectLayout.width;
        fontY = 0 + GameConfiguration.height/2f + difficultlySelect.getHeight() * 3 / 4;

        //startFont.draw(stage.getBatch(), difficultySelectText, fontX, fontY);

        final String seedTextFieldText = "Seed #:";
        final GlyphLayout seedTextFieldLayout = new GlyphLayout(startFont, seedTextFieldText);

        fontX = 0 + (GameConfiguration.width /2f - seedTextField.getWidth()/2) - seedTextFieldLayout.width;
        fontY = 0 + GameConfiguration.height/2f - seedTextField.getHeight();

        //startFont.draw(stage.getBatch(), seedTextFieldText, fontX, fontY);

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

        float headingHeight =  (GameConfiguration.height * 5 / 6f) - titleLayout.height - subtitleLayout.height - 100f;

        difficultlySelect.setX((GameConfiguration.width - difficultlySelect.getPrefWidth()) / 2f);
        difficultlySelect.setY(headingHeight - 50f - difficultlySelect.getPrefHeight());

        numberOfPlayerSelect.setX((GameConfiguration.width - numberOfPlayerSelect.getPrefWidth()) / 2f);
        numberOfPlayerSelect.setY(headingHeight - 50f - difficultlySelect.getPrefHeight() - 25f - numberOfPlayerSelect.getPrefHeight());

        seedTextField.setX((GameConfiguration.width - seedTextField.getPrefWidth()) / 2f);
        seedTextField.setY(headingHeight - 50f - numberOfPlayerSelect.getPrefHeight() - difficultlySelect.getPrefHeight() - 50f - seedTextField.getPrefHeight());

        startGameButton.setX((GameConfiguration.width - startGameButton.getPrefWidth()) / 2f);
        startGameButton.setY(headingHeight - 50f - numberOfPlayerSelect.getPrefHeight() - difficultlySelect.getPrefHeight() - 75f - seedTextField.getPrefHeight() - startGameButton.getPrefHeight());

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

    public String getNumPlayers() {
        return numberOfPlayerSelect.getSelected();
    }

    public String getDifficulty() {
        return difficultlySelect.getSelected();
    }

    public String getSeed() {
        return seedTextField.getText();
    }
}
