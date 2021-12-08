package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.state.GameState;

public class IslandTile extends Actor {

    private float positionX;

    private float positionY;

    private float islandWidth;

    private float islandHeight;

    private String tileName;

    private Pair coordinates;

    private GameState state;

    private Texture islandTileNormalImage;

    private Texture islandTileFloodedImage;

    private Texture islandTileSunkenImage;

    private Texture islandTileMovementImage = ForbiddenIsland.assetManager.get("island-tiles/Tile_Movement_Icon@2x.png", Texture.class);

    private Texture islandTileHoverImage = ForbiddenIsland.assetManager.get("island-tiles/Tile_Hover_Icon@2x.png", Texture.class);

    private Texture islandTileFocusedImage = ForbiddenIsland.assetManager.get("island-tiles/Tile_Focused_Icon@2x.png", Texture.class);

    private Texture islandTileSpecialMovementImage = ForbiddenIsland.assetManager.get("island-tiles/Tile_Special_Movement_Icon@2x.png", Texture.class);

    private Texture islandTileShoreUpImage = ForbiddenIsland.assetManager.get("island-tiles/Tile_Special_Movement_Icon@2x.png", Texture.class);

    private Texture islandTileDeathImage = ForbiddenIsland.assetManager.get("island-tiles/Tile_Death_Icon@2x.png", Texture.class);

    private boolean focused;

    private boolean hovered;

    private boolean canMove;

    private boolean canShoreUp;

    private boolean canMoveSpecialAction;

    private boolean canSuddenDeathMove;

    private final boolean[] tilePositionOpen = new boolean[]{true, true, true, true};

    private int tileState;

    public IslandTile(
            float positionX,
            float positionY,
            float width,
            float height,
            String tileName,
            Pair coordinates,
            GameState state
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.islandWidth = width;
        this.islandHeight = height;
        this.tileName = tileName;
        this.coordinates = coordinates;
        this.state = state;
        islandTileNormalImage = ForbiddenIsland.assetManager.get("island-tiles/" + tileName + ".png", Texture.class);
        islandTileFloodedImage = ForbiddenIsland.assetManager.get("island-tiles/" + tileName + " Flooded.png", Texture.class);
        islandTileNormalImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        islandTileFloodedImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        islandTileMovementImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        islandTileHoverImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        islandTileFocusedImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        islandTileSpecialMovementImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        islandTileShoreUpImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        islandTileDeathImage.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(this.positionX, this.positionY, this.islandWidth, this.islandHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (state.getIslandTileState()[getCoordinates().x][getCoordinates().y] == GameState.NORMAL_ISLAND_TILE) {
            batch.draw(islandTileNormalImage, positionX, positionY, islandWidth, islandHeight);
        }
        else if (state.getIslandTileState()[getCoordinates().x][getCoordinates().y] == GameState.FLOODED_ISLAND_TILE) {
            batch.draw(islandTileFloodedImage, positionX, positionY, islandWidth, islandHeight);
        }
        else if (state.getIslandTileState()[getCoordinates().x][getCoordinates().y] == GameState.SUNKEN_ISLAND_TILE) {
        }
        if (state.getIslandTileState()[getCoordinates().x][getCoordinates().y] == GameState.SUNKEN_ISLAND_TILE) {
        }
        else if (hovered) {
            batch.draw(islandTileHoverImage, positionX-2, positionY+3, islandWidth, islandHeight);
        }
        else if (focused) {
            batch.draw(islandTileFocusedImage, positionX-2, positionY+3, islandWidth, islandHeight);
        }
        else if (canSuddenDeathMove) {
            batch.draw(islandTileDeathImage, positionX-2, positionY+3, islandWidth, islandHeight);
        }
        else if (canShoreUp || (state.isSandbagUsed() && state.getIslandTileState()[getCoordinates().x][getCoordinates().y] == GameState.FLOODED_ISLAND_TILE)) {
            batch.draw(islandTileShoreUpImage, positionX-2, positionY+3, islandWidth, islandHeight);
        }
        else if (canMove || state.isHelicopterUsed()) {
            batch.draw(islandTileMovementImage, positionX-2, positionY+3, islandWidth, islandHeight);
        }
        else if (canMoveSpecialAction) {
            batch.draw(islandTileSpecialMovementImage, positionX-2, positionY+3, islandWidth, islandHeight);
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

    public float getIslandWidth() {
        return islandWidth;
    }

    public void setIslandWidth(float islandWidth) {
        this.islandWidth = islandWidth;
        super.setBounds(this.positionX, this.positionY, this.islandWidth, this.islandHeight);
    }

    public float getIslandHeight() {
        return islandHeight;
    }

    public void setIslandHeight(float islandHeight) {
        this.islandHeight = islandHeight;
        super.setBounds(this.positionX, this.positionY, this.islandWidth, this.islandHeight);
    }

    public int getTileState() {
        return tileState;
    }

    public void setTileState(int tileState) {
        this.tileState = tileState;
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

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isCanMoveSpecialAction() {
        return canMoveSpecialAction;
    }

    public void setCanMoveSpecialAction(boolean canMoveSpecialAction) {
        this.canMoveSpecialAction = canMoveSpecialAction;
    }

    public String getTileName() {
        return tileName;
    }

    public void setTileName(String tileName) {
        this.tileName = tileName;
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

    public boolean[] getTilePositionOpen() {
        return tilePositionOpen;
    }

    public boolean isCanShoreUp() {
        return canShoreUp;
    }

    public void setCanShoreUp(boolean canShoreUp) {
        this.canShoreUp = canShoreUp;
    }

    public boolean isCanSuddenDeathMove() {
        return canSuddenDeathMove;
    }

    public void setCanSuddenDeathMove(boolean canSuddenDeathMove) {
        this.canSuddenDeathMove = canSuddenDeathMove;
    }
}
