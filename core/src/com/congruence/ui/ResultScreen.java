package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultScreen extends Group {

    private Logger logger = LoggerFactory.getLogger(ResultScreen.class);

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture background;

    private GameState state;

    private GlyphLayout resultLayout;

    private BitmapFont titleFont;

    public ResultScreen(
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

        background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Abel-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.BLACK;
        parameter.genMipMaps = true;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        titleFont = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (state.isGameEnd()) {
            batch.draw(background, positionX, positiveY, width, height);
            resultLayout = new GlyphLayout(titleFont, "");
            if (state.getGameResult() == GameState.WIN) {
                resultLayout.setText(titleFont, "YOU WIN");
                float resultFontX = 0 + (GameConfiguration.width - resultLayout.width) / 2;
                float resultFontY = (GameConfiguration.height - resultLayout.height) / 2f;
                titleFont.draw(batch, resultLayout, resultFontX, resultFontY);
            } else if (state.getGameResult() == GameState.PLAYER_DROWNED) {
                resultLayout.setText(titleFont, "Player Drowned");
                float resultFontX = 0 + (GameConfiguration.width - resultLayout.width) / 2;
                float resultFontY = (GameConfiguration.height - resultLayout.height) / 2f;
                titleFont.draw(batch, resultLayout, resultFontX, resultFontY);
            } else if (state.getGameResult() == GameState.FOOLS_LANDING_SUNK) {
                resultLayout.setText(titleFont, "Fool's Landing Sunk");
                float resultFontX = 0 + (GameConfiguration.width - resultLayout.width) / 2;
                float resultFontY = (GameConfiguration.height - resultLayout.height) / 2f;
                titleFont.draw(batch, resultLayout, resultFontX, resultFontY);
            } else if (state.getGameResult() == GameState.BOTH_TREASURE_TILES_SUNK) {
                resultLayout.setText(titleFont, "Two Treasure Tiles Sunk");
                float resultFontX = 0 + (GameConfiguration.width - resultLayout.width) / 2;
                float resultFontY = (GameConfiguration.height - resultLayout.height) / 2f;
                titleFont.draw(batch, resultLayout, resultFontX, resultFontY);
            } else if (state.getGameResult() == GameState.WATER_METER_FULL) {
                resultLayout.setText(titleFont, "Water Meter Full");
                float resultFontX = 0 + (GameConfiguration.width - resultLayout.width) / 2;
                float resultFontY = (GameConfiguration.height - resultLayout.height) / 2f;
                titleFont.draw(batch, resultLayout, resultFontX, resultFontY);
            }
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
