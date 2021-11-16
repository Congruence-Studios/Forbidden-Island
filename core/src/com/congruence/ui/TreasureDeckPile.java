package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TreasureDeckPile extends Actor {

    private boolean hover;

    private boolean focused;

    private boolean enabled;

    private float positionX;

    private float positionY;

    private float treasureDeckWidth;

    private float treasureDeckHeight;

    private Texture outlinedButtonTexture;

    private Texture hoverButtonTexture;

    private Texture focusedButtonTexture;

    private Texture disabledButtonTexture;

    public TreasureDeckPile(
            float positionX,
            float positionY,
            float width,
            float height
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.treasureDeckWidth = width;
        this.treasureDeckHeight = height;
        outlinedButtonTexture = new Texture(Gdx.files.internal("./treasure-deck/Treasure-Deck.png"));
        hoverButtonTexture = new Texture(Gdx.files.internal("./treasure-deck/Treasure-Deck-Hovered.png"));
        focusedButtonTexture = new Texture(Gdx.files.internal("./treasure-deck/Treasure-Deck-Pressed.png"));
        disabledButtonTexture = new Texture(Gdx.files.internal("./treasure-deck/Treasure-Deck-Disabled.png"));
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        super.setBounds(this.positionX, this.positionY, this.treasureDeckWidth, this.treasureDeckHeight);
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
            batch.draw(disabledButtonTexture, positionX + 10f, positionY + 10f,
                    treasureDeckHeight * 8f/5f,
                    treasureDeckHeight);
        }
        else if (focused) {
            batch.draw(focusedButtonTexture, positionX + 10f, positionY + 10f,
                    treasureDeckHeight * 8f/5f,
                    treasureDeckHeight);
        }
        else if (hover) {
            batch.draw(hoverButtonTexture, positionX + 10f, positionY + 10f,
                    treasureDeckHeight * 8f/5f,
                    treasureDeckHeight);
        }
        else {
            batch.draw(outlinedButtonTexture, positionX + 10f, positionY + 10f,
                    treasureDeckHeight * 8f/5f,
                    treasureDeckHeight);
        }

    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(this.positionX, this.positionY, treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        super.setBounds(this.positionX, this.positionY, treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public float getTreasureDeckWidth() {
        return treasureDeckWidth;
    }

    public void setTreasureDeckWidth(float treasureDeckWidth) {
        this.treasureDeckWidth = treasureDeckWidth;
        super.setBounds(this.positionX, this.positionY, treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public float getTreasureDeckHeight() {
        return treasureDeckHeight;
    }

    public void setTreasureDeckHeight(float treasureDeckHeight) {
        this.treasureDeckHeight = treasureDeckHeight;
        super.setBounds(this.positionX, this.positionY,treasureDeckHeight * 8f/5f, this.treasureDeckHeight);
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
