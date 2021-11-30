package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;

public class WaterMeterScreen extends Actor {

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    public Texture background;

    public Texture waterMeterBoard;

    private BitmapFont titleFont;

    private GameState state;

    private GlyphLayout titleLayout;

    public WaterMeterScreen(
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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Abel-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        titleFont = generator.generateFont(parameter);
        generator.dispose();
        background = new Texture(Gdx.files.internal("./custom-ui/dialog-background/Dialog.png"));

        titleLayout = new GlyphLayout(titleFont, "");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            batch.draw(background, positionX, positiveY, width, height);

            titleLayout.setText(titleFont, "Water Height: " + state.getWaterHeight());
            float fontX = 0 + (GameConfiguration.width - titleLayout.width) / 2;
            float fontY = 0 + (GameConfiguration.height + titleLayout.height) * 3/4f;

            titleFont.draw(batch, titleLayout, fontX, fontY);
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
