package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameMenuShoreUpButton extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(GameMenuShoreUpButton.class);

    private boolean hover;

    private boolean focused;

    private boolean enabled;

    private Texture outlinedButtonTexture;

    private Texture hoverButtonTexture;

    private Texture focusedButtonTexture;

    private Texture disabledButtonTexture;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    public GameMenuShoreUpButton(
            float positionX,
            float positionY,
            float height,
            float width
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;

        outlinedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/shore-up/Shore-Up-Button.png", Texture.class);
        hoverButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/shore-up/Shore-Up-Button-Hovered.png", Texture.class);
        focusedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/shore-up/Shore-Up-Button-Pressed.png", Texture.class);
        disabledButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/shore-up/Shore-Up-Button-Disabled.png", Texture.class);
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        super.setBounds(positionX + 10f, positiveY + 10f, outlinedButtonTexture.getWidth(), hoverButtonTexture.getHeight());
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
                hover = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hover = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (!enabled) {
            batch.draw(disabledButtonTexture, positionX + 10f, positiveY + 10f,
                    height * 8f/5f,
                    height);
        }
        else if (focused) {
            batch.draw(focusedButtonTexture, positionX + 10f, positiveY + 10f,
                    height * 8f/5f,
                    height);
        }
        else if (hover) {
            batch.draw(hoverButtonTexture, positionX + 10f, positiveY + 10f,
                    height * 8f/5f,
                    height);
        }
        else {
            batch.draw(outlinedButtonTexture, positionX + 10f, positiveY + 10f,
                    height * 8f/5f,
                    height);
        }

    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 8f/5f,
                height);
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 8f/5f,
                height);
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 8f/5f,
                height);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 8f/5f,
                height);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
