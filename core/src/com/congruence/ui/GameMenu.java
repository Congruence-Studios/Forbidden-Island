package com.congruence.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.congruence.state.GameState;

public class GameMenu extends Group {

    private GameMenuSkipButton skipButton;

    private GameMenuBackground gameMenuBackground;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    public GameMenu(
            float positionX,
            float positionY,
            float height,
            float width
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;
        gameMenuBackground = new GameMenuBackground(positionX, positionY, height, width);
        super.addActor(gameMenuBackground);
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        this.gameMenuBackground.setPositionX(positionX);
        this.skipButton.setPositionX(positionX);
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
        this.gameMenuBackground.setPositiveY(positiveY);
        this.skipButton.setPositiveY(positiveY);
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        this.gameMenuBackground.setHeight(height);
        this.skipButton.setHeight(height);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        this.gameMenuBackground.setWidth(width);
        this.skipButton.setWidth(width);
    }

}
