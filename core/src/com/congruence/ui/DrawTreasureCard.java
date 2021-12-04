package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import com.congruence.state.GameState;
import com.congruence.state.TreasureCard;

public class DrawTreasureCard extends Actor {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture Background;

    private Texture COFTexture;

    private Texture SOTWTexture;

    private Texture OCTexture;

    private Texture ESTexture;

    private Texture SandbagCardTexture;

    private Texture HelicopterCardTexture;

    private Texture WatersRiseTexture;

    public DrawTreasureCard(
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
        COFTexture = ForbiddenIsland.assetManager.get("treasure-deck/Crystal of Fire.png", Texture.class);
        SOTWTexture = ForbiddenIsland.assetManager.get("treasure-deck/Statue of the Wind.png", Texture.class);
        OCTexture = ForbiddenIsland.assetManager.get("treasure-deck/Oceans Chalice.png", Texture.class);
        ESTexture = ForbiddenIsland.assetManager.get("treasure-deck/Earth Stone.png", Texture.class);
        SandbagCardTexture = ForbiddenIsland.assetManager.get("treasure-deck/Sandbag.png", Texture.class);
        HelicopterCardTexture = ForbiddenIsland.assetManager.get("treasure-deck/Helicopter.png", Texture.class);
        WatersRiseTexture = ForbiddenIsland.assetManager.get("treasure-deck/Waters Rise.png", Texture.class);

        Background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        COFTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        SOTWTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        OCTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        ESTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        SandbagCardTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        HelicopterCardTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WatersRiseTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            if (state.getCurrentDrawnTreasureCards() != null) {
                for (TreasureCard e : state.getCurrentDrawnTreasureCards()) {
                    if (e.getCardType() == TreasureCard.HELICOPTER_CARD) {
                        batch.draw(HelicopterCardTexture, 0, 0);
                    }
                    else if (e.getCardType() == TreasureCard.SANDBAG_CARD) {
                        batch.draw(SandbagCardTexture, 0, 0);
                    }
                    else if (e.getCardType() == TreasureCard.WATERS_RISE_CARD) {
                        batch.draw(SandbagCardTexture, 0, 0);
                    }
                    else if (e.getName().equals("Ocean's Chalice")) {
                        batch.draw(SandbagCardTexture, 0, 0);
                    }
                    else if (e.getName().equals("Statue of the Wind")) {
                        batch.draw(SandbagCardTexture, 0, 0);
                    }
                    else if (e.getName().equals("Earth Stone")) {
                        batch.draw(SandbagCardTexture, 0, 0);
                    }
                    else if (e.getName().equals("Crystal of Fire")) {
                        batch.draw(SandbagCardTexture, 0, 0);
                    }
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
