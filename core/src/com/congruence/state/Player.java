package com.congruence.state;

import java.util.ArrayList;
import java.util.Objects;

public class Player {

    public static final int PILOT = 0;
    public static final int ENGINEER = 1;
    public static final int MESSENGER = 2;
    public static final int EXPLORER = 3;
    public static final int DIVER = 4;
    public static final int NAVIGATOR = 5;
    /**
     * The Name of the Player
     */
    private String playerName;
    /**
     * The Name of the Resource
     */
    private String rName;
    /**
     * An ArrayList Containing the Cards at Hand
     */
    private ArrayList<TreasureCard> cardsAtHand;
    /*
     * An ArrayList containing the Treasures at Hand
     */
    private ArrayList<String> treasuresAtHand;
    /**
     * The X Coordinate of the Tile
     */
    private int tileX;
    /**
     * The Y Coordinate of the Tile
     */
    private int tileY;
    /**
     * The Ability of the Player
     */
    private int ability;

    private int tilePosition;

    private boolean canUsePilotSpecial;

    public Player(String playerName, String rName, ArrayList<TreasureCard> cardsAtHand,
                  int tileX, int tileY) {
        this.playerName = playerName;
        this.rName = rName;
        this.cardsAtHand = cardsAtHand;
        this.tileX = tileX;
        this.tileY = tileY;
        this.tilePosition = 0;
        this.canUsePilotSpecial = true;

        if (playerName.equals("Pilot")) {
            ability = Player.PILOT;
        }
        else if (playerName.equals("Engineer")) {
            ability = Player.ENGINEER;
        }
        else if (playerName.equals("Messenger")) {
            ability = Player.MESSENGER;
        }
        else if (playerName.equals("Explorer")) {
            ability = Player.EXPLORER;
        }
        else if (playerName.equals("Diver")) {
            ability = Player.DIVER;
        }
        else if (playerName.equals("Navigator")) {
            ability = Player.NAVIGATOR;
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public ArrayList<TreasureCard> getCardsAtHand() {
        return cardsAtHand;
    }

    public void setCardsAtHand(ArrayList<TreasureCard> cardsAtHand) {
        this.cardsAtHand = cardsAtHand;
    }

    public ArrayList<String> getTreasuresAtHand() {
        return treasuresAtHand;
    }

    public void setTreasuresAtHand(ArrayList<String> treasuresAtHand) {
        this.treasuresAtHand = treasuresAtHand;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getAbility() {
        return ability;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }

    public int getTilePosition() {
        return tilePosition;
    }

    public void setTilePosition(int tilePosition) {
        this.tilePosition = tilePosition;
    }

    public boolean isCanUsePilotSpecial() {
        return canUsePilotSpecial;
    }

    public void setCanUsePilotSpecial(boolean canUsePilotSpecial) {
        this.canUsePilotSpecial = canUsePilotSpecial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return tileX == player.tileX && tileY == player.tileY && ability == player.ability && Objects.equals(playerName, player.playerName) && Objects.equals(rName, player.rName) && Objects.equals(cardsAtHand, player.cardsAtHand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, rName, cardsAtHand, tileX, tileY, ability);
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", rName='" + rName + '\'' +
                ", cardsAtHand=" + cardsAtHand +
                ", tileX=" + tileX +
                ", tileY=" + tileY +
                ", ability=" + ability +
                '}';
    }
}
