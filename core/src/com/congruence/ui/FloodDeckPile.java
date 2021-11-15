package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class FloodDeckPile extends Actor {

    private boolean hovered;

    private boolean focused;

    private float positionX;

    private float positionY;

    private float floodDeckWidth;

    private float floodDeckHeight;

    private Texture floodDeckTexture;

    private Texture hoveredFloodDeckTreasure;

    public FloodDeckPile(
            float positionX,
            float positionY,
            float width,
            float height
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.floodDeckWidth = width;
        this.floodDeckHeight = height;
        floodDeckTexture = new Texture(Gdx.files.internal("./flood-deck/Flood-Deck.png"));
        hoveredFloodDeckTreasure = new Texture(Gdx.files.internal("./flood-deck/Flood-Deck-Hovered.png"));
        floodDeckTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoveredFloodDeckTreasure.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        super.setBounds(this.positionX, this.positionY, this.floodDeckWidth, this.floodDeckHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (focused) {
            batch.draw(floodDeckTexture, positionX, positionY, floodDeckHeight * 8f/5f, floodDeckHeight);
        }
        else if (hovered) {
            batch.draw(hoveredFloodDeckTreasure, positionX, positionY, floodDeckHeight * 8f/5f, floodDeckHeight);
        }
        else {
            batch.draw(floodDeckTexture, positionX, positionY, floodDeckHeight * 8f/5f, floodDeckHeight);
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(this.positionX, this.positionY, floodDeckHeight * 8f/5f, this.floodDeckHeight);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        super.setBounds(this.positionX, this.positionY, floodDeckHeight * 8f/5f, this.floodDeckHeight);
    }

    public float getFloodDeckWidth() {
        return floodDeckWidth;
    }

    public void setFloodDeckWidth(float floodDeckWidth) {
        this.floodDeckWidth = floodDeckWidth;
        super.setBounds(this.positionX, this.positionY, floodDeckHeight * 8f/5f, this.floodDeckHeight);
    }

    public float getFloodDeckHeight() {
        return floodDeckHeight;
    }

    public void setFloodDeckHeight(float floodDeckHeight) {
        this.floodDeckHeight = floodDeckHeight;
        super.setBounds(this.positionX, this.positionY,floodDeckHeight * 8f/5f, this.floodDeckHeight);
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
