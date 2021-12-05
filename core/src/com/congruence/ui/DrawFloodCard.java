package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import com.congruence.state.FloodCard;
import com.congruence.state.GameState;
import com.congruence.state.TreasureCard;

public class DrawFloodCard extends Actor {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture Background;

    private Texture BaseCard;

    public DrawFloodCard(
            float positionX,
            float positionY,
            float height,
            float width,
            GameState state
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;
        this.state = state;

        super.setBounds(0, 0, 0, 0);

        Background = ForbiddenIsland.assetManager.get("custom-ui/draw-screen/Draw Treasure Cards Background.png", Texture.class);
        BaseCard = ForbiddenIsland.assetManager.get("treasure-deck/Crystal of Fire.png", Texture.class);

        Background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        BaseCard.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            batch.draw(Background, getPositionX(), getPositiveY(), getWidth(), getHeight());
            float x = positionX+(getWidth()/2)-5-BaseCard.getWidth();
            float y = positiveY+(getHeight()/2)-BaseCard.getHeight()/2f;
            if (state.getCurrentDrawnIslandTileCards() != null) {
                for (FloodCard e : state.getCurrentDrawnIslandTileCards()) {
                    //Texture texture = ForbiddenIsland.assetManager.get(e.getName());
                    batch.draw(BaseCard, x, y);
                    x += BaseCard.getWidth()+10;
                }
            }
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        if (isOpen) {
            super.setBounds(positionX, positiveY, width, height);
        }
        else {
            super.setBounds(0, 0, 0, 0);
        }
    }
}
