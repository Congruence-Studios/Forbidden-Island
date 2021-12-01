package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
            normalTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Pilot-Pawn.png"));
            activeTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Pilot-Pawn-Active.png"));
            giveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Pilot-Pawn-Give.png"));
            moveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Pilot-Pawn-Move.png"));
            focusedTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Pilot-Pawn-Focused.png"));
        } else if (ability == Player.ENGINEER) {
            normalTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Engineer-Pawn.png"));
            activeTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Engineer-Pawn-Active.png"));
            giveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Engineer-Pawn-Give.png"));
            moveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Engineer-Pawn-Move.png"));
            focusedTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Engineer-Pawn-Focused.png"));
        } else if (ability == Player.MESSENGER) {
            normalTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Messenger-Pawn.png"));
            activeTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Messenger-Pawn-Active.png"));
            giveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Messenger-Pawn-Give.png"));
            moveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Messenger-Pawn-Move.png"));
            focusedTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Messenger-Pawn-Focused.png"));
        } else if (ability == Player.EXPLORER) {
            normalTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Explorer-Pawn.png"));
            activeTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Explorer-Pawn-Active.png"));
            giveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Explorer-Pawn-Give.png"));
            moveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Explorer-Pawn-Move.png"));
            focusedTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Explorer-Pawn-Focused.png"));
        } else if (ability == Player.DIVER) {
            normalTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Diver-Pawn.png"));
            activeTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Diver-Pawn-Active.png"));
            giveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Diver-Pawn-Give.png"));
            moveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Diver-Pawn-Move.png"));
            focusedTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Diver-Pawn-Focused.png"));
        } else if (ability == Player.NAVIGATOR) {
            normalTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Navigator-Pawn.png"));
            activeTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Navigator-Pawn-Active.png"));
            giveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Navigator-Pawn-Give.png"));
            moveTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Navigator-Pawn-Move.png"));
            focusedTexture = new Texture(Gdx.files.internal("./desktop/assets/custom-ui/pawn/Navigator-Pawn-Focused.png"));
        }

        super.setBounds(this.x, this.y, this.pawnWidth, this.pawnHeight);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                logger.info("touchDown: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                pawnState = FOCUSED;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

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

    public String toString() {
        return "X: " + x + " Y: " + y + " width: " + pawnWidth + " height: " + pawnHeight;
    }
}
