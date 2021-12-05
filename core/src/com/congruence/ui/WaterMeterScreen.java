package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterMeterScreen extends Group {

    private Logger logger = LoggerFactory.getLogger(WaterMeterScreen.class);

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture background;

    private Texture WaterMeterHeight1;

    private Texture WaterMeterHeight2;

    private Texture WaterMeterHeight3;

    private Texture WaterMeterHeight4;

    private Texture WaterMeterHeight5;

    private Texture WaterMeterHeight6;

    private Texture WaterMeterHeight7;

    private Texture WaterMeterHeight8;

    private Texture WaterMeterHeight9;

    private Texture WaterMeterHeight10;

    private GameState state;

    private CloseButton closeButton;

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

        super.setBounds(0, 0, 0, 0);

        background = ForbiddenIsland.assetManager.get("custom-ui/dialog-background/Dialog.png", Texture.class);
        WaterMeterHeight1 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-1.png", Texture.class);
        WaterMeterHeight2 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-2.png", Texture.class);
        WaterMeterHeight3 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-3.png", Texture.class);
        WaterMeterHeight4 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-4.png", Texture.class);
        WaterMeterHeight5 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-5.png", Texture.class);
        WaterMeterHeight6 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-6.png", Texture.class);
        WaterMeterHeight7 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-7.png", Texture.class);
        WaterMeterHeight8 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-8.png", Texture.class);
        WaterMeterHeight9 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-9.png", Texture.class);
        WaterMeterHeight10 = ForbiddenIsland.assetManager.get("water-meter/Water Meter-10.png", Texture.class);

        background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight1.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight2.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight3.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight4.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight5.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight6.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight7.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight8.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight9.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WaterMeterHeight10.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        closeButton = new CloseButton(
                getWidth() - 50,
                getHeight() - 50,
                40,
                40
        );
        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setOpen(false);
            }
        });
        this.addActor(closeButton);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            batch.draw(background, positionX, positiveY, width, height);

            if (state.getWaterHeight() == 1) {
                batch.draw(WaterMeterHeight1, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 2) {
                batch.draw(WaterMeterHeight2, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 3) {
                batch.draw(WaterMeterHeight3, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 4) {
                batch.draw(WaterMeterHeight4, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 5) {
                batch.draw(WaterMeterHeight5, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 6) {
                batch.draw(WaterMeterHeight6, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 7) {
                batch.draw(WaterMeterHeight7, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 8) {
                batch.draw(WaterMeterHeight8, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 9) {
                batch.draw(WaterMeterHeight9, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }
            else if (state.getWaterHeight() == 10) {
                batch.draw(WaterMeterHeight10, (GameConfiguration.width / 2f) - (height * 7/8f * 5/8f * 0.5f), positiveY + getHeight()/2f - (height * 7/8f * 0.5f), height * 7/8f * 5/8f, height * 7/8f);
            }

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
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;

        closeButton.setPositionX(width-50);
        closeButton.setPositiveY(getHeight()-50);
        closeButton.setWidth(40);
        closeButton.setHeight(40);

        logger.info(height + " " + closeButton.getPositiveY());
        logger.info(width + " " + closeButton.getPositionX());
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
