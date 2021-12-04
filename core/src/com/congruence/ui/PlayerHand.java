package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.state.GameState;
import com.congruence.state.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHand extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(PlayerHand.class);

    private float positionX;

    private float positionY;

    private float height;

    private float width;

    private Player player;

    private boolean hovered;

    private boolean focused;

    private Texture outlinedButtonTexture;

    private Texture hoverButtonTexture;

    private Texture focusedButtonTexture;

    private boolean[] collectedTreasures;

    public PlayerHand(
            float positionX,
            float positionY,
            float width,
            float height,
            Player player
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.player = player;
        this.width = width;
        this.height = height;
        outlinedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/player-hand/Player Hand Button.png", Texture.class);
        hoverButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/player-hand/Player Hand Button Hovered.png", Texture.class);
        focusedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/player-hand/Player Hand Button Pressed.png", Texture.class);
        outlinedButtonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(this.positionX, this.positionY, this.width, this.height);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //logger.info("X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //logger.info("touchDown: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //focused = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                focused = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //logger.info("enter: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //hovered = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hovered = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (hovered) {
            batch.draw(hoverButtonTexture, positionX, positionY, width, height);
        }
        else if (focused) {
            batch.draw(focusedButtonTexture, positionX, positionY, width, height);
        } else {
            batch.draw(outlinedButtonTexture, positionX, positionY, width, height);
        }
    }


    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
