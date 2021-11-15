package com.congruence.ui;

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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;
import com.congruence.state.Resources;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class GameUI implements Screen {

    private GameState gameState;

    private OrthographicCamera camera;

    private Texture backgroundImage;

    private Stage stage;

    private BitmapFont startFont;

    private Map<String, IslandTile> islandTiles;

    private Map<String, Artifact> artifacts;

    private String currentFocusedTile = "";

    private GameMenuSkipButton skipButton;

    private GameMenuBackground gameMenuBackground;

    private TreasureDeckPile treasureDeckPile;

    private FloodDeckPile floodDeckPile;

    public GameUI( GameState gameState ) {
        this.gameState = gameState;
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

        float tileHeight = (GameConfiguration.height - 70f) / 6f;
        float tileWidth = (GameConfiguration.height - 70f) / 6f;
        float positionX = (GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f;
        float positionY = GameConfiguration.height - 10f;

        islandTiles = new TreeMap<>();
        artifacts = new TreeMap<>();

        for (int i = 0; i < gameState.getIslandTiles().length; i++) {
            positionY -= tileHeight;
            for (int j = 0; j < gameState.getIslandTiles()[i].length; j++) {
                positionX -= tileWidth;
                if (gameState.getIslandTiles()[i][j] != null) {
                    final IslandTile islandTile = new IslandTile(positionX, positionY, tileWidth, tileHeight, gameState.getIslandTiles()[i][j],i+""+j ,  gameState.getIslandTileState()[i][j]);
                    islandTiles.put(i + "" + j, islandTile);

                    //Logic for the Actor Events
                    islandTile.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (GameUI.this.currentFocusedTile.equals(islandTile.getCoordinates())) {
                                islandTile.setFocused(false);
                                currentFocusedTile = "";
                            }
                            else {
                                islandTile.setFocused(true);
                                if (!GameUI.this.currentFocusedTile.equals("")) {
                                    GameUI.this.islandTiles.get(currentFocusedTile).setFocused(false);
                                }
                                GameUI.this.currentFocusedTile = islandTile.getCoordinates();
                            }
                        }

                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            islandTile.setHovered(true);
                        }

                        @Override
                        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                            islandTile.setHovered(false);
                        }
                    });
                }
                else if (Resources.DefaultArtifactMapPlacement.containsKey(i+""+j)) {
                    artifacts.put(i+""+j, new Artifact(positionX, positionY, tileWidth, tileHeight, Resources.DefaultArtifactMapPlacement.get(i+""+j)));
                }
                positionX -= 10f;
            }
            positionY -= 10f;
            positionX = (GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f;
        }

        for (IslandTile e: islandTiles.values()) {
            stage.addActor(e);
        }
        for (Artifact e: artifacts.values()) {
            stage.addActor(e);
        }

        gameMenuBackground = new GameMenuBackground(
                10f,
                3 * tileHeight + 30f,
                tileHeight,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f - 20f
        );
        stage.addActor(gameMenuBackground);
        skipButton = new GameMenuSkipButton(
                10f,
                3.5f * tileHeight + 30f,
                0.5f * tileHeight,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f - 20f
        );
        stage.addActor(skipButton);
        treasureDeckPile = new TreasureDeckPile(
                ((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) /2f - tileHeight * 8f / 10f,
                2 * tileHeight + 30f,
                tileHeight,
                tileHeight
        );
        treasureDeckPile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                treasureDeckPile.setFocused(!treasureDeckPile.isFocused());
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                treasureDeckPile.setHovered(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                treasureDeckPile.setHovered(false);
            }
        });
        stage.addActor(treasureDeckPile);
        floodDeckPile = new FloodDeckPile(
                ((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) /2f - tileHeight * 8f / 10f,
                3 * tileHeight + 40f,
                tileHeight,
                tileHeight
        );
        floodDeckPile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                floodDeckPile.setFocused(!treasureDeckPile.isFocused());
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                floodDeckPile.setHovered(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                floodDeckPile.setHovered(false);
            }
        });
        stage.addActor(floodDeckPile);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18/255f, 18/255f, 18/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().end();
        //Render the Island Tiles

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

        float tileHeight = (GameConfiguration.height - 70f) / 6f;
        float tileWidth = (GameConfiguration.height - 70f) / 6f;
        float positionX = (GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f;
        float positionY = GameConfiguration.height - 10f;

        for (int i = 0; i < 6; i++) {
            positionY -= tileHeight;
            for (int j = 0; j < 6; j++) {
                positionX -= tileWidth;
                if (gameState.getIslandTiles()[i][j] != null) {
                    IslandTile e = islandTiles.get(i + "" + j);
                    e.setPositionX(positionX);
                    e.setPositionY(positionY);
                    e.setIslandWidth(tileWidth);
                    e.setIslandHeight(tileHeight);
                }
                else if (Resources.DefaultArtifactMapPlacement.containsKey(i+""+j)) {
                    Artifact e = artifacts.get(i + "" + j);
                    e.setPositionX(positionX);
                    e.setPositionY(positionY);
                    e.setArtifactWidth(tileWidth);
                    e.setArtifactHeight(tileHeight);
                }
                positionX -= 10f;
            }
            positionY -= 10f;
            positionX = (GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f;
        }

        gameMenuBackground.setPositionX(10f);
        gameMenuBackground.setPositiveY(2*tileHeight + 30f);
        gameMenuBackground.setHeight(2*tileHeight + 10f);
        gameMenuBackground.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f - 20f);

        skipButton.setPositionX(10f);
        skipButton.setPositiveY(3.5f*tileHeight + 40f);
        skipButton.setHeight(0.5f * tileHeight);
        skipButton.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f - 20f);

        treasureDeckPile.setPositionX(((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) /2f - tileHeight * 8f / 10f);
        treasureDeckPile.setPositionY( 2 * tileHeight + 30f );
        treasureDeckPile.setTreasureDeckHeight(tileHeight);
        treasureDeckPile.setTreasureDeckWidth(tileHeight);

        floodDeckPile.setPositionX(((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) /2f - tileHeight * 8f / 10f);
        floodDeckPile.setPositionY( 3 * tileHeight + 40f );
        floodDeckPile.setFloodDeckHeight(tileHeight);
        floodDeckPile.setFloodDeckHeight(tileHeight);

        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
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
    }

}
