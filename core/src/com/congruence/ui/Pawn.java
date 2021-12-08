package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.state.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pawn extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(Pawn.class);

    public static final int NORMAL = 0;
    public static final int ACTIVE = 1;
    public static final int GIVE = 2;
    public static final int MOVE = 3;
    public static final int FOCUSED = 4;

    private float x;

    private float y;

    private float pawnWidth;

    private float pawnHeight;

    private int pawnState;

    private int ability;

    private Texture normalTexture;

    private Texture activeTexture;

    private Texture giveTexture;

    private Texture moveTexture;

    private Texture focusedTexture;

    public Pawn(
            float x,
            float y,
            float width,
            float height,
            int ability
    ) {
        this.x = x;
        this.y = y;
        this.pawnHeight = height;
        this.pawnWidth = width;
        this.ability = ability;
        if (ability == Player.PILOT) {
            normalTexture = ForbiddenIsland.assetManager.get("pawn/Pilot-Pawn.png", Texture.class);
            activeTexture = ForbiddenIsland.assetManager.get("pawn/Pilot-Pawn-Active.png", Texture.class);
            giveTexture = ForbiddenIsland.assetManager.get("pawn/Pilot-Pawn-Give.png", Texture.class);
            moveTexture = ForbiddenIsland.assetManager.get("pawn/Pilot-Pawn-Move.png", Texture.class);
            focusedTexture = ForbiddenIsland.assetManager.get("pawn/Pilot-Pawn-Focused.png", Texture.class);
        } else if (ability == Player.ENGINEER) {
            normalTexture = ForbiddenIsland.assetManager.get("pawn/Engineer-Pawn.png", Texture.class);
            activeTexture = ForbiddenIsland.assetManager.get("pawn/Engineer-Pawn-Active.png", Texture.class);
            giveTexture = ForbiddenIsland.assetManager.get("pawn/Engineer-Pawn-Give.png", Texture.class);
            moveTexture = ForbiddenIsland.assetManager.get("pawn/Engineer-Pawn-Move.png", Texture.class);
            focusedTexture = ForbiddenIsland.assetManager.get("pawn/Engineer-Pawn-Focused.png", Texture.class);
        } else if (ability == Player.MESSENGER) {
            normalTexture = ForbiddenIsland.assetManager.get("pawn/Messenger-Pawn.png", Texture.class);
            activeTexture = ForbiddenIsland.assetManager.get("pawn/Messenger-Pawn-Active.png", Texture.class);
            giveTexture = ForbiddenIsland.assetManager.get("pawn/Messenger-Pawn-Give.png", Texture.class);
            moveTexture = ForbiddenIsland.assetManager.get("pawn/Messenger-Pawn-Move.png", Texture.class);
            focusedTexture = ForbiddenIsland.assetManager.get("pawn/Messenger-Pawn-Focused.png", Texture.class);
        } else if (ability == Player.EXPLORER) {
            normalTexture = ForbiddenIsland.assetManager.get("pawn/Explorer-Pawn.png", Texture.class);
            activeTexture = ForbiddenIsland.assetManager.get("pawn/Explorer-Pawn-Active.png", Texture.class);
            giveTexture = ForbiddenIsland.assetManager.get("pawn/Explorer-Pawn-Give.png", Texture.class);
            moveTexture = ForbiddenIsland.assetManager.get("pawn/Explorer-Pawn-Move.png", Texture.class);
            focusedTexture = ForbiddenIsland.assetManager.get("pawn/Explorer-Pawn-Focused.png", Texture.class);
        } else if (ability == Player.DIVER) {
            normalTexture = ForbiddenIsland.assetManager.get("pawn/Diver-Pawn.png", Texture.class);
            activeTexture = ForbiddenIsland.assetManager.get("pawn/Diver-Pawn-Active.png", Texture.class);
            giveTexture = ForbiddenIsland.assetManager.get("pawn/Diver-Pawn-Give.png", Texture.class);
            moveTexture = ForbiddenIsland.assetManager.get("pawn/Diver-Pawn-Move.png", Texture.class);
            focusedTexture = ForbiddenIsland.assetManager.get("pawn/Diver-Pawn-Focused.png", Texture.class);
        } else if (ability == Player.NAVIGATOR) {
            normalTexture = ForbiddenIsland.assetManager.get("pawn/Navigator-Pawn.png", Texture.class);
            activeTexture = ForbiddenIsland.assetManager.get("pawn/Navigator-Pawn-Active.png", Texture.class);
            giveTexture = ForbiddenIsland.assetManager.get("pawn/Navigator-Pawn-Give.png", Texture.class);
            moveTexture = ForbiddenIsland.assetManager.get("pawn/Navigator-Pawn-Move.png", Texture.class);
            focusedTexture = ForbiddenIsland.assetManager.get("pawn/Navigator-Pawn-Focused.png", Texture.class);
        }
        normalTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        activeTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        giveTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        moveTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        focusedTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(this.x, this.y, this.pawnWidth, this.pawnHeight);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //focused = !focused;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                logger.info("enter: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (pawnState == FOCUSED) {
            batch.draw(focusedTexture, x, y, pawnWidth, pawnHeight);
        }
        else if (pawnState == MOVE) {
            batch.draw(moveTexture, x, y, pawnWidth, pawnHeight);
        } else {
            batch.draw(normalTexture, x, y, pawnWidth, pawnHeight);
        }
    }


    @Override
    public float getHeight() {
        return pawnHeight;
    }

    @Override
    public void setHeight(float height) {
        this.pawnHeight = height;
        super.setBounds(this.x, this.y, this.pawnWidth, this.pawnHeight);
    }

    @Override
    public float getWidth() {
        return pawnWidth;
    }

    @Override
    public void setWidth(float width) {
        this.pawnWidth = width;
        super.setBounds(this.x, this.y, this.pawnWidth, this.pawnHeight);
    }

    public float getX() {
        return x;
    }

    public void setX(float positionX) {
        this.x = positionX;
        super.setBounds(this.x, this.y, this.pawnWidth, this.pawnHeight);
    }

    public float getY() {
        return y;
    }

    public void setY(float positionY) {
        this.y = positionY;
        super.setBounds(this.x, this.y, this.pawnWidth, this.pawnHeight);
    }

    public int getPawnState() {
        return pawnState;
    }

    public void setPawnState(int pawnState) {
        this.pawnState = pawnState;
    }

    public String toString() {
        return "X: " + x + " Y: " + y + " width: " + pawnWidth + " height: " + pawnHeight;
    }


    public int getAbility() {
        return ability;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }
}
