package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;

public class GameMenuSwapButton extends Actor {

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

    public GameMenuSwapButton(
            float positionX,
            float positionY,
            float height,
            float width
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;

        outlinedButtonTexture = ForbiddenIsland.assetManager.get("./desktop/assets/custom-ui/give-button/Give-Button.png", Texture.class);
        hoverButtonTexture = ForbiddenIsland.assetManager.get("./desktop/assets/custom-ui/give-button/Give-Button-Hovered.png", Texture.class);
        focusedButtonTexture = ForbiddenIsland.assetManager.get("./desktop/assets/custom-ui/give-button/Give-Button-Pressed.png", Texture.class);
        disabledButtonTexture = ForbiddenIsland.assetManager.get("./desktop/assets/custom-ui/give-button/Give-Button-Disabled.png", Texture.class);
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        super.setBounds(positionX + 10f, positiveY + 10f, outlinedButtonTexture.getWidth(), hoverButtonTexture.getHeight());
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
