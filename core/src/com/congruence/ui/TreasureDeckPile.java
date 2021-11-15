package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TreasureDeckPile extends Actor {

    private boolean hovered;

    private boolean focused;

    private float positionX;

    private float positionY;

    private float treasureDeckWidth;

    private float treasureDeckHeight;

    private Texture treasureDeckTexture;

    private Texture hoveredTreasureDeckTreasure;

    public TreasureDeckPile(
            float positionX,
            float positionY,
            float width,
            float height
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.treasureDeckWidth = width;
        this.treasureDeckHeight = height;
        treasureDeckTexture = new Texture(Gdx.files.internal("./treasure-deck/Treasure-Deck.png"));
        hoveredTreasureDeckTreasure = new Texture(Gdx.files.internal("./treasure-deck/Treasure-Deck-Hovered.png"));
        super.setBounds(this.positionX, this.positionY, this.treasureDeckWidth, this.treasureDeckHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (focused) {
            batch.draw(treasureDeckTexture, positionX, positionY, treasureDeckHeight * 8f/5f, treasureDeckHeight);
        }
        else if (hovered) {
            batch.draw(hoveredTreasureDeckTreasure, positionX, positionY, treasureDeckHeight * 8f/5f, treasureDeckHeight);
        }
        else {
            batch.draw(treasureDeckTexture, positionX, positionY, treasureDeckHeight * 8f/5f, treasureDeckHeight);
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(this.positionX, this.positionY, treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        super.setBounds(this.positionX, this.positionY, treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public float getTreasureDeckWidth() {
        return treasureDeckWidth;
    }

    public void setTreasureDeckWidth(float treasureDeckWidth) {
        this.treasureDeckWidth = treasureDeckWidth;
        super.setBounds(this.positionX, this.positionY, treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public float getTreasureDeckHeight() {
        return treasureDeckHeight;
    }

    public void setTreasureDeckHeight(float treasureDeckHeight) {
        this.treasureDeckHeight = treasureDeckHeight;
        super.setBounds(this.positionX, this.positionY,treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

}
