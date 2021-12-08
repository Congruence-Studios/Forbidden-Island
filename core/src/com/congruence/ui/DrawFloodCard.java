package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import com.congruence.state.FloodCard;
import com.congruence.state.GameState;
import com.congruence.state.TreasureCard;
import com.congruence.util.Observable;

import java.util.HashMap;

public class DrawFloodCard extends Actor {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture Background;

    private HashMap<String, Texture> textures;

    private Observable observable = ()->{};

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

        Background = ForbiddenIsland.assetManager.get("custom-ui/draw-screen/Draw Flood Cards Background.png", Texture.class);

        Background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        textures = new HashMap<>();
        for (String e : ForbiddenIsland.islandTilesUsed) {
            Texture t = ForbiddenIsland.assetManager.get("flood-deck/Flood_Card_" + e + "@2x.png");
            t.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
            textures.put( e, t);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            batch.draw(Background, getPositionX(), getPositiveY(), getWidth(), getHeight());
            float x = positionX+(getWidth()/2)-(state.getCurrentDrawnIslandTileCards().size() > 1 ? 5 : 0)-(textures.get(ForbiddenIsland.islandTilesUsed.get(0)).getWidth() * state.getCurrentDrawnIslandTileCards().size() * 0.5f);
            float y = positiveY+(getHeight()/2)-textures.get(ForbiddenIsland.islandTilesUsed.get(0)).getHeight()/2f;
            if (state.getCurrentDrawnIslandTileCards() != null) {
                for (FloodCard e : state.getCurrentDrawnIslandTileCards()) {
                    batch.draw(textures.get(e.getName()), x, y);
                    x += textures.get(ForbiddenIsland.islandTilesUsed.get(0)).getWidth()+10;
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

    public void setOpen(boolean open, Observable observable) {
        isOpen = open;
        this.observable = observable;
        if (isOpen) {
            super.setBounds(positionX, positiveY, width, height);
        }
        else {
            super.setBounds(0, 0, 0, 0);
        }
    }

    public Observable getObservable() {
        return observable;
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }
}
