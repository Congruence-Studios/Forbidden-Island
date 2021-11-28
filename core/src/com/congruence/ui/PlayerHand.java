package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    private boolean enabled;

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
        outlinedButtonTexture = new Texture(Gdx.files.internal("./custom-ui/player-hand/Player-Hand-Button.png"));
        hoverButtonTexture = new Texture(Gdx.files.internal("./custom-ui/player-hand/Player-Hand-Button-Hovered.png"));
        focusedButtonTexture = new Texture(Gdx.files.internal("./custom-ui/player-hand/Player-Hand-Button-Pressed.png"));
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        super.setBounds(this.positionX, this.positionY, this.width, this.height);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                logger.info("touchDown: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                focused = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                focused = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                logger.info("enter: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                hovered = true;
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
