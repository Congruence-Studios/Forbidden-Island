package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.state.GameState;
import com.congruence.state.Player;

public class PlayerHand extends Actor {

    private float positionX;

    private float positionY;

    private float height;

    private float width;

    private Player player;

    private boolean hovered;

    private boolean focused;

    private boolean enabled;

    private Texture outlinedButtonTexture;

    private Texture hoverButtonTexture;

    private Texture focusedButtonTexture;

    private boolean[] collectedTreasures;

    public PlayerHand(
            float positionX,
            float positionY,
            Player player
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.player = player;
        outlinedButtonTexture = new Texture(Gdx.files.internal("./custom-ui/player-hand/Player-Hand-Button.png"));
        hoverButtonTexture = new Texture(Gdx.files.internal("./custom-ui/player-hand/Player-Hand-Button-Hovered.png"));
        focusedButtonTexture = new Texture(Gdx.files.internal("./custom-ui/player-hand/Player-Hand-Button-Pressed.png"));
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        super.setBounds(this.positionX, this.positionY, this.width, this.height);
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
