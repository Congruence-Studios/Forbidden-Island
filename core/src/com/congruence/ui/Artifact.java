package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import com.congruence.state.GameState;
import com.congruence.state.Resources;

public class Artifact extends Actor {

    private float positionX;

    private float positionY;

    private float artifactWidth;

    private float artifactHeight;

    private String artifactName;

    private int artifactState;

    private Texture artifactTexture;

    private GameState state;

    public Artifact(
            float positionX,
            float positionY,
            float width,
            float height,
            String artifactName,
            GameState state
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.artifactWidth = width;
        this.artifactHeight = height;
        this.artifactName = artifactName;
        this.state = state;
        artifactTexture = ForbiddenIsland.assetManager.get("artifacts/" + artifactName + ".png", Texture.class);
        artifactTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        super.setBounds(this.positionX, this.positionY, this.artifactWidth, this.artifactHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (artifactName.equals(Resources.ArtifactNames[GameState.CRYSTAL_OF_FIRE]) && !state.getCollectedArtifacts()[GameState.CRYSTAL_OF_FIRE]) {
            batch.draw(artifactTexture, positionX, positionY, artifactWidth, artifactHeight);
        }
        else if (artifactName.equals(Resources.ArtifactNames[GameState.EARTH_STONE]) && !state.getCollectedArtifacts()[GameState.EARTH_STONE]) {
            batch.draw(artifactTexture, positionX, positionY, artifactWidth, artifactHeight);
        }
        else if (artifactName.equals(Resources.ArtifactNames[GameState.STATUE_OF_THE_WIND]) && !state.getCollectedArtifacts()[GameState.STATUE_OF_THE_WIND]) {
            batch.draw(artifactTexture, positionX, positionY, artifactWidth, artifactHeight);
        }
        else if (artifactName.equals(Resources.ArtifactNames[GameState.OCEANS_CHALICE]) && !state.getCollectedArtifacts()[GameState.OCEANS_CHALICE]) {
            batch.draw(artifactTexture, positionX, positionY, artifactWidth, artifactHeight);
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(this.positionX, this.positionY, this.artifactWidth, this.artifactHeight);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        super.setBounds(this.positionX, this.positionY, this.artifactWidth, this.artifactHeight);
    }

    public float getArtifactWidth() {
        return artifactWidth;
    }

    public void setArtifactWidth(float artifactWidth) {
        this.artifactWidth = artifactWidth;
        super.setBounds(this.positionX, this.positionY, this.artifactWidth, this.artifactHeight);
    }

    public float getArtifactHeight() {
        return artifactHeight;
    }

    public void setArtifactHeight(float artifactHeight) {
        this.artifactHeight = artifactHeight;
        super.setBounds(this.positionX, this.positionY, this.artifactWidth, this.artifactHeight);
    }
}
