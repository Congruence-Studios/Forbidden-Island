package com.congruence.ui;

import com.congruence.state.GameState;
import com.congruence.state.Player;
import com.congruence.state.TreasureCard;

import java.util.*;

public class Game {

    /**
     * The GameState of the Application
     */
    private GameState gameState;

    public Game(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Move the Current Player to the Desired Coordinates
     *
     * @param x New X Coordinate
     * @param y New Y Coordinate
     */
    public void move(int x, int y) {
        gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).setTileX(x);
        gameState.getPlayers().get(gameState.getCurrentPlayerTurn()).setTileY(y);
    }

    /**
     * Sets the Next Players Turn in the GameState
     * <p>
     * - Sets the Actions Left for the Player to 3
     */
    public void setPlayerTurn() {
        if (gameState.getTurnNumber() >= gameState.getMaxTurnLoops()) {
            gameState.setTurnNumber(0);
            gameState.setCurrentPlayerTurn(gameState.getPlayerOrder().get(0));
        } else {
            gameState.setTurnNumber(gameState.getTurnNumber() + 1);
            gameState.setCurrentPlayerTurn(gameState.getPlayerOrder().get(gameState.getTurnNumber()));
        }
        gameState.setCurrentPlayerActionsLeft(3);
    }

    /**
     * Draws the IslandCardTiles
     */
    public void drawIslandTileCards() {
    }

    /**
     * Draws the Treasure Cards
     */
    public void drawTreasureCards() {
    }

    /**
     * Swap the Treasure Cards in the Current
     * Persons Hand with the Treasure Cards in another
     * person's hand
     *
     * @param pName The Name of the Player to Swap With
     */
    public void swapTreasureCards(String pName) {
    }

    /**
     * Uses a Sandbags on the Specified Tile
     *
     * @param x The X Coordinate of the Tile to Use the Sandbag On
     * @param y The Y Coordinate of the Tile to Use the Sandbag On
     */
    public void useSandbag(int x, int y) {
    }

    /**
     * Use a Helicopter Card to Lift the Players and Transport them
     * to another square
     *
     * @param x     The X Coordinate to Move To
     * @param y     The Y Coordinate to Move To
     * @param pName The Players to Move
     */
    public void useHelicopterCard(int x, int y, String... pName) {
    }

    /**
     * Use a Helicopter Card when on Fools Landing to Transport the
     * Players Out
     */
    public void useHelicopterCardOnFoolsLanding() {
        boolean hasHelicopterCard = false;
        boolean onFoolsLanding = true;
        HashSet<Player> set = (HashSet<Player>) gameState.getPlayers().values();
        String[][] arr = gameState.getIslandTiles();
        int foolX = -1;
        int foolY = -1;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j].equals("Fool's Landing")) {
                    foolX = j;
                    foolY = i;
                    break;
                }
            }
        }
        if (foolX < 0 || foolY < 0) {
            return;
        }
        for (Player p : set) {
            for (TreasureCard tc : p.getCardsAtHand()) {
                if (tc.getCardType() == TreasureCard.HELICOPTER_CARD) {
                    hasHelicopterCard = true;
                    break;
                }
            }
            if (p.getTileX() != foolX || p.getTileY() != foolY) {
                onFoolsLanding = false;
            }
        }
        if (onFoolsLanding && hasHelicopterCard) {
            gameState.setGameEnd(true);
            gameState.setGameResult(GameState.WIN);
        }
    }

    /**
     * Collect the Specified Artifact
     */
    public void collectArtifact(String aName) {
        Player p = gameState.getPlayers().get(gameState.getCurrentPlayerTurn());
        switch (aName) {
            case "Crystal of Fire" :
                gameState.getCollectedArtifacts()[GameState.CRYSTAL_OF_FIRE] = true;
                p.getTreasuresAtHand().add("Crystal of Fire");
            case "Earth Stone" :
                gameState.getCollectedArtifacts()[GameState.EARTH_STONE] = true;
                p.getTreasuresAtHand().add("Earth Stone");
            case "Statue of the Wind" :
                gameState.getCollectedArtifacts()[GameState.STATUE_OF_THE_WIND] = true;
                p.getTreasuresAtHand().add("Statue of the Wind");
            case "Ocean's Chalice" :
                gameState.getCollectedArtifacts()[GameState.OCEANS_CHALICE] = true;
                p.getTreasuresAtHand().add("Ocean's Chalice");
        }
    }

    /**
     * Flood the Specified Tile
     *
     * @param x The X Coordinate of the Tile
     * @param y The Y Coordinate of the Tile
     */
    public void floodTile(int x, int y) {
        int[][] arr = gameState.getIslandTileState();
        if (arr[y][x] == GameState.NORMAL_ISLAND_TILE) {
            arr[y][x] = GameState.FLOODED_ISLAND_TILE;
        }
    }

    /**
     * Sink the Tile at X and Y and Do the Necessary Actions
     *
     * @param x The X Coordinate
     * @param y The Y Coordinate
     */
    public void sinkTile(int x, int y) {
        int[][] arr = gameState.getIslandTileState();
        if (arr[y][x] == GameState.FLOODED_ISLAND_TILE) {
            arr[y][x] = GameState.SUNKEN_ISLAND_TILE;
            for (Player p : gameState.getPlayers().values()) {
                if (p.getTileX() == x && p.getTileY() == y) {
                    if (p.getAbility() == Player.DIVER) {
                        //Show diver's available moves
                    } else if (p.getAbility() == Player.PILOT) {
                        //Show pilot's available moves
                    } else if (p.getAbility() == Player.EXPLORER) {
                        //Show explorer's available moves
                    } else {
                        //Show available moves
                    }
                }
            }
        }
    }

    /**
     * Sink the Fools Landing Tile
     */
    public void sinkFoolsLanding() {
        gameState.setGameResult(GameState.FOOLS_LANDING_SUNK);
        gameState.setGameEnd(true);
    }

    public ArrayList<int[]> getDiverMoves(Player p) {
        assert p.getAbility() == Player.DIVER;
        int[][] tiles = gameState.getIslandTileState();
        boolean[][] visitedTiles = new boolean[tiles.length][tiles[0].length];
        HashSet<int[]> currentTiles = new HashSet<>();
        HashSet<int[]> nextTiles = new HashSet<>();
        currentTiles.add(new int[]{p.getTileY(), p.getTileX()});
        boolean changesMade = false;
        while (!currentTiles.isEmpty()) {
            for (int[] i : currentTiles) {
                visitedTiles[i[1]][i[0]] = true;
                if (i[1] + 1 < tiles.length && tiles[i[1] + 1][i[0]] != GameState.INVALID &&
                        !visitedTiles[i[1] + 1][i[0]] &&
                        tiles[i[1]][i[0]] != GameState.NORMAL_ISLAND_TILE) {
                    changesMade = true;
                    nextTiles.add(new int[]{i[0], i[1] + 1});
                } else if (i[1] - 1 >= 0 && tiles[i[1] - 1][i[0]] != GameState.INVALID &&
                        !visitedTiles[i[1] - 1][i[0]] &&
                        tiles[i[1]][i[0]] != GameState.NORMAL_ISLAND_TILE) {
                    changesMade = true;
                    nextTiles.add(new int[]{i[0], i[1] - 1});
                } else if (i[0] + 1 < tiles[0].length && tiles[i[1]][i[0] + 1] != GameState.INVALID &&
                        !visitedTiles[i[1]][i[0] + 1] &&
                        tiles[i[1]][i[0]] != GameState.NORMAL_ISLAND_TILE) {
                    changesMade = true;
                    nextTiles.add(new int[]{i[0] + 1, i[1]});
                } else if (i[0] - 1 >= 0 && tiles[i[1]][i[0] - 1] != GameState.INVALID &&
                        !visitedTiles[i[1]][i[0] - 1] &&
                        tiles[i[1]][i[0]] != GameState.NORMAL_ISLAND_TILE) {
                    changesMade = true;
                    nextTiles.add(new int[]{i[0] - 1, i[1]});
                } else if (tiles[i[1]][i[0]] != GameState.NORMAL_ISLAND_TILE) {
                    nextTiles.add(i);
                }
            }
            if (changesMade) {
                currentTiles = nextTiles;
                nextTiles.clear();
                changesMade = false;
            } else {
                break;
            }
        }
        return new ArrayList<>(nextTiles);
    }

    public ArrayList<int[]> getPilotMoves (Player p) {
        assert p.getAbility() == Player.PILOT;
        int[][] tiles = gameState.getIslandTileState();
        ArrayList<int[]> tilesToVisit = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == GameState.NORMAL_ISLAND_TILE && i != p.getTileY() && j != p.getTileX()) {
                    tilesToVisit.add(new int[]{i, j});
                }
            }
        }
        return tilesToVisit;
    }

    public ArrayList<int[]> getExplorerMoves (Player p) {
        assert p.getAbility() == Player.EXPLORER;
        int[][] tiles = gameState.getIslandTileState();
        ArrayList<int[]> tilesToVisit = getPlayerMoves(p);
        if (tiles[p.getTileY() + 1][p.getTileX() + 1] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY() + 1, p.getTileX() + 1});
        }
        if (tiles[p.getTileY() + 1][p.getTileX() - 1] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY() + 1, p.getTileX() - 1});
        }
        if (tiles[p.getTileY() - 1][p.getTileX() + 1] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY() - 1, p.getTileX() + 1});
        }
        if (tiles[p.getTileY() - 1][p.getTileX() - 1] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY() - 1, p.getTileX() - 1});
        }
        return tilesToVisit;
    }

    public ArrayList<int[]> getPlayerMoves (Player p) {
        int[][] tiles = gameState.getIslandTileState();
        ArrayList<int[]> tilesToVisit = new ArrayList<>();
        if (tiles[p.getTileY() + 1][p.getTileX()] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY() + 1, p.getTileX()});
        }
        if (tiles[p.getTileY() - 1][p.getTileX()] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY() - 1, p.getTileX()});
        }
        if (tiles[p.getTileY()][p.getTileX() + 1] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY(), p.getTileX() + 1});
        }
        if (tiles[p.getTileY()][p.getTileX() - 1] == GameState.NORMAL_ISLAND_TILE) {
            tilesToVisit.add(new int[]{p.getTileY(), p.getTileX() - 1});
        }
        return tilesToVisit;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameState, game.gameState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameState);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameState=" + gameState +
                '}';
    }
}
