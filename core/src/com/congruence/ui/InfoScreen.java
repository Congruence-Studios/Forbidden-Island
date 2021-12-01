package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;

public class InfoScreen extends Actor {

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture background;

    private BitmapFont titleFont;

    private GameState state;

    private GlyphLayout turnLayout;

    private GlyphLayout actionCountLayout;

    public InfoScreen(
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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Abel-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.BLACK;
        titleFont = generator.generateFont(parameter);
        generator.dispose();
        background = ForbiddenIsland.assetManager.get("./desktop/assets/custom-ui/dialog-background/Dialog.png", Texture.class);

        turnLayout = new GlyphLayout(titleFont, "");
        actionCountLayout = new GlyphLayout(titleFont, "");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            batch.draw(background, positionX, positiveY, width, height);

            turnLayout.setText(titleFont, "Player Turn: " + state.getPlayers().get(state.getPlayerOrder().get(state.getTurnNumber())).getPlayerName());
            float turnFontX = 0 + (GameConfiguration.width - turnLayout.width) / 2;
            float turnFontY = positiveY + height - 25f;

            actionCountLayout.setText(titleFont, "Actions Left: " + state.getCurrentPlayerActionsLeft());
            float actionCountFontX = 0 + (GameConfiguration.width - actionCountLayout.width) / 2;
            float actionCountFontY = positiveY + height - 125f;

            titleFont.draw(batch, turnLayout, turnFontX, turnFontY);
            titleFont.draw(batch, actionCountLayout, actionCountFontX, actionCountFontY);

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

    public void setOpen(boolean open) {
        isOpen = open;
        if (isOpen) {
            super.setBounds(positionX, positiveY, width, height);
        }
        else {
            super.setBounds(0, 0, 0, 0);
        }
    }
}