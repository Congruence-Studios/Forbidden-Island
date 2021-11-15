package com.congruence.state;

import java.util.Objects;

public class TreasureCard {

    public static final int TREASURE_CARD = 0;
    public static final int SANDBAG_CARD = 1;
    public static final int HELICOPTER_CARD = 2;
    /**
     * The Name of the Treasure Card
     */
    private String name;
    /**
     * The Name of the Resource
     */
    private String rName;
    /**
     * The Type of Resource Card
     */
    private int cardType;

    public TreasureCard(String name, String rName, int cardType) {
        this.name = name;
        this.rName = rName;
        this.cardType = cardType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreasureCard that = (TreasureCard) o;
        return cardType == that.cardType && Objects.equals(name, that.name) && Objects.equals(rName, that.rName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rName, cardType);
    }

    @Override
    public String toString() {
        return "TreasureCard{" +
                "name='" + name + '\'' +
                ", rName='" + rName + '\'' +
                ", cardType=" + cardType +
                '}';
    }
}
