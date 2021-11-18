package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.state.GameState;

public class PlayerHand extends Actor {

    private float positionX;

    private float positionY;

    private float islandWidth;

    private float islandHeight;

    private String tileName;

    private Pair coordinates;

    private int tileState;

    private Texture islandTileNormalImage;

    private Texture islandTileFloodedImage;

    private Texture islandTileSunkenImage;

    private static final Texture islandTileMovementImage = new Texture(Gdx.files.internal("./island-tiles/Tile_Movement_Icon@2x.png"));

    private static final Texture islandTileHoverImage = new Texture(Gdx.files.internal("./island-tiles/Tile_Hover_Icon@2x.png"));

    private static final Texture islandTileFocusedImage = new Texture(Gdx.files.internal("./island-tiles/Tile_Focused_Icon@2x.png"));

    private static final Texture islandTileSpecialMovementImage = new Texture(Gdx.files.internal("./island-tiles/Tile_Special_Movement_Icon@2x.png"));

    private boolean focused;

    private boolean hovered;

    private boolean canMove;

    private boolean canMoveSpecialAction;

    public PlayerHand(
            float positionX,
            float positionY,
            float width,
            float height,
            String tileName,
            Pair coordinates,
            int tileState
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.islandWidth = width;
        this.islandHeight = height;
        this.tileName = tileName;
        this.coordinates = coordinates;
        this.tileState = tileState;
        islandTileNormalImage = new Texture(Gdx.files.internal("./island-tiles/" + tileName + "@2x.png"));
        islandTileFloodedImage = new Texture(Gdx.files.internal("./island-tiles/" + tileName + "_flood@2x.png"));
        islandTileSunkenImage = new Texture(Gdx.files.internal("./island-tiles/Sunken-Tile.png"));
        islandTileNormalImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        islandTileFloodedImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        islandTileSunkenImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        islandTileMovementImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        islandTileHoverImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        islandTileFocusedImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        islandTileSpecialMovementImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        super.setBounds(this.positionX, this.positionY, this.islandWidth, this.islandHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (hovered) {
            batch.draw(islandTileHoverImage, positionX-2, positionY+3, islandWidth, islandHeight);
        }
        else if (focused) {
            batch.draw(islandTileFocusedImage, positionX-2, positionY+3, islandWidth, islandHeight);
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(this.positionX, this.positionY, this.islandWidth, this.islandHeight);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        super.setBounds(this.positionX, this.positionY, this.islandWidth, this.islandHeight);
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public Texture getIslandTileNormalImage() {
        return islandTileNormalImage;
    }

    public void setIslandTileNormalImage(Texture islandTileNormalImage) {
        this.islandTileNormalImage = islandTileNormalImage;
    }

    public Pair getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Pair coordinates) {
        this.coordinates = coordinates;
    }

}
