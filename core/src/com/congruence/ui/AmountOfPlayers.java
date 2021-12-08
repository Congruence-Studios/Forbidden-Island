package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmountOfPlayers extends Actor {

    private Logger logger = LoggerFactory.getLogger(AmountOfPlayers.class);

    private boolean hover;

    private boolean focused;

    private Texture players2;

    private Texture players2Hover;

    private Texture players3;

    private Texture players3Hover;

    private Texture players4;

    private Texture players4Hover;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private int amountOfPlayers;

    public AmountOfPlayers(
            float positionX,
            float positionY,
            float height,
            float width,
            int amountOfPlayers
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;
        this.amountOfPlayers = amountOfPlayers;

        players2 = new Texture(Gdx.files.internal("./start/player-amount/Player Amount 2.png"), true);
        players2Hover = new Texture(Gdx.files.internal("./start/player-amount/Player Amount 2 Hover.png"), true);
        players3 = new Texture(Gdx.files.internal("./start/player-amount/Player Amount 3.png"), true);
        players3Hover = new Texture(Gdx.files.internal("./start/player-amount/Player Amount 3 Hover.png"), true);
        players4 = new Texture(Gdx.files.internal("./start/player-amount/Player Amount 4.png"), true);
        players4Hover = new Texture(Gdx.files.internal("./start/player-amount/Player Amount 4 Hover.png"), true);

        players2.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        players2Hover.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        players3.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        players3Hover.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        players4.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        players4Hover.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(positionX, positiveY, width, height);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setAmountOfPlayers(getAmountOfPlayers()+1);
                if (getAmountOfPlayers() > 4) {
                    setAmountOfPlayers(2);
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                focused = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hover = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hover = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (amountOfPlayers == 2) {
            if (hover) {
                batch.draw(players2Hover, positionX, positiveY, width, height);
            }
            else {
                batch.draw(players2, positionX, positiveY, width, height);
            }
        }
        else if (amountOfPlayers == 3) {
            if (hover) {
                batch.draw(players3Hover, positionX, positiveY, width, height);
            }
            else {
                batch.draw(players3, positionX, positiveY, width, height);
            }
        }
        else if (amountOfPlayers == 4) {
            if (hover) {
                batch.draw(players4Hover, positionX, positiveY, width, height);
            }
            else {
                batch.draw(players4, positionX, positiveY, width, height);
            }
        }

    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(positionX, positiveY, width, height);
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
        super.setBounds(positionX, positiveY, width, height);
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(positionX, positiveY, width, height);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(positionX, positiveY, width, height);
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public void setAmountOfPlayers(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
    }

}
