package com.congruence.ui;

import ch.qos.logback.classic.util.StatusViaSLF4JLoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;
import com.congruence.state.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class GameUI implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(GameUI.class);

    private final GameState gameState;

    private OrthographicCamera camera;

    private Stage stage;

    private Map<Pair, IslandTile> islandTiles;

    private Map<String, Artifact> artifacts;

    private Pair currentFocusedTile = Pair.INVALID;

    private GameMenuSkipButton skipButton;

    private GameMenuShoreUpButton shoreUpButton;

    private GameMenuCollectButton collectButton;

    private GameMenuSwapButton swapButton;

    private GameMenuWaterButton waterButton;

    private GameMenuTurnInfo turnInfo;

    private GameMenuBackground gameMenuBackground;

    private TreasureDeckPile treasureDeckPile;

    private FloodDeckPile floodDeckPile;

    private ArrayList<PlayerHand> playerHands = new ArrayList<>();

    private ArrayList<AbilityCard> abilityCards = new ArrayList<>();

    private WaterMeterScreen waterMeterScreen;

    public GameUI(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void show() {
        Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
        //if (!Gdx.graphics.setFullscreenMode(displayMode)) {
            // switching to full-screen mode failed
        //}

        logger.info("GameUI show called.");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ScreenViewport viewport = new ScreenViewport(camera);

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
                    final IslandTile islandTile = new IslandTile(positionX, positionY, tileWidth, tileHeight, gameState.getIslandTiles()[i][j], new Pair(i, j), gameState.getIslandTileState()[i][j]);
                    islandTiles.put(new Pair(i, j), islandTile);

                    //Logic for the Actor Events
                    islandTile.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (GameUI.this.currentFocusedTile.equals(islandTile.getCoordinates())) {
                                islandTile.setFocused(false);
                                disableShoreUpButton();
                                currentFocusedTile = Pair.INVALID;
                            } else {
                                islandTile.setFocused(true);
                                if (!GameUI.this.currentFocusedTile.isInvalid()) {
                                    GameUI.this.islandTiles.get(currentFocusedTile).setFocused(false);
                                }
                                GameUI.this.currentFocusedTile = islandTile.getCoordinates();
                                enableShoreUpButton(islandTile);
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
                } else if (Resources.DefaultArtifactMapPlacement.containsKey(i + "" + j)) {
                    artifacts.put(i + "" + j, new Artifact(positionX, positionY, tileWidth, tileHeight, Resources.DefaultArtifactMapPlacement.get(i + "" + j)));
                }
                positionX -= 10f;
            }
            positionY -= 10f;
            positionX = (GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f;
        }

        for (IslandTile e : islandTiles.values()) {
            stage.addActor(e);
        }
        for (Artifact e : artifacts.values()) {
            stage.addActor(e);
        }

        gameMenuBackground = new GameMenuBackground(
                10f,
                2 * tileHeight + 30f,
                tileHeight,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f - 20f
        );
        stage.addActor(gameMenuBackground);
        skipButton = new GameMenuSkipButton(
                10f,
                2 * tileHeight + 30f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        stage.addActor(skipButton);
        shoreUpButton = new GameMenuShoreUpButton(
                10f,
                2 * tileHeight + 30f + 0.33f * (2 * tileHeight + 10f) - 5f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        shoreUpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onShoreUpButtonClick();
            }
        });
        stage.addActor(shoreUpButton);
        collectButton = new GameMenuCollectButton(
                10f + (0.33f * (2 * tileHeight + 10f) - 5f) * 8f / 5f,
                2 * tileHeight + 30f + 0.33f * (2 * tileHeight + 10f) - 5f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        stage.addActor(collectButton);
        swapButton = new GameMenuSwapButton(
                10f + (0.33f * (2 * tileHeight + 10f) - 5f) * 8f / 5f,
                2 * tileHeight + 30f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        stage.addActor(swapButton);
        waterButton = new GameMenuWaterButton(
                10f + (0.33f * (2 * tileHeight + 10f) - 5f) * 8f / 5f,
                2 * tileHeight + 30f + 0.66f * (2 * tileHeight + 10f) - 10f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        stage.addActor(waterButton);
        waterButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waterMeterScreen.setOpen(!waterMeterScreen.isOpen());
            }
        });
        turnInfo = new GameMenuTurnInfo(
                10f,
                2 * tileHeight + 30f + 0.66f * (2 * tileHeight + 10f) - 10f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f,
                getCurrentPlayerName()
        );
        stage.addActor(turnInfo);
        treasureDeckPile = new TreasureDeckPile(
                ((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) / 2f - tileHeight * 8f / 10f,
                2 * tileHeight + 30f,
                tileWidth,
                tileHeight
        );
        stage.addActor(treasureDeckPile);
        floodDeckPile = new FloodDeckPile(
                ((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) / 2f - tileHeight * 8f / 10f,
                3 * tileHeight + 40f,
                tileWidth,
                tileHeight
        );
        stage.addActor(floodDeckPile);
        playerHands.add(new PlayerHand(
                10f,
                10f,
                (tileHeight * 2 + 10f) / 2,
                tileHeight * 2 + 10f,
                gameState.getPlayers().get(gameState.getPlayerOrder().get(0))
        ));
        playerHands.add(new PlayerHand(
                10f,
                GameConfiguration.height - (tileHeight * 2 + 10f) - 10f,
                (tileHeight * 2 + 10f) / 2,
                tileHeight * 2 + 10f,
                gameState.getPlayers().get(gameState.getPlayerOrder().get(1))
        ));
        if (gameState.getMaxTurnLoops() >= 3) {
            playerHands.add(new PlayerHand(
                    GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f,
                    GameConfiguration.height - (tileHeight * 2 + 10f) - 10f,
                    (tileHeight * 2 + 10f) / 2,
                    tileHeight * 2 + 10f,
                    gameState.getPlayers().get(gameState.getPlayerOrder().get(2))
            ));
        }
        if (gameState.getMaxTurnLoops() >= 4) {
            playerHands.add(new PlayerHand(
                    GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f,
                    10f,
                    (tileHeight * 2 + 10f) / 2,
                    tileHeight * 2 + 10f,
                    gameState.getPlayers().get(gameState.getPlayerOrder().get(3))
            ));
        }
        for (PlayerHand ph : playerHands) {
            stage.addActor(ph);
        }
        abilityCards.add(new AbilityCard(
                10f + ((tileHeight * 2 + 10f) / 2),
                10f,
                (tileHeight * 2 + 10f) / 2,
                tileHeight * 2 + 10f,
                gameState.getPlayers().get(gameState.getPlayerOrder().get(0)).getAbility()
        ));
        abilityCards.add(new AbilityCard(
                10f + ((tileHeight * 2 + 10f) / 2),
                GameConfiguration.height - (tileHeight * 2 + 10f) - 10f,
                (tileHeight * 2 + 10f) / 2,
                tileHeight * 2 + 10f,
                gameState.getPlayers().get(gameState.getPlayerOrder().get(1)).getAbility()
        ));
        if (gameState.getMaxTurnLoops() >= 3) {
            abilityCards.add(new AbilityCard(
                    GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f - ((tileHeight * 2 + 10f) / 2),
                    GameConfiguration.height - (tileHeight * 2 + 10f) - 10f,
                    (tileHeight * 2 + 10f) / 2,
                    tileHeight * 2 + 10f,
                    gameState.getPlayers().get(gameState.getPlayerOrder().get(2)).getAbility()
            ));
        }
        if (gameState.getMaxTurnLoops() >= 4) {
            abilityCards.add(new AbilityCard(
                    GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f - ((tileHeight * 2 + 10f) / 2),
                    10f,
                    (tileHeight * 2 + 10f) / 2,
                    tileHeight * 2 + 10f,
                    gameState.getPlayers().get(gameState.getPlayerOrder().get(3)).getAbility()
            ));
        }
        for (AbilityCard ac : abilityCards) {
            stage.addActor(ac);
        }

        waterMeterScreen = new WaterMeterScreen(
                (GameConfiguration.width - (GameConfiguration.height * 3/4f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 3/4f))*0.5f,
                GameConfiguration.height * 3/4f,
                GameConfiguration.height * 3/4f,
                gameState
        );
        stage.addActor(waterMeterScreen);

        floodDeckPile.setEnabled(true);
        treasureDeckPile.setEnabled(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18 / 255f, 18 / 255f, 18 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().end();
        //Render the Island Tiles

        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F) || Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (!Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(1280, 720);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.graphics.setWindowedMode(1280, 720);
        }

    }

    @Override
    public void resize(int width, int height) {
        GameConfiguration.width = Math.max(width, 1280);
        GameConfiguration.height = Math.max(height, 720);

        double ratio = (double) GameConfiguration.height / (double) GameConfiguration.width;

        if (ratio > 9.0 / 16.0) {
            GameConfiguration.height = (int)(GameConfiguration.width * 9.0 / 16.0);
        } else {
            GameConfiguration.width = (int)(GameConfiguration.height * 16.0 / 9.0);
        }

        float tileHeight = (GameConfiguration.height - 70f) / 6f;
        float tileWidth = (GameConfiguration.height - 70f) / 6f;
        float positionX = (GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f;
        float positionY = GameConfiguration.height - 10f;

        for (int i = 0; i < 6; i++) {
            positionY -= tileHeight;
            for (int j = 0; j < 6; j++) {
                positionX -= tileWidth;
                if (gameState.getIslandTiles()[i][j] != null) {
                    IslandTile e = islandTiles.get(new Pair(i, j));
                    e.setPositionX(positionX);
                    e.setPositionY(positionY);
                    e.setIslandWidth(tileWidth);
                    e.setIslandHeight(tileHeight);
                } else if (Resources.DefaultArtifactMapPlacement.containsKey(i + "" + j)) {
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
        gameMenuBackground.setPositiveY(2 * tileHeight + 30f);
        gameMenuBackground.setHeight(2 * tileHeight + 10f);
        gameMenuBackground.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f - 20f);

        skipButton.setPositionX(10f);
        skipButton.setPositiveY(2 * tileHeight + 30f);
        skipButton.setHeight(0.33f * (2 * tileHeight + 10f) - 5f);
        skipButton.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f);

        shoreUpButton.setPositionX(10f);
        shoreUpButton.setPositiveY(2 * tileHeight + 30f + 0.33f * (2 * tileHeight + 10f) - 5f);
        shoreUpButton.setHeight(0.33f * (2 * tileHeight + 10f) - 5f);
        shoreUpButton.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f);

        collectButton.setPositionX(10f + (0.33f * (2 * tileHeight + 10f) - 5f) * 8f / 5f);
        collectButton.setPositiveY(2 * tileHeight + 30f + 0.33f * (2 * tileHeight + 10f) - 5f);
        collectButton.setHeight(0.33f * (2 * tileHeight + 10f) - 5f);
        collectButton.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f);

        swapButton.setPositionX(10f + (0.33f * (2 * tileHeight + 10f) - 5f) * 8f / 5f);
        swapButton.setPositiveY(2 * tileHeight + 30f);
        swapButton.setHeight(0.33f * (2 * tileHeight + 10f) - 5f);
        swapButton.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f);

        waterButton.setPositionX(10f + (0.33f * (2 * tileHeight + 10f) - 5f) * 8f / 5f);
        waterButton.setPositiveY(2 * tileHeight + 30f + 0.66f * (2 * tileHeight + 10f) - 10f);
        waterButton.setHeight(0.33f * (2 * tileHeight + 10f) - 5f);
        waterButton.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f);

        turnInfo.setPositionX(10f);
        turnInfo.setPositiveY(2 * tileHeight + 30f + 0.66f * (2 * tileHeight + 10f) - 10f);
        turnInfo.setHeight(0.33f * (2 * tileHeight + 10f) - 5f);
        turnInfo.setWidth(((GameConfiguration.width) - (GameConfiguration.height)) / 2f);

        treasureDeckPile.setPositionX(((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) / 2f - tileHeight * 8f / 10f);
        treasureDeckPile.setPositionY(2 * tileHeight + 30f);
        treasureDeckPile.setTreasureDeckHeight(tileHeight);
        treasureDeckPile.setTreasureDeckWidth(tileHeight);

        floodDeckPile.setPositionX(((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) / 2f - tileHeight * 8f / 10f);
        floodDeckPile.setPositionY(3 * tileHeight + 40f);
        floodDeckPile.setFloodDeckHeight(tileHeight);
        floodDeckPile.setFloodDeckHeight(tileHeight);

        playerHands.get(0).setPositionX(10f);
        playerHands.get(0).setPositionY(10f);
        playerHands.get(0).setHeight(tileHeight * 2 + 10f);
        playerHands.get(0).setWidth((tileHeight * 2 + 10f) / 2);

        playerHands.get(1).setPositionX(10f);
        playerHands.get(1).setPositionY(GameConfiguration.height - (tileHeight * 2 + 10f) - 10f);
        playerHands.get(1).setHeight(tileHeight * 2 + 10f);
        playerHands.get(1).setWidth((tileHeight * 2 + 10f) / 2);

        if (gameState.getMaxTurnLoops() >= 3) {
            playerHands.get(2).setPositionX(GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f);
            playerHands.get(2).setPositionY(GameConfiguration.height - (tileHeight * 2 + 10f) - 10f);
            playerHands.get(2).setHeight(tileHeight * 2 + 10f);
            playerHands.get(2).setWidth((tileHeight * 2 + 10f) / 2);
        }

        if (gameState.getMaxTurnLoops() >= 4) {
            playerHands.get(3).setPositionX(GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f);
            playerHands.get(3).setPositionY(10f);
            playerHands.get(3).setHeight(tileHeight * 2 + 10f);
            playerHands.get(3).setWidth((tileHeight * 2 + 10f) / 2);
        }

        waterMeterScreen.setPositionX((GameConfiguration.width - (GameConfiguration.height * 3/4f))*0.5f);
        waterMeterScreen.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 3/4f))*0.5f);
        waterMeterScreen.setHeight(GameConfiguration.height * 3/4f);
        waterMeterScreen.setWidth(GameConfiguration.height * 3/4f);

        abilityCards.get(0).setPositionX(10f + ((tileHeight * 2 + 10f) / 2));
        abilityCards.get(0).setPositionY(10f);
        abilityCards.get(0).setHeight(tileHeight * 2 + 10f);
        abilityCards.get(0).setWidth((tileHeight * 2 + 10f) / 2);

        abilityCards.get(1).setPositionX(10f + ((tileHeight * 2 + 10f) / 2));
        abilityCards.get(1).setPositionY(GameConfiguration.height - (tileHeight * 2 + 10f) - 10f);
        abilityCards.get(1).setHeight(tileHeight * 2 + 10f);
        abilityCards.get(1).setWidth((tileHeight * 2 + 10f) / 2);

        if (gameState.getMaxTurnLoops() >= 3) {
            abilityCards.get(2).setPositionX(GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f - ((tileHeight * 2 + 10f) / 2));
            abilityCards.get(2).setPositionY(GameConfiguration.height - (tileHeight * 2 + 10f) - 10f);
            abilityCards.get(2).setHeight(tileHeight * 2 + 10f);
            abilityCards.get(2).setWidth((tileHeight * 2 + 10f) / 2);
        }

        if (gameState.getMaxTurnLoops() >= 4) {
            abilityCards.get(3).setPositionX(GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f - ((tileHeight * 2 + 10f) / 2));
            abilityCards.get(3).setPositionY(10f);
            abilityCards.get(3).setHeight(tileHeight * 2 + 10f);
            abilityCards.get(3).setWidth((tileHeight * 2 + 10f) / 2);
        }

        logger.info("playerHand 0, height: " + playerHands.get(0).getHeight());

        camera.setToOrtho(false, GameConfiguration.width, GameConfiguration.height);
        stage.getViewport().update(GameConfiguration.width, GameConfiguration.height, true);
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

    public void enableShoreUpButton(IslandTile e) {
        int x = e.getCoordinates().x;
        int y = e.getCoordinates().y;
        if (gameState.getIslandTileState()[x][y] == GameState.FLOODED_ISLAND_TILE) {
            shoreUpButton.setEnabled(true);
        }
        else {
            shoreUpButton.setEnabled(false);
        }
    }

    public void disableShoreUpButton() {
        shoreUpButton.setEnabled(false);
    }

    public void onShoreUpButtonClick() {
        int x = currentFocusedTile.x;
        int y = currentFocusedTile.y;
        if (x >= 0 && y >= 0 && shoreUpButton.isEnabled()) {
            gameState.getIslandTileState()[x][y] = GameState.NORMAL_ISLAND_TILE;
            islandTiles.get(currentFocusedTile).setTileState(GameState.NORMAL_ISLAND_TILE);
            shoreUpButton.setEnabled(false);
        }
    }

    public String getCurrentPlayerName() {
        String pName = gameState.getPlayerOrder().get(gameState.getTurnNumber());
        if (pName == null) {
            return "Player " + gameState.getTurnNumber();
        }
        else return pName;
    }



}