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

public class TurnChangeScreen extends Actor {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture DiverTexture;

    private Texture NavigatorTexture;

    private Texture MessengerTexture;

    private Texture ExplorerTexture;

    private Texture EngineerTexture;

    private Texture PilotTexture;

    public TurnChangeScreen(
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

        EngineerTexture = ForbiddenIsland.assetManager.get("custom-ui/dialog-background/Dialog.png", Texture.class);
        ExplorerTexture = ForbiddenIsland.assetManager.get("water-meter/Water Meter-1.png", Texture.class);
        DiverTexture = ForbiddenIsland.assetManager.get("water-meter/Water Meter-2.png", Texture.class);
        NavigatorTexture = ForbiddenIsland.assetManager.get("water-meter/Water Meter-3.png", Texture.class);
        MessengerTexture = ForbiddenIsland.assetManager.get("water-meter/Water Meter-4.png", Texture.class);
        PilotTexture = ForbiddenIsland.assetManager.get("water-meter/Water Meter-4.png", Texture.class);

        EngineerTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        ExplorerTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        DiverTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        NavigatorTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        PilotTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        MessengerTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            if (state.getPlayerOrder().get(state.getTurnNumber()).equals("Engineer")) {
                batch.draw(EngineerTexture, positionX, positiveY, width, height);
            }
            else if (state.getPlayerOrder().get(state.getTurnNumber()).equals("Explorer")) {
                batch.draw(ExplorerTexture, positionX, positiveY, width, height);
            }
            else if (state.getPlayerOrder().get(state.getTurnNumber()).equals("Navigator")) {
                batch.draw(NavigatorTexture, positionX, positiveY, width, height);
            }
            else if (state.getPlayerOrder().get(state.getTurnNumber()).equals("Pilot")) {
                batch.draw(PilotTexture, positionX, positiveY, width, height);
            }
            else if (state.getPlayerOrder().get(state.getTurnNumber()).equals("Diver")) {
                batch.draw(DiverTexture, positionX, positiveY, width, height);
            }
            else {
                batch.draw(MessengerTexture, positionX, positiveY, width, height);
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
