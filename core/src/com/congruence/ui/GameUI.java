package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.congruence.GameConfiguration;
import com.congruence.state.*;
import com.congruence.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameUI implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(GameUI.class);

    private final GameState gameState;

    private OrthographicCamera camera;

    private Stage stage;

    private Map<Pair, IslandTile> islandTiles;

    private Map<String, Artifact> artifacts;

    private Pair currentFocusedTile = Pair.INVALID;

    private IslandTile currentFocusedTileInstance = null;

    private int currentNormalPawn = -1;

    private boolean currentPawnFocused = false;

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

    private InfoScreen infoScreen;

    private TurnChangeScreen turnChangeScreen;

    private ArrayList<Pawn> pawns = new ArrayList<>();

    private DrawTreasureCard drawTreasureCard;

    private DrawFloodCard drawFloodCard;

    private ShoredUpDialog shoredUpDialog;

    public static int MOVEMENT_MODE = 1;

    public static int SHORE_UP_MODE = 2;

    public static int ISLAND_SINK_MOVEMENT_MODE = 3;

    private int mode;

    private Pair previousShoredUpTile;

    private ResultScreen resultScreen;

    private GiveCardScreen giveCardScreen;

    private Player currentSuddenDeathPlayer;

    private int currentSuddenDeathPlayerPawn;

    private Queue<Integer> suddenDeathPlayerQueue;

    private Observable suddenDeathObservable;

    public GameUI(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void show() {

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
            for (int j = gameState.getIslandTiles()[i].length - 1; j >= 0; j--) {
                positionX -= tileWidth;
                if (gameState.getIslandTiles()[i][j] != null) {
                    final IslandTile islandTile = new IslandTile(positionX, positionY, tileWidth, tileHeight, gameState.getIslandTiles()[i][j], new Pair(i, j), gameState);
                    logger.info("new islandTile: x: " + positionX + " y: " + positionY + " i: " + i + " j: " + j);
                    islandTiles.put(new Pair(i, j), islandTile);

                    //Logic for the Actor Events
                    islandTile.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (islandTile.isCanMove() && mode == MOVEMENT_MODE) {
                                Player tempPlayerData = gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber()));
                                logger.info(islandTile.getCoordinates().toString());
                                IslandTile a = islandTiles.get(new Pair(tempPlayerData.getTileX(), tempPlayerData.getTileY()));
                                if (tempPlayerData.getTilePosition() == 0) {
                                    a.getTilePositionOpen()[0] = true;
                                }
                                else if (tempPlayerData.getTilePosition() == 1) {
                                    a.getTilePositionOpen()[1] = true;
                                }
                                else if (tempPlayerData.getTilePosition() == 2) {
                                    a.getTilePositionOpen()[2] = true;
                                }
                                else {
                                    a.getTilePositionOpen()[3] = true;
                                }
                                if (tempPlayerData.getAbility() == Player.PILOT && !((Math.abs(islandTile.getCoordinates().x - tempPlayerData.getTileX()) <= 1) &&
                                        islandTile.getCoordinates().y - tempPlayerData.getTileY() == 0 || (Math.abs(islandTile.getCoordinates().y - tempPlayerData.getTileY()) <= 1) &&
                                        islandTile.getCoordinates().x - tempPlayerData.getTileX() == 0)) {
                                    tempPlayerData.setCanUseSpecialAction(false);
                                }
                                tempPlayerData.setTileX(islandTile.getCoordinates().x);
                                tempPlayerData.setTileY(islandTile.getCoordinates().y);
                                logger.info(currentNormalPawn + "");
                                pawns.get(currentNormalPawn).setX(findPawnPositionX(currentNormalPawn));
                                pawns.get(currentNormalPawn).setY(findPawnPositionY(currentNormalPawn));
                                IslandTile e = islandTiles.get(new Pair(tempPlayerData.getTileX(), tempPlayerData.getTileY()));
                                if (e.getTilePositionOpen()[0]) {
                                    e.getTilePositionOpen()[0] = false;
                                    tempPlayerData.setTilePosition(0);
                                }
                                else if (e.getTilePositionOpen()[1]) {
                                    e.getTilePositionOpen()[1] = false;
                                    tempPlayerData.setTilePosition(1);
                                }
                                else if (e.getTilePositionOpen()[2]) {
                                    e.getTilePositionOpen()[2] = false;
                                    tempPlayerData.setTilePosition(2);
                                }
                                else {
                                    e.getTilePositionOpen()[3] = false;
                                    tempPlayerData.setTilePosition(3);
                                }
                                eraseMovementTiles();
                                setMovementTiles(currentNormalPawn);
                                registerMove();
                                shoreUpButton.setEnabled(canShoreUp());
                            }
                            else if (islandTile.isCanSuddenDeathMove() && mode == ISLAND_SINK_MOVEMENT_MODE) {

                                IslandTile a = islandTiles.get(new Pair(currentSuddenDeathPlayer.getTileX(), currentSuddenDeathPlayer.getTileY()));
                                if (currentSuddenDeathPlayer.getTilePosition() == 0) {
                                    a.getTilePositionOpen()[0] = true;
                                }
                                else if (currentSuddenDeathPlayer.getTilePosition() == 1) {
                                    a.getTilePositionOpen()[1] = true;
                                }
                                else if (currentSuddenDeathPlayer.getTilePosition() == 2) {
                                    a.getTilePositionOpen()[2] = true;
                                }
                                else {
                                    a.getTilePositionOpen()[3] = true;
                                }
                                currentSuddenDeathPlayer.setTileX(islandTile.getCoordinates().x);
                                currentSuddenDeathPlayer.setTileY(islandTile.getCoordinates().y);
                                logger.info(currentNormalPawn + "");
                                pawns.get(currentSuddenDeathPlayerPawn).setX(findPawnPositionX(currentSuddenDeathPlayerPawn));
                                pawns.get(currentSuddenDeathPlayerPawn).setY(findPawnPositionY(currentSuddenDeathPlayerPawn));
                                IslandTile e = islandTiles.get(new Pair(currentSuddenDeathPlayer.getTileX(), currentSuddenDeathPlayer.getTileY()));
                                if (e.getTilePositionOpen()[0]) {
                                    e.getTilePositionOpen()[0] = false;
                                    currentSuddenDeathPlayer.setTilePosition(0);
                                }
                                else if (e.getTilePositionOpen()[1]) {
                                    e.getTilePositionOpen()[1] = false;
                                    currentSuddenDeathPlayer.setTilePosition(1);
                                }
                                else if (e.getTilePositionOpen()[2]) {
                                    e.getTilePositionOpen()[2] = false;
                                    currentSuddenDeathPlayer.setTilePosition(2);
                                }
                                else {
                                    e.getTilePositionOpen()[3] = false;
                                    currentSuddenDeathPlayer.setTilePosition(3);
                                }
                                if (!suddenDeathPlayerQueue.isEmpty()) {
                                    setSuddenDeathTiles(suddenDeathPlayerQueue.peek());
                                    currentSuddenDeathPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(suddenDeathPlayerQueue.peek()));
                                    setSuddenDeathTiles(suddenDeathPlayerQueue.peek());
                                    suddenDeathPlayerQueue.poll();
                                }
                                else {
                                    eraseSuddenDeathTiles();
                                    suddenDeathObservable.onFinished();
                                }

                            }
                            else if (islandTile.isCanShoreUp() && mode == SHORE_UP_MODE) {
                                if (gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber())).getAbility() == Player.ENGINEER && previousShoredUpTile != islandTile.getCoordinates()) {
                                    if (previousShoredUpTile != null) {
                                        gameState.getIslandTileState()
                                                [islandTile.getCoordinates().x]
                                                [islandTile.getCoordinates().y] = GameState.NORMAL_ISLAND_TILE;
                                        gameState.getIslandTileState()
                                                [previousShoredUpTile.x]
                                                [previousShoredUpTile.y] = GameState.NORMAL_ISLAND_TILE;
                                        ArrayList<String> shoredUpTiles = new ArrayList<>();
                                        shoredUpTiles.add(islandTiles.get(previousShoredUpTile).getTileName());
                                        shoredUpTiles.add(islandTile.getTileName());
                                        shoredUpDialog.setOpen(true, shoredUpTiles);
                                        shoreUpButton.setEnabled(canShoreUp());
                                        gameState.setCurrentPlayerActionsLeft(gameState.getCurrentPlayerActionsLeft()-1);
                                        checkTurn();
                                        previousShoredUpTile = null;
                                    }
                                    else {
                                        previousShoredUpTile = new Pair(islandTile.getCoordinates().x, islandTile.getCoordinates().y);
                                    }
                                }
                                else if (gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber())).getAbility() == Player.ENGINEER && previousShoredUpTile == islandTile.getCoordinates()) {

                                }
                                else {
                                    gameState.getIslandTileState()
                                            [islandTile.getCoordinates().x]
                                            [islandTile.getCoordinates().y] = GameState.NORMAL_ISLAND_TILE;
                                    ArrayList<String> shoredUpTiles = new ArrayList<>();
                                    shoredUpTiles.add(islandTile.getTileName());
                                    shoredUpDialog.setOpen(true, shoredUpTiles);
                                    shoreUpButton.setEnabled(canShoreUp());
                                    gameState.setCurrentPlayerActionsLeft(gameState.getCurrentPlayerActionsLeft()-1);
                                    checkTurn();
                                }
                                eraseShoreUpTiles();
                                setShoreUpTiles();
                            }
                            else {
                                if (GameUI.this.currentFocusedTile.equals(islandTile.getCoordinates())) {
                                    islandTile.setFocused(false);
                                    disableShoreUpButton();
                                    currentFocusedTile = Pair.INVALID;
                                    currentFocusedTileInstance = null;
                                } else {
                                    islandTile.setFocused(true);
                                    if (!GameUI.this.currentFocusedTile.isInvalid()) {
                                        GameUI.this.islandTiles.get(currentFocusedTile).setFocused(false);
                                    }
                                    GameUI.this.currentFocusedTile = islandTile.getCoordinates();
                                    currentFocusedTileInstance = islandTile;
                                    enableShoreUpButton(islandTile);
                                }
                                if (currentNormalPawn != -1) {
                                    pawns.get(currentNormalPawn).setPawnState(Pawn.NORMAL);
                                }
                                currentPawnFocused = false;
                                eraseMovementTiles();
                            }
                        }

                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            islandTile.setHovered(true);
                            //logger.info("enter: " + islandTile.getCoordinates().x + " " + islandTile.getCoordinates().y + " x: " + x + " y: " + y);
                        }

                        @Override
                        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                            islandTile.setHovered(false);
                        }
                    });
                } else if (Resources.DefaultArtifactMapPlacement.containsKey(i + "" + j)) {
                    artifacts.put(i + "" + j, new Artifact(positionX, positionY, tileWidth, tileHeight, Resources.DefaultArtifactMapPlacement.get(i + "" + j), gameState));
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
        skipButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameState.setCurrentPlayerActionsLeft(0);
                checkTurn();
            }
        });
        shoreUpButton = new GameMenuShoreUpButton(
                10f,
                2 * tileHeight + 30f + 0.33f * (2 * tileHeight + 10f) - 5f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        shoreUpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (shoreUpButton.isEnabled()) {
                    if (gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber())).getAbility() == Player.ENGINEER && previousShoredUpTile != null) {
                        gameState.getIslandTileState()
                                [previousShoredUpTile.x]
                                [previousShoredUpTile.y] = GameState.NORMAL_ISLAND_TILE;
                        ArrayList<String> shoredUpTiles = new ArrayList<>();
                        shoredUpTiles.add(islandTiles.get(previousShoredUpTile).getTileName());
                        shoredUpDialog.setOpen(true, shoredUpTiles);
                        shoreUpButton.setEnabled(canShoreUp());
                        gameState.setCurrentPlayerActionsLeft(gameState.getCurrentPlayerActionsLeft()-1);
                        checkTurn();
                        previousShoredUpTile = null;
                    }
                    else {
                        setShoreUpTiles();
                        eraseMovementTiles();
                        mode = SHORE_UP_MODE;
                    }
                }
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
        collectButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                collectArtifact();
            }
        });
        swapButton = new GameMenuSwapButton(
                10f + (0.33f * (2 * tileHeight + 10f) - 5f) * 8f / 5f,
                2 * tileHeight + 30f,
                0.33f * (2 * tileHeight + 10f) - 5f,
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        stage.addActor(swapButton);
        swapButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (swapButton.isEnabled()) {
                    giveCardScreen.setOpen(true, ()->{
                        gameState.setCurrentPlayerActionsLeft(gameState.getCurrentPlayerActionsLeft()-1);
                        setCanGive();
                        swapButton.setEnabled(gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber())).getCardsAtHand().size() > 0);
                        checkTurn();
                    });
                }
            }
        });
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
                ((GameConfiguration.width) - (GameConfiguration.height)) / 2f
        );
        stage.addActor(turnInfo);
        turnInfo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                infoScreen.setOpen(!infoScreen.isOpen());
            }
        });
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
                gameState,
                10f,
                10f,
                (tileHeight * 2 + 10f) / 2,
                tileHeight * 2 + 10f,
                gameState.getPlayers().get(gameState.getPlayerOrder().get(0))
        ));
        playerHands.get(0).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (PlayerHand p : playerHands) {
                    p.setFocused(false);
                }
                playerHands.get(0).setFocused(true);
                setCanGive();
            }
        });
        playerHands.add(new PlayerHand(
                gameState,
                10f,
                GameConfiguration.height - (tileHeight * 2 + 10f) - 10f,
                (tileHeight * 2 + 10f) / 2,
                tileHeight * 2 + 10f,
                gameState.getPlayers().get(gameState.getPlayerOrder().get(1))
        ));
        playerHands.get(1).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (PlayerHand p : playerHands) {
                    p.setFocused(false);
                }
                playerHands.get(1).setFocused(true);
                setCanGive();
            }
        });
        if (gameState.getMaxTurnLoops() >= 3) {
            playerHands.add(new PlayerHand(
                    gameState,
                    GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f,
                    GameConfiguration.height - (tileHeight * 2 + 10f) - 10f,
                    (tileHeight * 2 + 10f) / 2,
                    tileHeight * 2 + 10f,
                    gameState.getPlayers().get(gameState.getPlayerOrder().get(2))
            ));
            playerHands.get(2).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for (PlayerHand p : playerHands) {
                        p.setFocused(false);
                    }
                    playerHands.get(2).setFocused(true);
                    setCanGive();
                }
            });
        }
        if (gameState.getMaxTurnLoops() >= 4) {
            playerHands.add(new PlayerHand(
                    gameState,
                    GameConfiguration.width - ((tileHeight * 2 + 10f) / 2) - 10f,
                    10f,
                    (tileHeight * 2 + 10f) / 2,
                    tileHeight * 2 + 10f,
                    gameState.getPlayers().get(gameState.getPlayerOrder().get(3))
            ));
            playerHands.get(3).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for (PlayerHand p : playerHands) {
                        p.setFocused(false);
                    }
                    playerHands.get(3).setFocused(true);
                    setCanGive();
                }
            });
        }
        for (PlayerHand ph : playerHands) {
            stage.addActor(ph);
        }
        setCanGive();
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
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(0));
        logger.info(tempPlayer.toString());
        pawns.add(new Pawn(
                findPawnPositionX(0),
                findPawnPositionY(0),
                tileWidth / 4,
                tileHeight / 3,
                tempPlayer.getAbility()
        ));
        logger.info("Pawn 1: X: " + tempPlayer.getTileX() + " Y: " + tempPlayer.getTileY());
        tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(1));
        pawns.add(new Pawn(
                findPawnPositionX(1),
                findPawnPositionY(1),
                tileWidth / 4,
                tileHeight / 3,
                tempPlayer.getAbility()
        ));
        logger.info("Pawn 2: X: " + tempPlayer.getTileX() + " Y: " + tempPlayer.getTileY());
        if (gameState.getMaxTurnLoops() >= 3) {
            tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(2));
            pawns.add(new Pawn(
                    findPawnPositionX(2),
                    findPawnPositionY(2),
                    tileWidth / 4,
                    tileHeight / 3,
                    tempPlayer.getAbility()
            ));
            logger.info("Pawn 3: X: " + tempPlayer.getTileX() + " Y: " + tempPlayer.getTileY());
        }
        if (gameState.getMaxTurnLoops() >= 4) {
            tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(3));
            pawns.add(new Pawn(
                    findPawnPositionX(3),
                    findPawnPositionY(3),
                    tileWidth / 4,
                    tileHeight / 3,
                    tempPlayer.getAbility()
            ));
            logger.info("Pawn 4: X: " + tempPlayer.getTileX() + " Y: " + tempPlayer.getTileY());
        }
        for (int i = 0; i < pawns.size(); i++) {
            Pawn p = pawns.get(i);
            int finalI = i;
            p.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (finalI == gameState.getTurnNumber()) {
                        if (currentPawnFocused) {
                            pawns.get(currentNormalPawn).setPawnState(Pawn.NORMAL);
                            currentPawnFocused = false;
                            eraseMovementTiles();
                        }
                        else {
                            currentFocusedTile = Pair.INVALID;
                            if (currentFocusedTileInstance != null) {
                                currentFocusedTileInstance.setFocused(false);
                                currentFocusedTileInstance = null;
                            }
                            currentPawnFocused = true;
                            mode = MOVEMENT_MODE;
                            setMovementTiles(finalI);
                        }
                    }
                    //Navigator is Able to Move Other People
                    else if (gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber())).getAbility() == Player.NAVIGATOR) {

                    }
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                }
            });
            logger.info("Pawn " + i + ": " + p.toString());
            stage.addActor(p);
        }

        waterMeterScreen = new WaterMeterScreen(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f,
                GameConfiguration.height * 15/16f,
                gameState
        );
        stage.addActor(waterMeterScreen);

        infoScreen = new InfoScreen(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f,
                GameConfiguration.height * 15/16f,
                gameState
        );
        stage.addActor(infoScreen);

        turnChangeScreen = new TurnChangeScreen(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f * (750/1600f),
                GameConfiguration.height * 15/16f,
                gameState
        );
        stage.addActor(turnChangeScreen);
        turnChangeScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                turnChangeScreen.setOpen(false);
            }
        });
        drawTreasureCard = new DrawTreasureCard(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f * (750/1600f),
                GameConfiguration.height * 15/16f,
                gameState,
                this
        );
        stage.addActor(drawTreasureCard);
        drawFloodCard = new DrawFloodCard(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f * (750/1600f),
                GameConfiguration.height * 15/16f,
                gameState
        );
        stage.addActor(drawFloodCard);
        drawFloodCard.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                drawFloodCard.setOpen(false, drawFloodCard.getObservable());
                drawFloodCard.getObservable().onFinished();
                drawFloodCard.setObservable(null);
            }
        });
        shoredUpDialog = new ShoredUpDialog(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f * (750/1600f),
                GameConfiguration.height * 15/16f,
                gameState
        );
        stage.addActor(shoredUpDialog);
        shoredUpDialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shoredUpDialog.setOpen(false, null);
            }
        });
        giveCardScreen = new GiveCardScreen(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f * (750/1600f),
                GameConfiguration.height * 15/16f,
                gameState
        );
        stage.addActor(giveCardScreen);
        giveCardScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giveCardScreen.setOpen(false, null);
            }
        });
        resultScreen = new ResultScreen(
                (GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f,
                (GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f,
                GameConfiguration.height * 15/16f,
                GameConfiguration.height * 15/16f,
                gameState
        );
        stage.addActor(resultScreen);

        floodDeckPile.setEnabled(true);
        treasureDeckPile.setEnabled(true);


        ///////////////////////////////////////////
        /////////////SET THE UI FOR THE FIRST PLAYER TUN
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////

        //setMovementTiles();
        //Set the First Pawn
        currentNormalPawn = 0;
        shoreUpButton.setEnabled(canShoreUp());

        for (int i = 0; i < pawns.size(); i++) {
            IslandTile a = islandTiles.get(new Pair(gameState.getPlayers().get(gameState.getPlayerOrder().get(i)).getTileX(), gameState.getPlayers().get(gameState.getPlayerOrder().get(i)).getTileY()));
            a.getTilePositionOpen()[gameState.getPlayers().get(gameState.getPlayerOrder().get(i)).getTilePosition()] = false;
        }

        turnChangeScreen.setOpen(true);

        if (!Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode())) {
            logger.warn("Could Not Display In Full Screen");
        }

        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
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
            for (int j = 5; j >= 0; j--) {
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
        treasureDeckPile.setPositionY(2 * tileHeight + 20f);
        treasureDeckPile.setTreasureDeckHeight(tileHeight);
        treasureDeckPile.setTreasureDeckWidth(tileHeight);

        floodDeckPile.setPositionX(((GameConfiguration.width - 10f) + ((GameConfiguration.width - 10f) - ((GameConfiguration.width) - (GameConfiguration.height)) / 2f)) / 2f - tileHeight * 8f / 10f);
        floodDeckPile.setPositionY(3 * tileHeight + 30f);
        floodDeckPile.setFloodDeckHeight(tileHeight);
        floodDeckPile.setFloodDeckHeight(tileHeight);


        playerHands.get(0).setPositionX(10f);
        playerHands.get(0).setPositionY(GameConfiguration.height - tileHeight - 10f);
        playerHands.get(0).setWidth(tileWidth * 2 + 10f);
        playerHands.get(0).setHeight(tileHeight);
        logger.info(0 + " " + playerHands.get(0).getPositionX() + " " + playerHands.get(0).getPositionY());

        playerHands.get(1).setPositionX(GameConfiguration.width - 10f - (tileWidth * 2 + 10f));
        playerHands.get(1).setPositionY(GameConfiguration.height - tileHeight - 10f);
        playerHands.get(1).setWidth(tileWidth * 2 + 10f);
        playerHands.get(1).setHeight(tileHeight);
        logger.info(1 + " " + playerHands.get(1).getPositionX() + " " + playerHands.get(1).getPositionY());

        if (gameState.getMaxTurnLoops() >= 3) {
            playerHands.get(2).setPositionX(GameConfiguration.width - 10f - (tileWidth * 2 + 10f));
            playerHands.get(2).setPositionY(10f);
            playerHands.get(2).setWidth(tileWidth * 2 + 10f);
            playerHands.get(2).setHeight(tileHeight);
            logger.info(2 + " " + playerHands.get(2).getPositionX() + " " + playerHands.get(2).getPositionY());
        }

        if (gameState.getMaxTurnLoops() >= 4) {
            playerHands.get(3).setPositionX(10f);
            playerHands.get(3).setPositionY(10f);
            playerHands.get(3).setWidth(tileWidth * 2 + 10f);
            playerHands.get(3).setHeight(tileHeight);
            logger.info(3 + " " + playerHands.get(3).getPositionX() + " " + playerHands.get(3).getPositionY());
        }

        waterMeterScreen.setPositionX((GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f);
        waterMeterScreen.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f);
        waterMeterScreen.setHeight(GameConfiguration.height * 15/16f);
        waterMeterScreen.setWidth(GameConfiguration.height * 15/16f);

        infoScreen.setPositionX((GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f);
        infoScreen.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 15/16f))*0.5f);
        infoScreen.setHeight(GameConfiguration.height * 15/16f);
        infoScreen.setWidth(GameConfiguration.height * 15/16f);

        turnChangeScreen.setPositionX((GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f);
        turnChangeScreen.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 15/16f * 750f/1600f))*0.5f);
        turnChangeScreen.setHeight(GameConfiguration.height * 15/16f * (750/1600f));
        turnChangeScreen.setWidth(GameConfiguration.height * 15/16f);

        drawTreasureCard.setPositionX((GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f);
        drawTreasureCard.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 15/16f * 750f/1600f))*0.5f);
        drawTreasureCard.setHeight(GameConfiguration.height * 15/16f * (750/1600f));
        drawTreasureCard.setWidth(GameConfiguration.height * 15/16f);

        drawFloodCard.setPositionX((GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f);
        drawFloodCard.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 15/16f * 750f/1600f))*0.5f);
        drawFloodCard.setHeight(GameConfiguration.height * 15/16f * (750/1600f));
        drawFloodCard.setWidth(GameConfiguration.height * 15/16f);

        shoredUpDialog.setPositionX((GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f);
        shoredUpDialog.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 15/16f * 750f/1600f))*0.5f);
        shoredUpDialog.setHeight(GameConfiguration.height * 15/16f * (750/1600f));
        shoredUpDialog.setWidth(GameConfiguration.height * 15/16f);

        giveCardScreen.setPositionX((GameConfiguration.width - (GameConfiguration.height * 15/16f))*0.5f);
        giveCardScreen.setPositiveY((GameConfiguration.height - (GameConfiguration.height * 15/16f * 750f/1600f))*0.5f);
        giveCardScreen.setHeight(GameConfiguration.height * 15/16f * (750/1600f));
        giveCardScreen.setWidth(GameConfiguration.height * 15/16f);

        abilityCards.get(0).setPositionX(10f);
        abilityCards.get(0).setPositionY(GameConfiguration.height - (tileHeight * 2 + 10f) - 10f);
        abilityCards.get(0).setHeight(tileHeight);
        abilityCards.get(0).setWidth(tileWidth);
        logger.info(0 + " " + abilityCards.get(0).getPositionX() + " " + abilityCards.get(0).getPositionY());

        abilityCards.get(1).setPositionX(GameConfiguration.width - (tileWidth * 2 + 10f) / 2 - 10f);
        abilityCards.get(1).setPositionY(GameConfiguration.height - (tileHeight * 2 + 10f) - 10f);
        abilityCards.get(1).setHeight(tileHeight);
        abilityCards.get(1).setWidth(tileWidth);
        logger.info(1 + " " + abilityCards.get(1).getPositionX() + " " + abilityCards.get(1).getPositionY());

        if (gameState.getMaxTurnLoops() >= 3) {
            abilityCards.get(2).setPositionX(GameConfiguration.width - (tileWidth * 2 + 10f) / 2 - 10f);
            abilityCards.get(2).setPositionY(10f + (tileHeight * 2 + 10f) / 2);
            abilityCards.get(2).setHeight(tileHeight);
            abilityCards.get(2).setWidth(tileWidth);
            //logger.info(2 + " " + abilityCards.get(2).getPositionX() + " " + abilityCards.get(2).getPositionY());
        }

        if (gameState.getMaxTurnLoops() >= 4) {
            abilityCards.get(3).setPositionX(10f);
            abilityCards.get(3).setPositionY(10f + (tileHeight * 2 + 10f) / 2);
            abilityCards.get(3).setHeight(tileHeight);
            abilityCards.get(3).setWidth(tileWidth);
            logger.info(3 + " " + abilityCards.get(3).getPositionX() + " " + abilityCards.get(3).getPositionY());
        }
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(0));
        pawns.get(0).setX(findInitialsPawnPositionX(0));
        pawns.get(0).setY(findInitialsPawnPositionY(0));
        pawns.get(0).setWidth(tileWidth / 4);
        pawns.get(0).setHeight(tileHeight / 3);
        logger.info(pawns.get(0).toString());
        tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(1));
        pawns.get(1).setX(findInitialsPawnPositionX(1));
        pawns.get(1).setY(findInitialsPawnPositionY(1));
        pawns.get(1).setWidth(tileWidth / 4);
        pawns.get(1).setHeight(tileHeight / 3);
        logger.info(pawns.get(1).toString());
        if (gameState.getMaxTurnLoops() >= 3) {
            tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(2));
            pawns.get(2).setX(findInitialsPawnPositionX(2));
            pawns.get(2).setY(findInitialsPawnPositionY(2));
            pawns.get(2).setWidth(tileWidth / 4);
            pawns.get(2).setHeight(tileHeight / 3);
            logger.info(pawns.get(2).toString());
        }
        if (gameState.getMaxTurnLoops() >= 4) {
            tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(3));
            pawns.get(3).setX(findInitialsPawnPositionX(3));
            pawns.get(3).setY(findInitialsPawnPositionY(3));
            pawns.get(3).setWidth(tileWidth / 4);
            pawns.get(3).setHeight(tileHeight / 3);
            logger.info(pawns.get(3).toString());
        }

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
        logger.info("enableShoreUpButton: x: " + x + " y: " + y + "player x: " +
                gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileX() + " y: " +
                gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileY() + " " +
                gameState.getIslandTileState()[x][y]);
        if (gameState.getIslandTileState()[x][y] == GameState.FLOODED_ISLAND_TILE &&
                gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getAbility() == Player.EXPLORER &&
                Math.abs(gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileX() - x) <= 1 &&
                Math.abs(gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileY() - y) <= 1) {
            shoreUpButton.setEnabled(true);
            logger.info("1");
        } else if (gameState.getIslandTileState()[x][y] == GameState.FLOODED_ISLAND_TILE &&
                ((Math.abs(gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileX() - x) <= 1 &&
                        gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileY() == y) ||
                (Math.abs(gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileY() - y) <= 1 &&
                        gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).getTileX() == x))) {
            shoreUpButton.setEnabled(true);
            logger.info("2");
        }
        else {
            shoreUpButton.setEnabled(false);
            logger.info("3");
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
            registerMove();
        }
    }

    public String getCurrentPlayerName() {
        String pName = gameState.getPlayerOrder().get(gameState.getTurnNumber());
        if (pName == null) {
            return "Player " + gameState.getTurnNumber();
        }
        else return pName;
    }

    public float findPawnPositionX( int player ) {
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(player));

        float tileHeight = (GameConfiguration.height - 70f) / 6f;
        float tileWidth = (GameConfiguration.height - 70f) / 6f;
        IslandTile e = islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()));

        if (e.getTilePositionOpen()[0]) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 4) - (tileWidth / 4) / 2;
        }
        else if (e.getTilePositionOpen()[1]) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 2);
        }
        else if (e.getTilePositionOpen()[2]) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 4) - (tileWidth / 4) / 2;
        }
        else {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 2);
        }
    }

    public float findPawnPositionY( int player ) {
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(player));

        float tileHeight = (GameConfiguration.height - 70f) / 6f - 15f;
        float tileWidth = (GameConfiguration.height - 70f) / 6f;
        IslandTile e = islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()));
        if (e.getTilePositionOpen()[0]) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 4) - (tileHeight / 3) / 2;
        }
        else if (e.getTilePositionOpen()[1]) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 4) - (tileHeight / 3) / 2;
        }
        else if (e.getTilePositionOpen()[2]) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 2);
        }
        else {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 2);
        }
    }

    public float findInitialsPawnPositionX( int player ) {
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(player));

        float tileHeight = (GameConfiguration.height - 70f) / 6f;
        float tileWidth = (GameConfiguration.height - 70f) / 6f;
        IslandTile e = islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()));

        if (tempPlayer.getTilePosition() == 0) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 4) - (tileWidth / 4) / 2;
        }
        else if (tempPlayer.getTilePosition() == 1) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 2);
        }
        else if (tempPlayer.getTilePosition() == 2) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 4) - (tileWidth / 4) / 2;
        }
        else {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionX() + (tileWidth / 2);
        }
    }

    public float findInitialsPawnPositionY( int player ) {
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(player));

        float tileHeight = (GameConfiguration.height - 70f) / 6f - 15f;
        float tileWidth = (GameConfiguration.height - 70f) / 6f;
        IslandTile e = islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()));
        if (tempPlayer.getTilePosition() == 0) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 4) - (tileHeight / 3) / 2;
        }
        else if (tempPlayer.getTilePosition() == 1) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 4) - (tileHeight / 3) / 2;
        }
        else if (tempPlayer.getTilePosition() == 2) {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 2);
        }
        else {
            return islandTiles.get(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY())).getPositionY() + 15f + (tileHeight / 2);
        }
    }


    public void setMovementTiles(int player) {
        eraseMovementTiles();
        eraseShoreUpTiles();

        Pawn current = pawns.get(player);
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(player));
        ArrayList<Pair> movableTiles = new ArrayList<>();
        movableTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()));
        movableTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()));
        movableTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()+1));
        movableTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()-1));
        if (tempPlayer.getAbility() == Player.PILOT && tempPlayer.isCanUseSpecialAction()) {
            movableTiles.addAll(islandTiles.keySet());
        }
        else if (tempPlayer.getAbility() == Player.DIVER) {
            TreeSet<Pair> currentTiles = new TreeSet<>();
            TreeSet<Pair> nextTiles = new TreeSet<>();
            currentTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()));
            currentTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()));
            currentTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()+1));
            currentTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()-1));
            boolean changesMade = false;
            while (!currentTiles.isEmpty()) {
                for (Pair i : currentTiles) {
                    if (islandTiles.get(i) != null && gameState.getIslandTileState()[i.x][i.y] == GameState.NORMAL_ISLAND_TILE) {
                        nextTiles.add(i);
                    } else if (islandTiles.get(i) != null && (gameState.getIslandTileState()[i.x][i.y] == GameState.FLOODED_ISLAND_TILE ||
                            gameState.getIslandTileState()[i.x][i.y] == GameState.SUNKEN_ISLAND_TILE)) {
                        if (islandTiles.get(new Pair(i.x+1, i.y)) != null &&
                                islandTiles.get(new Pair(i.x+1, i.y)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x+1, i.y))) {
                                changesMade = true;
                            }
                        }
                        if (islandTiles.get(new Pair(i.x-1, i.y)) != null &&
                                islandTiles.get(new Pair(i.x-1, i.y)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x-1, i.y))) {
                                changesMade = true;
                            }
                        }
                        if (islandTiles.get(new Pair(i.x, i.y+1)) != null &&
                                islandTiles.get(new Pair(i.x, i.y+1)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x, i.y+1))) {
                                changesMade = true;
                            }
                        }
                        if (islandTiles.get(new Pair(i.x, i.y-1)) != null &&
                                islandTiles.get(new Pair(i.x, i.y-1)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x, i.y-1))) {
                                changesMade = true;
                            }
                        }
                        nextTiles.add(i);
                    }
                }
                if (changesMade) {
                    currentTiles = new TreeSet<>(nextTiles);
                    changesMade = false;
                } else {
                    break;
                }
            }
            nextTiles.remove(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()));
            TreeSet<Pair> temp = new TreeSet<>();
            temp.addAll(nextTiles);
            temp.addAll(movableTiles);
            movableTiles = new ArrayList<>(temp);
        }
        else if (tempPlayer.getAbility() == Player.EXPLORER) {
            movableTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()+1));
            movableTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()-1));
            movableTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()+1));
            movableTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()-1));
        }
        logger.info(movableTiles.toString());

        current.setPawnState(Pawn.MOVE);

        for (int i = 0; i < movableTiles.size(); i++) {
            Pair e = movableTiles.get(i);
            e.x = Math.max(e.x, 0);
            e.x = Math.min(e.x, 5);
            e.y = Math.max(e.y, 0);
            e.y = Math.min(e.y, 5);
            if (gameState.getIslandTileState()[e.x][e.y] == GameState.SUNKEN_ISLAND_TILE) {
                movableTiles.remove(i);
                i--;
            }
        }

        for (Pair e : movableTiles) {
            if (islandTiles.containsKey(e)) {
                islandTiles.get(e).setCanMove(true);
            }
        }
    }

    public void eraseMovementTiles() {
        for (IslandTile e : islandTiles.values()) {
            e.setCanMove(false);
        }
    }

    public void registerMove() {
        Player e = gameState.getPlayers().get(gameState.getPlayerOrder().get(currentNormalPawn));
        gameState.setCurrentPlayerActionsLeft(gameState.getCurrentPlayerActionsLeft() - 1);
        shoreUpButton.setEnabled(canShoreUp());
        checkTurn();
    }

    public void checkTurn() {
        collectButton.setEnabled(canCollectItems());
        if (gameState.isGameEnd()) {
            resultScreen.setOpen(true);
        }
        if (gameState.getCurrentPlayerActionsLeft() == 0) {
            disableShoreUpButton();
            eraseMovementTiles();
            eraseShoreUpTiles();
            drawTreasureCards(()->drawFloodCards(()->setSuddenDeathMode(this::registerTurnChange)));
        }
    }

    public void registerTurnChange() {
        gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).setCanUseSpecialAction(true);
        gameState.setTurnNumber( gameState.getTurnNumber() + 1 );
        if (gameState.getTurnNumber() >= gameState.getMaxTurnLoops()) {
            gameState.setTurnNumber( 0 );
        }
        gameState.setCurrentPlayerTurn(gameState.getPlayerOrder().get(gameState.getTurnNumber()));
        pawns.get(currentNormalPawn).setPawnState(Pawn.NORMAL);
        currentNormalPawn = gameState.getTurnNumber();
        gameState.setCurrentPlayerActionsLeft(3);
        shoreUpButton.setEnabled(canShoreUp());
        turnChangeScreen.setOpen(true);
        mode = MOVEMENT_MODE;
    }

    public void drawTreasureCards(Observable observable) {
        Stack<TreasureCard> treasureDeck = gameState.getTreasureCardDeck();
        gameState.setCurrentDrawnTreasureCards(new ArrayList<>(2));
        for (int i = 0; i < 2; i++) {
            if (!treasureDeck.empty()) {
                gameState.getCurrentDrawnTreasureCards().add(treasureDeck.pop());
            } else {
                Collections.shuffle(gameState.getTreasureCardDiscardDeck());
                treasureDeck.addAll(gameState.getTreasureCardDiscardDeck());
                gameState.getTreasureCardDiscardDeck().clear();
                gameState.getCurrentDrawnTreasureCards().add(treasureDeck.pop());
            }
        }
        drawTreasureCard.setOpen(true, observable);
        gameState.setDrawingTreasureCards(true);
    }

    public void drawFloodCards(Observable observable) {
        Stack<FloodCard> floodDeck = gameState.getIslandTileDeck();
        gameState.setCurrentDrawnIslandTileCards(new ArrayList<>(5));
        for (int i = 0; i < gameState.getCardsToDraw(); i++) {
            if (!floodDeck.empty()) {
                gameState.getCurrentDrawnIslandTileCards().add(floodDeck.peek());
                lowerIslandTileState(floodDeck.peek().getName());
                gameState.getIslandTileDiscardDeck().add(floodDeck.pop());
            } else {
                Collections.shuffle(gameState.getIslandTileDiscardDeck());
                floodDeck.addAll(gameState.getIslandTileDiscardDeck());
                gameState.getIslandTileDiscardDeck().clear();
                lowerIslandTileState(floodDeck.peek().getName());
                gameState.getCurrentDrawnIslandTileCards().add(floodDeck.pop());
            }
        }
        drawFloodCard.setOpen(true, observable);
    }

    public void lowerIslandTileState(String islandTileName) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (gameState.getIslandTiles()[i][j] != null) {
                    if (gameState.getIslandTiles()[i][j].equals(islandTileName)) {
                        if (gameState.getIslandTileState()[i][j] == GameState.NORMAL_ISLAND_TILE) {
                            gameState.getIslandTileState()[i][j] = GameState.FLOODED_ISLAND_TILE;
                        }
                        else if (gameState.getIslandTileState()[i][j] == GameState.FLOODED_ISLAND_TILE) {
                            gameState.getIslandTileState()[i][j] = GameState.SUNKEN_ISLAND_TILE;
                        }
                    }
                }
            }
        }
    }

    public boolean canShoreUp() {
        Player p = gameState.getPlayers().get(gameState.getCurrentPlayerTurn());
        logger.info(currentFocusedTile.toString());
        if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX(), 5), 0)]
                [Math.max(Math.min(p.getTileY(), 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            return true;
        else if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX()+1, 5), 0)]
                [Math.max(Math.min(p.getTileY(), 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            return true;
        else if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX(), 5), 0)]
                [Math.max(Math.min(p.getTileY()+1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            return true;
        else if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX()-1, 5), 0)]
                [Math.max(Math.min(p.getTileY(), 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            return true;
        else if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX(), 5), 0)]
                [Math.max(Math.min(p.getTileY()-1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            return true;
        else if (p.getAbility() == Player.EXPLORER){
            if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX()+1, 5), 0)]
                    [Math.max(Math.min(p.getTileY()+1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                return true;
            else if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX()+1, 5), 0)]
                    [Math.max(Math.min(p.getTileY()-1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                return true;
            else if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX()-1, 5), 0)]
                    [Math.max(Math.min(p.getTileY()+1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                return true;
            else if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX()+1, 5), 0)]
                    [Math.max(Math.min(p.getTileY()-1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                return true;
        }
        return false;
    }

    public void setShoreUpTiles() {
        Player p = gameState.getPlayers().get(gameState.getCurrentPlayerTurn());
        logger.info(currentFocusedTile.toString());
        LinkedList<IslandTile> shoreUpTiles = new LinkedList<>();
        if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX(), 5), 0)]
                [Math.max(Math.min(p.getTileY(), 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX(), 5), 0),Math.max(Math.min(p.getTileY(), 5), 0))));
        if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX()+1, 5), 0)]
                [Math.max(Math.min(p.getTileY(), 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX()+1, 5), 0),Math.max(Math.min(p.getTileY(), 5), 0))));
        if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX(), 5), 0)]
                [Math.max(Math.min(p.getTileY()+1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX(), 5), 0),Math.max(Math.min(p.getTileY()+1, 5), 0))));
        if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX()-1, 5), 0)]
                [Math.max(Math.min(p.getTileY(), 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX()-1, 5), 0),Math.max(Math.min(p.getTileY(), 5), 0))));
        if (gameState.getIslandTileState()
                [Math.max(Math.min(p.getTileX(), 5), 0)]
                [Math.max(Math.min(p.getTileY()-1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
            shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX(), 5), 0),Math.max(Math.min(p.getTileY()-1, 5), 0))));
        if (p.getAbility() == Player.EXPLORER) {
            if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX() + 1, 5), 0)]
                    [Math.max(Math.min(p.getTileY() + 1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX()+1, 5), 0), Math.max(Math.min(p.getTileY()+1, 5), 0))));
            if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX() + 1, 5), 0)]
                    [Math.max(Math.min(p.getTileY() - 1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX()+1, 5), 0), Math.max(Math.min(p.getTileY()-1, 5), 0))));
            if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX() - 1, 5), 0)]
                    [Math.max(Math.min(p.getTileY() + 1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX()-1, 5), 0), Math.max(Math.min(p.getTileY()+1, 5), 0))));
            if (gameState.getIslandTileState()
                    [Math.max(Math.min(p.getTileX() + 1, 5), 0)]
                    [Math.max(Math.min(p.getTileY() - 1, 5), 0)] == GameState.FLOODED_ISLAND_TILE)
                shoreUpTiles.add(islandTiles.get(new Pair(Math.max(Math.min(p.getTileX()-1, 5), 0), Math.max(Math.min(p.getTileY()-1, 5), 0))));
        }
        for (IslandTile e: shoreUpTiles) {
            e.setCanShoreUp(true);
        }
    }

    public void eraseShoreUpTiles() {
        for (IslandTile e : islandTiles.values()) {
            e.setCanShoreUp(false);
        }
    }

    public void setCanGive() {
        for (PlayerHand p : playerHands) {
            logger.info("" + playerHands.get(gameState.getTurnNumber()).isFocused());
        }
        Player currentPlayer = gameState.getPlayers().get(gameState.getCurrentPlayerTurn());
        ArrayList<Player> availablePlayers = new ArrayList<>();
        if (playerHands.get(gameState.getTurnNumber()).isFocused()) {
            for (Player p : gameState.getPlayers().values()) {
                if ((currentPlayer.getTileX() == p.getTileX() && currentPlayer.getTileY() == p.getTileY()) || currentPlayer.getAbility() == Player.MESSENGER) {
                    availablePlayers.add(p);
                }
            }
            availablePlayers.remove(currentPlayer);
            if (!availablePlayers.isEmpty()) {
                swapButton.setEnabled(true);
            } else {
                swapButton.setEnabled(false);
            }
        } else {
            swapButton.setEnabled(false);
        }
    }

    public void setSuddenDeathMode(Observable observable) {
        mode = ISLAND_SINK_MOVEMENT_MODE;
        suddenDeathObservable = observable;
        suddenDeathPlayerQueue = new LinkedList<>();
        pawns.get(currentNormalPawn).setPawnState(Pawn.NORMAL);
        for (int i = 0; i < gameState.getPlayers().size(); i++) {
            Player e = gameState.getPlayers().get(gameState.getPlayerOrder().get(i));
            if (gameState.getIslandTileState()[e.getTileX()][e.getTileY()] == GameState.SUNKEN_ISLAND_TILE) {
                suddenDeathPlayerQueue.add(i);
            }
        }
        if (!suddenDeathPlayerQueue.isEmpty()) {
            setSuddenDeathTiles(suddenDeathPlayerQueue.peek());
            currentSuddenDeathPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(suddenDeathPlayerQueue.peek()));
            setSuddenDeathTiles(suddenDeathPlayerQueue.peek());
            currentSuddenDeathPlayerPawn = suddenDeathPlayerQueue.peek();
            suddenDeathPlayerQueue.poll();
        }
        else {
            eraseSuddenDeathTiles();
            observable.onFinished();
        }
    }

    public void setSuddenDeathTiles(int player) {
        eraseMovementTiles();
        eraseShoreUpTiles();

        Pawn current = pawns.get(player);
        Player tempPlayer = gameState.getPlayers().get(gameState.getPlayerOrder().get(player));
        ArrayList<Pair> movableTiles = new ArrayList<>();
        movableTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()));
        movableTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()));
        movableTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()+1));
        movableTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()-1));
        if (tempPlayer.getAbility() == Player.PILOT && tempPlayer.isCanUseSpecialAction()) {
            movableTiles.addAll(islandTiles.keySet());
        }
        else if (tempPlayer.getAbility() == Player.DIVER) {
            TreeSet<Pair> currentTiles = new TreeSet<>();
            TreeSet<Pair> nextTiles = new TreeSet<>();
            currentTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()));
            currentTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()));
            currentTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()+1));
            currentTiles.add(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()-1));
            boolean changesMade = false;
            while (!currentTiles.isEmpty()) {
                for (Pair i : currentTiles) {
                    if (islandTiles.get(i) != null && gameState.getIslandTileState()[i.x][i.y] == GameState.NORMAL_ISLAND_TILE) {
                        nextTiles.add(i);
                    } else if (islandTiles.get(i) != null && (gameState.getIslandTileState()[i.x][i.y] == GameState.FLOODED_ISLAND_TILE ||
                            gameState.getIslandTileState()[i.x][i.y] == GameState.SUNKEN_ISLAND_TILE)) {
                        if (islandTiles.get(new Pair(i.x+1, i.y)) != null &&
                                islandTiles.get(new Pair(i.x+1, i.y)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x+1, i.y))) {
                                changesMade = true;
                            }
                        }
                        if (islandTiles.get(new Pair(i.x-1, i.y)) != null &&
                                islandTiles.get(new Pair(i.x-1, i.y)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x-1, i.y))) {
                                changesMade = true;
                            }
                        }
                        if (islandTiles.get(new Pair(i.x, i.y+1)) != null &&
                                islandTiles.get(new Pair(i.x, i.y+1)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x, i.y+1))) {
                                changesMade = true;
                            }
                        }
                        if (islandTiles.get(new Pair(i.x, i.y-1)) != null &&
                                islandTiles.get(new Pair(i.x, i.y-1)).getTileState() != GameState.INVALID) {
                            if (nextTiles.add(new Pair(i.x, i.y-1))) {
                                changesMade = true;
                            }
                        }
                        nextTiles.add(i);
                    }
                }
                if (changesMade) {
                    currentTiles = new TreeSet<>(nextTiles);
                    changesMade = false;
                } else {
                    break;
                }
            }
            nextTiles.remove(new Pair(tempPlayer.getTileX(), tempPlayer.getTileY()));
            TreeSet<Pair> temp = new TreeSet<>();
            temp.addAll(nextTiles);
            temp.addAll(movableTiles);
            movableTiles = new ArrayList<>(temp);
        }
        else if (tempPlayer.getAbility() == Player.EXPLORER) {
            movableTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()+1));
            movableTiles.add(new Pair(tempPlayer.getTileX()+1, tempPlayer.getTileY()-1));
            movableTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()+1));
            movableTiles.add(new Pair(tempPlayer.getTileX()-1, tempPlayer.getTileY()-1));
        }

        logger.info(movableTiles.toString());

        current.setPawnState(Pawn.MOVE);

        for (int i = 0; i < movableTiles.size(); i++) {
            Pair e = movableTiles.get(i);
            e.x = Math.max(e.x, 0);
            e.x = Math.min(e.x, 5);
            e.y = Math.max(e.y, 0);
            e.y = Math.min(e.y, 5);
            if (gameState.getIslandTileState()[e.x][e.y] == GameState.SUNKEN_ISLAND_TILE) {
                movableTiles.remove(i);
                i--;
            }
        }

        for (Pair e : movableTiles) {
            if (islandTiles.containsKey(e)) {
                islandTiles.get(e).setCanSuddenDeathMove(true);
            }
        }
    }

    public void eraseSuddenDeathTiles() {
        for (IslandTile e: islandTiles.values()) {
            e.setCanSuddenDeathMove(false);
        }
    }

    public boolean canCollectItems() {
        Player p = gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber()));
        IslandTile islandTile = islandTiles.get(new Pair(p.getTileX(), p.getTileY()));
        if (Resources.CollectTiles.containsKey(islandTile.getTileName())) {
            String artifactName = Resources.CollectTiles.get(islandTile.getTileName());
            int amount = 0;
            for (TreasureCard e : p.getCardsAtHand()) {
                if (e.getName().equals(artifactName)) {
                    amount++;
                }
            }
            if (amount >= 4) {
                return true;
            }
        }
        return false;
    }

    public void collectArtifact() {
        Player p = gameState.getPlayers().get(gameState.getPlayerOrder().get(gameState.getTurnNumber()));
        IslandTile islandTile = islandTiles.get(new Pair(p.getTileX(), p.getTileY()));
        if (Resources.CollectTiles.containsKey(islandTile.getTileName())) {
            String artifactName = Resources.CollectTiles.get(islandTile.getTileName());
            int amount = 0;
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < p.getCardsAtHand().size(); i++) {
                TreasureCard e = p.getCardsAtHand().get(i);
                if (e.getName().equals(artifactName)) {
                    amount++;
                    indexes.add(i);
                }
            }
            if (amount >= 4) {
                if (Resources.ArtifactNames[0].equals(artifactName)) {
                    gameState.getCollectedArtifacts()[0] = true;
                }
                else if (Resources.ArtifactNames[1].equals(artifactName)) {
                    gameState.getCollectedArtifacts()[1] = true;
                }
                else if (Resources.ArtifactNames[2].equals(artifactName)) {
                    gameState.getCollectedArtifacts()[1] = true;
                }
                else if (Resources.ArtifactNames[3].equals(artifactName)) {
                    gameState.getCollectedArtifacts()[3] = true;
                }
                for (int e : indexes) {
                    p.removeTreasureFromHand(e);
                }
            }
        }
    }
}