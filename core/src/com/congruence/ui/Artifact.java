package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Artifact extends Actor {

    private static final Logger logger = LoggerFactory.getLogger(Artifact.class);

    private float positionX;

    private float positionY;

    private float artifactWidth;

    private float artifactHeight;

    private String artifactName;

    private int artifactState;

    private Texture artifactTexture;

    public Artifact(
            float positionX,
            float positionY,
            float width,
            float height,
            String artifactName
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.artifactWidth = width;
        this.artifactHeight = height;
        this.artifactName = artifactName;
        artifactTexture = ForbiddenIsland.assetManager.get("./desktop/assets/artifacts/" + artifactName + ".png", Texture.class);
        artifactTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        super.setBounds(this.positionX, this.positionY, this.artifactWidth, this.artifactHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        batch.draw(artifactTexture, positionX, positionY, artifactWidth, artifactHeight);
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
