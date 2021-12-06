package com.congruence.state;

import com.congruence.ui.Pair;
import com.congruence.ui.PlayerHand;
import com.congruence.ui.TreasureCardUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameState {
    private static final Logger logger = LoggerFactory.getLogger(GameState.class);

    public static final int NOVICE = 0;
    public static final int NORMAL = 1;
    public static final int ELITE = 2;
    public static final int LEGENDARY = 3;
    public static final int LOSS_WATER_HEIGHT = 10;
    public static final int INVALID = -1;
    public static final int NORMAL_ISLAND_TILE = 0;
    public static final int FLOODED_ISLAND_TILE = 1;
    public static final int SUNKEN_ISLAND_TILE = 2;
    public static final int CRYSTAL_OF_FIRE = 0;
    public static final int EARTH_STONE = 1;
    public static final int STATUE_OF_THE_WIND = 2;
    public static final int OCEANS_CHALICE = 3;
    public static final int WIN = 0;
    public static final int PLAYER_DROWNED = 1;
    public static final int FOOLS_LANDING_SUNK = 2;
    public static final int BOTH_TREASURE_TILES_SUNK = 3;
    public static final int WATER_METER_FULL = 4;
    /**
     * The Collected Artifacts of the Player
     */
    private final boolean[] CollectedArtifacts = new boolean[4];
    /**
     * The Owner of the Artifacts
     */
    private final String[] ArtifactOwner = new String[4];
    /**
     * The Difficulty of the Game
     * <p>
     * Sets the Initial Height of the Water Meter
     */
    private int difficulty;
    /**
     * The Current Height of the WaterMeter
     */
    private int waterHeight;
    /**
     * The Amount of Cards the Player Has To Draw
     * Based on their Water Level
     */
    private int cardsToDraw;
    /**
     * The Players of the Game
     * <p>
     * This Maps the Players to the Player Name
     */
    private Map<String, Player> players;
    /**
     * The Ordering of the Players Turns in the
     * Game
     * <p>
     * Each Turn in the Map Maps to the Player Name
     * <p>
     * The Turns start from 0 to n-1
     * where n is the number of players
     */
    private Map<Integer, String> playerOrder;
    /**
     * The Resource Name of the Buffers that
     * map to the images of the Island Tiles
     */
    private String[][] islandTiles;
    /**
     * The State of the Individual Island Tiles
     * <p>
     * States whether the tile is normal, flooded,
     * or sunken
     */
    private int[][] islandTileState;
    /**
     * The StateListeners of the GameState
     * <p>
     * These Watch for StateChanges in the Data
     */
    private List<StateListener> stateListeners;
    /**
     * The Turn that the Game is Currently On
     */
    private int turnNumber;
    /**
     * The Amount of Turns that the Game has Played
     * so far
     */
    private int totalTurns;
    /**
     * The Max Amount of Turns in a Turn Loop
     * <p>
     * For Example: R -> G -> Y then back to R has 3 maxTurnLoops
     */
    private int maxTurnLoops;
    /**
     * Which Players Turn is it Currently
     * <p>
     * Maps to the Player Name
     */
    private String currentPlayerTurn;
    /**
     * The Amount of Actions left for the Current
     * Player that is Playing
     * <p>
     * There can only be 3 Actions Left at the Max
     */
    private int currentPlayerActionsLeft;
    /**
     * The Deck of the Island Tile Cards
     * <p>
     * This is a Stack-Like Deck Structure
     */
    private Stack<FloodCard> islandTileDeck;
    /**
     * The Discard Pile for the Island Tile Cards
     */
    private ArrayList<FloodCard> islandTileDiscardDeck;
    /**
     * The Deck of Treasure Cards
     * <p>
     * This is a Stack-Like Deck Structure
     */
    private Stack<TreasureCard> treasureCardDeck;
    /**
     * The Discard Pile for the Treasure Cards
     */
    private ArrayList<TreasureCard> treasureCardDiscardDeck;
    /**
     * Should the Game Show the UI for Drawing Treasure Cards
     */
    private boolean drawingTreasureCards = false;
    /**
     * The Current Cards that Have Been Drawn from the TreasureDeck
     */
    private ArrayList<TreasureCard> currentDrawnTreasureCards = null;
    /**
     * Should the Game Show the UI for Drawing the IslandCards
     */
    private boolean drawingIslandTileCards = false;
    /**
     * The Current Cards that Have Been Drawn from the IslandTileDeck
     */
    private ArrayList<FloodCard> currentDrawnIslandTileCards = null;
    /**
     * The Amount of Island Cards that Have Been Drawn
     */
    private int amountOfCurrentDrawnIslandTileCards;
    /*
     * Whether the game has ended
     */
    private boolean gameEnd = false;
    /*
     * What the outcome of the game is (win, drown,
     *  fools landing sunk, both treasure tiles sunk, flood meter full)
     */
    private int gameResult;

    private Pair foolsLandingCoordinates;

    private ArrayList<Pair> fireSpacesLeft;

    private ArrayList<Pair> earthSpacesLeft;

    private ArrayList<Pair> oceanSpacesLeft;

    private ArrayList<Pair> windSpacesLeft;

    private TreasureCardUI treasureCardUI;

    public GameState(
            int difficulty,
            Map<String, Player> players,
            Map<Integer, String> playerOrder,
            String[][] islandTiles,
            int[][] islandTileState,
            Stack<FloodCard> islandTileDeck,
            Stack<TreasureCard> treasureCardDeck,
            int waterHeight
            ) {
        this.difficulty = difficulty;
        this.waterHeight = waterHeight;
        this.cardsToDraw = waterHeight / 2 +1;
        this.players = players;
        this.playerOrder = playerOrder;
        this.islandTiles = islandTiles;
        this.islandTileState = islandTileState;
        this.stateListeners = stateListeners;
        this.turnNumber = 0;
        this.totalTurns = 0;
        this.maxTurnLoops = players.size();
        this.currentPlayerTurn = playerOrder.get(0);
        this.currentPlayerActionsLeft = 3;
        this.islandTileDeck = islandTileDeck;
        this.islandTileDiscardDeck = new ArrayList<>();
        this.treasureCardDeck = treasureCardDeck;
        this.treasureCardDiscardDeck = new ArrayList<>();
        this.drawingTreasureCards = false;
        this.currentDrawnTreasureCards = currentDrawnTreasureCards;
        this.drawingIslandTileCards = drawingIslandTileCards;
        this.currentDrawnIslandTileCards = currentDrawnIslandTileCards;
        this.amountOfCurrentDrawnIslandTileCards = amountOfCurrentDrawnIslandTileCards;
        for (int x = 0; x < islandTiles.length; x++) {
            for (int y = 0; y < islandTiles[x].length; y++) {
                logger.info("x: " + x + " y: " + y);
                if (islandTiles[x][y] != null && islandTiles[x][y].equals("Fools Landing")) {
                    foolsLandingCoordinates = new Pair(x, y);
                    logger.info("found: x: " + x + " y: " + y);
                } else if (islandTiles[x][y] != null && (islandTiles[x][y].equals("Cave of Embers") ||
                    islandTiles[x][y].equals("Cave of Shadows"))) {
                    fireSpacesLeft.add(new Pair(x, y));
                    logger.info("fire: x: " + x + " y: " + y);
                } else if (islandTiles[x][y] != null && (islandTiles[x][y].equals("Temple of the Moon") ||
                        islandTiles[x][y].equals("Temple of the Sun"))) {
                    earthSpacesLeft.add(new Pair(x, y));
                    logger.info("earth: x: " + x + " y: " + y);
                } else if (islandTiles[x][y] != null && (islandTiles[x][y].equals("Coral Palace") ||
                        islandTiles[x][y].equals("Tidal Palace"))) {
                    oceanSpacesLeft.add(new Pair(x, y));
                    logger.info("ocean: x: " + x + " y: " + y);
                } else if (islandTiles[x][y] != null && (islandTiles[x][y].equals("Howling Garden") ||
                        islandTiles[x][y].equals("Whispering Garden"))) {
                    windSpacesLeft.add(new Pair(x, y));
                    logger.info("wind: x: " + x + " y: " + y);
                }
            }
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getWaterHeight() {
        return waterHeight;
    }

    public void setWaterHeight(int waterHeight) {
        this.waterHeight = waterHeight;
        setCardsToDraw(waterHeight/2+1);
    }

    public int getCardsToDraw() {
        return cardsToDraw;
    }

    public void setCardsToDraw(int cardsToDraw) {
        this.cardsToDraw = cardsToDraw;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public Map<Integer, String> getPlayerOrder() {
        return playerOrder;
    }

    public void setPlayerOrder(Map<Integer, String> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public String[][] getIslandTiles() {
        return islandTiles;
    }

    public void setIslandTiles(String[][] islandTiles) {
        this.islandTiles = islandTiles;
    }

    public int[][] getIslandTileState() {
        return islandTileState;
    }

    public void setIslandTileState(int[][] islandTileState) {
        this.islandTileState = islandTileState;
    }

    public List<StateListener> getStateListeners() {
        return stateListeners;
    }

    public void setStateListeners(List<StateListener> stateListeners) {
        this.stateListeners = stateListeners;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public void setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
    }

    public int getMaxTurnLoops() {
        return maxTurnLoops;
    }

    public void setMaxTurnLoops(int maxTurnLoops) {
        this.maxTurnLoops = maxTurnLoops;
    }

    public String getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public void setCurrentPlayerTurn(String currentPlayerTurn) {
        this.currentPlayerTurn = currentPlayerTurn;
    }

    public int getCurrentPlayerActionsLeft() {
        return currentPlayerActionsLeft;
    }

    public void setCurrentPlayerActionsLeft(int currentPlayerActionsLeft) {
        this.currentPlayerActionsLeft = currentPlayerActionsLeft;
    }

    public Stack<FloodCard> getIslandTileDeck() {
        return islandTileDeck;
    }

    public void setIslandTileDeck(Stack<FloodCard> islandTileDeck) {
        this.islandTileDeck = islandTileDeck;
    }

    public ArrayList<FloodCard> getIslandTileDiscardDeck() {
        return islandTileDiscardDeck;
    }

    public void setIslandTileDiscardDeck(ArrayList<FloodCard> islandTileDiscardDeck) {
        this.islandTileDiscardDeck = islandTileDiscardDeck;
    }

    public Stack<TreasureCard> getTreasureCardDeck() {
        return treasureCardDeck;
    }

    public void setTreasureCardDeck(Stack<TreasureCard> treasureCardDeck) {
        this.treasureCardDeck = treasureCardDeck;
    }

    public ArrayList<TreasureCard> getTreasureCardDiscardDeck() {
        return treasureCardDiscardDeck;
    }

    public void setTreasureCardDiscardDeck(ArrayList<TreasureCard> treasureCardDiscardDeck) {
        this.treasureCardDiscardDeck = treasureCardDiscardDeck;
    }

    public boolean isDrawingTreasureCards() {
        return drawingTreasureCards;
    }

    public void setDrawingTreasureCards(boolean drawingTreasureCards) {
        this.drawingTreasureCards = drawingTreasureCards;
    }

    public ArrayList<TreasureCard> getCurrentDrawnTreasureCards() {
        return currentDrawnTreasureCards;
    }

    public void setCurrentDrawnTreasureCards(ArrayList<TreasureCard> currentDrawnTreasureCards) {
        this.currentDrawnTreasureCards = currentDrawnTreasureCards;
    }

    public boolean isDrawingIslandTileCards() {
        return drawingIslandTileCards;
    }

    public void setDrawingIslandTileCards(boolean drawingIslandTileCards) {
        this.drawingIslandTileCards = drawingIslandTileCards;
    }

    public ArrayList<FloodCard> getCurrentDrawnIslandTileCards() {
        return currentDrawnIslandTileCards;
    }

    public void setCurrentDrawnIslandTileCards(ArrayList<FloodCard> currentDrawnIslandTileCards) {
        this.currentDrawnIslandTileCards = currentDrawnIslandTileCards;
    }

    public int getAmountOfCurrentDrawnIslandTileCards() {
        return amountOfCurrentDrawnIslandTileCards;
    }

    public void setAmountOfCurrentDrawnIslandTileCards(int amountOfCurrentDrawnIslandTileCards) {
        this.amountOfCurrentDrawnIslandTileCards = amountOfCurrentDrawnIslandTileCards;
    }

    public boolean[] getCollectedArtifacts() {
        return CollectedArtifacts;
    }

    public String[] getArtifactOwner() {
        return ArtifactOwner;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }

    public int getGameResult() {
        return gameResult;
    }

    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }

    public Pair getFoolsLandingCoordinates() {
        return foolsLandingCoordinates;
    }

    public void setFoolsLandingCoordinates(Pair foolsLandingCoordinates) {
        this.foolsLandingCoordinates = foolsLandingCoordinates;
    }

    public TreasureCardUI getTreasureCardUI() {
        return treasureCardUI;
    }

    public void setTreasureCardUI(TreasureCardUI treasureCardUI) {
        this.treasureCardUI = treasureCardUI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return difficulty == gameState.difficulty && waterHeight == gameState.waterHeight && cardsToDraw == gameState.cardsToDraw && turnNumber == gameState.turnNumber && totalTurns == gameState.totalTurns && maxTurnLoops == gameState.maxTurnLoops && currentPlayerActionsLeft == gameState.currentPlayerActionsLeft && drawingTreasureCards == gameState.drawingTreasureCards && drawingIslandTileCards == gameState.drawingIslandTileCards && amountOfCurrentDrawnIslandTileCards == gameState.amountOfCurrentDrawnIslandTileCards && Arrays.equals(CollectedArtifacts, gameState.CollectedArtifacts) && Arrays.equals(ArtifactOwner, gameState.ArtifactOwner) && Objects.equals(players, gameState.players) && Objects.equals(playerOrder, gameState.playerOrder) && Arrays.deepEquals(islandTiles, gameState.islandTiles) && Arrays.deepEquals(islandTileState, gameState.islandTileState) && Objects.equals(stateListeners, gameState.stateListeners) && Objects.equals(currentPlayerTurn, gameState.currentPlayerTurn) && Objects.equals(islandTileDeck, gameState.islandTileDeck) && Objects.equals(islandTileDiscardDeck, gameState.islandTileDiscardDeck) && Objects.equals(treasureCardDeck, gameState.treasureCardDeck) && Objects.equals(treasureCardDiscardDeck, gameState.treasureCardDiscardDeck) && Objects.equals(currentDrawnTreasureCards, gameState.currentDrawnTreasureCards) && Objects.equals(currentDrawnIslandTileCards, gameState.currentDrawnIslandTileCards);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(difficulty, waterHeight, cardsToDraw, players, playerOrder, stateListeners, turnNumber, totalTurns, maxTurnLoops, currentPlayerTurn, currentPlayerActionsLeft, islandTileDeck, islandTileDiscardDeck, treasureCardDeck, treasureCardDiscardDeck, drawingTreasureCards, currentDrawnTreasureCards, drawingIslandTileCards, currentDrawnIslandTileCards, amountOfCurrentDrawnIslandTileCards);
        result = 31 * result + Arrays.hashCode(CollectedArtifacts);
        result = 31 * result + Arrays.hashCode(ArtifactOwner);
        result = 31 * result + Arrays.deepHashCode(islandTiles);
        result = 31 * result + Arrays.deepHashCode(islandTileState);
        return result;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "CollectedArtifacts=" + Arrays.toString(CollectedArtifacts) +
                ", ArtifactOwner=" + Arrays.toString(ArtifactOwner) +
                ", difficulty=" + difficulty +
                ", waterHeight=" + waterHeight +
                ", cardsToDraw=" + cardsToDraw +
                ", players=" + players +
                ", playerOrder=" + playerOrder +
                ", islandTiles=" + Arrays.toString(islandTiles) +
                ", islandTileState=" + Arrays.toString(islandTileState) +
                ", stateListeners=" + stateListeners +
                ", turnNumber=" + turnNumber +
                ", totalTurns=" + totalTurns +
                ", maxTurnLoops=" + maxTurnLoops +
                ", currentPlayerTurn='" + currentPlayerTurn + '\'' +
                ", currentPlayerActionsLeft=" + currentPlayerActionsLeft +
                ", islandTileDeck=" + islandTileDeck +
                ", islandTileDiscardDeck=" + islandTileDiscardDeck +
                ", treasureCardDeck=" + treasureCardDeck +
                ", treasureCardDiscardDeck=" + treasureCardDiscardDeck +
                ", drawingTreasureCards=" + drawingTreasureCards +
                ", currentDrawnTreasureCards=" + currentDrawnTreasureCards +
                ", drawingIslandTileCards=" + drawingIslandTileCards +
                ", currentDrawnIslandTileCards=" + currentDrawnIslandTileCards +
                ", amountOfCurrentDrawnIslandTileCards=" + amountOfCurrentDrawnIslandTileCards +
                '}';
    }

}
