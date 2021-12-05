package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseButton extends Actor {

    private Logger logger = LoggerFactory.getLogger(CloseButton.class);

    private boolean hover;

    private boolean focused;

    private boolean claimed;

    private Texture outlinedButtonTexture;

    private Texture hoverButtonTexture;

    private Texture focusedButtonTexture;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    public CloseButton(
            float positionX,
            float positionY,
            float height,
            float width
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;

        outlinedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/close-button/Close Button.png", Texture.class);
        hoverButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/close-button/Close Button Hover.png", Texture.class);
        focusedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/close-button/Close Button Pressed.png", Texture.class);

        outlinedButtonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(positionX, positiveY, width, height);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                focused = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                focused = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
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

        if (focused) {
            batch.draw(focusedButtonTexture, positionX, positiveY, width, height);
        }
        else if (hover) {
            batch.draw(hoverButtonTexture, positionX, positiveY, width, height);
        }
        else {
            batch.draw(outlinedButtonTexture, positionX, positiveY, width, height);
        }


    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(positionX, positiveY, width, height);
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
        super.setBounds(positionX, positiveY, width, height);
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(positionX, positiveY, width, height);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(positionX, positiveY, width, height);
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

}