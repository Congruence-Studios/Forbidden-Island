package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import com.congruence.state.FloodCard;
import com.congruence.state.GameState;
import com.congruence.util.Observable;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoredUpDialog extends Actor {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture Background;

    private ArrayList<Texture> textures;

    private ArrayList<String> shoredUpTiles = null;

    public ShoredUpDialog(
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

        Background = ForbiddenIsland.assetManager.get("custom-ui/draw-screen/Shore Up Background.png", Texture.class);

        Background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen && textures != null) {
            batch.draw(Background, getPositionX(), getPositiveY(), getWidth(), getHeight());
            float side = Math.min(height * 1/2f, textures.get(0).getWidth());
            float x = positionX+(getWidth()/2)-(textures.size() > 1 ? 5 : 0)-(side * textures.size() * 0.5f);
            float y = positiveY+(getHeight()/2)-side/2f;
            if (textures != null) {
                for (Texture e : textures) {
                    batch.draw(e, x, y, side, side);
                    x += textures.get(0).getWidth()+10;
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

    public void setOpen(boolean open, ArrayList<String> shoredUpTiles) {
        isOpen = open;
        if (isOpen) {
            super.setBounds(positionX, positiveY, width, height);
            textures = new ArrayList<>();
            for (String e : shoredUpTiles) {
                Texture t = ForbiddenIsland.assetManager.get("island-tiles/" + e + ".png");
                t.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
                textures.add(t);
            }
        }
        else {
            super.setBounds(0, 0, 0, 0);
        }
    }
}
