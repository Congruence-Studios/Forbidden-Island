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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;
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

    private SelectBox<String> numberOfPlayerSelect;

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
        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (GameInitializeListener e : StartMenu.this.gameInitializeListeners) {
                    int NumberOfPlayers = Integer.parseInt(StartMenu.this.numberOfPlayerSelect.getList().getSelected());
                    int Difficulty = 0;
                    String SelectedDifficulty = StartMenu.this.difficultlySelect.getSelected();
                    if (SelectedDifficulty.equals("Novice")) {
                        Difficulty = GameState.NOVICE;
                    }
                    else if (SelectedDifficulty.equals("Normal")){
                        Difficulty = GameState.NORMAL;
                    }
                    else if (SelectedDifficulty.equals("Elite")) {
                        Difficulty = GameState.ELITE;
                    }
                    else if (SelectedDifficulty.equals("Legendary")) {
                        Difficulty = GameState.LEGENDARY;
                    }
                    e.onInitialize(NumberOfPlayers, Difficulty);
                }
            }
        });

        difficultlySelect = new SelectBox<>(neonUISkin);
        difficultlySelect.setItems("Novice", "Normal", "Elite", "Legendary");
        difficultlySelect.setSelected("Novice");
        difficultlySelect.setSize(150, 50);
        stage.addActor(difficultlySelect);

        numberOfPlayerSelect = new SelectBox<>(neonUISkin);
        numberOfPlayerSelect.setItems("2", "3", "4");
        numberOfPlayerSelect.setSelected("4");
        numberOfPlayerSelect.setSize(90, 45);
        stage.addActor(numberOfPlayerSelect);
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

        final String startText = "Please enter the following information";
        final GlyphLayout startLayout = new GlyphLayout(startFont, startText);

        fontX = 0 + (GameConfiguration.width - startLayout.width) / 2;
        fontY = 0 + (GameConfiguration.height + startLayout.height) * 2 / 3f;

        startFont.draw(stage.getBatch(), startText, fontX, fontY);

        final String numberOfPlayerSelectText = "Number of Players:";
        final GlyphLayout numberOfPlayerSelectLayout = new GlyphLayout(startFont, numberOfPlayerSelectText);

        fontX = 0 + (GameConfiguration.width /2f - numberOfPlayerSelect.getWidth()/2) - numberOfPlayerSelectLayout.width;
        fontY = 0 + GameConfiguration.height/2f + numberOfPlayerSelect.getHeight() * 7 / 4;

        startFont.draw(stage.getBatch(), numberOfPlayerSelectText, fontX, fontY);

        final String difficultySelectText = "Difficulty:";
        final GlyphLayout difficultySelectLayout = new GlyphLayout(startFont, difficultySelectText);

        fontX = 0 + (GameConfiguration.width /2f - difficultlySelect.getWidth()/2) - difficultySelectLayout.width;
        fontY = 0 + GameConfiguration.height/2f + difficultlySelect.getHeight() * 3 / 4;

        startFont.draw(stage.getBatch(), difficultySelectText, fontX, fontY);

        difficultlySelect.setPosition(GameConfiguration.width /2f - difficultlySelect.getWidth()/2, GameConfiguration.height/2f);
        startGameButton.setPosition(GameConfiguration.width /2f - startGameButton.getWidth()/2 , GameConfiguration.height/2f - difficultlySelect.getHeight());
        numberOfPlayerSelect.setPosition(GameConfiguration.width /2f - numberOfPlayerSelect.getWidth()/2 , GameConfiguration.height/2f + numberOfPlayerSelect.getHeight());

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
