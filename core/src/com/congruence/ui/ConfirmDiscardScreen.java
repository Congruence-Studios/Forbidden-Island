package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.state.GameState;
import com.congruence.state.TreasureCard;
import com.congruence.util.Observable;

public class ConfirmDiscardScreen extends Group {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private ConfirmDiscardButton confirmDiscardButton;

    private Observable successObservable = ()->{};

    private Observable failureObservable = ()->{};

    private Texture Background;

    public ConfirmDiscardScreen(
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

        Background = ForbiddenIsland.assetManager.get("custom-ui/draw-screen/Confirm Discard Background.png", Texture.class);
        Background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (isOpen) {
            batch.end();
            batch.begin();

            batch.draw(Background, positionX, positiveY, width, height);
            super.draw(batch, parentAlpha);
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

        if (confirmDiscardButton != null) {
            float x = 0+(getWidth()/2) - 80f;
            float y = 0+(getHeight()*1/2) - 25;
            confirmDiscardButton.setPositionX(x);
            confirmDiscardButton.setPositiveY(y);
            confirmDiscardButton.setHeight(100f);
            confirmDiscardButton.setWidth(160f);
        }
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        if (confirmDiscardButton != null) {
            float x = 0+(getWidth()/2) - 80f;
            float y = 0+(getHeight()*1/2) - 25;
            confirmDiscardButton.setPositionX(x);
            confirmDiscardButton.setPositiveY(y);
            confirmDiscardButton.setHeight(100f);
            confirmDiscardButton.setWidth(160f);
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open, Observable successObservable, Observable failureObservable, TreasureCard treasureCard) {
        this.isOpen = open;
        this.failureObservable = failureObservable;
        this.successObservable = successObservable;
        System.out.println("Confirm Discard Screen");
        if (isOpen) {
            super.setBounds(positionX, positiveY, width, height);
            System.out.println("Confirm Is Open Screen");

            float x = 0+(getWidth()/2) - 80f;
            float y = 0+(getHeight()*1/2) - 25;
            confirmDiscardButton = new ConfirmDiscardButton(x, y, 100f, 160f);
            confirmDiscardButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    successObservable.onFinished();
                }
            });
            addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    failureObservable.onFinished();
                    setOpen(false, null, null,  null);
                }
            });
            addActor(confirmDiscardButton);
        }
        else {
            super.setBounds(0, 0, 0, 0);
            super.clear();
        }
    }
}
