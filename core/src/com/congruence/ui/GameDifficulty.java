package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.state.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameDifficulty extends Actor {

    private Logger logger = LoggerFactory.getLogger(GameDifficulty.class);

    private boolean hover;

    private boolean focused;

    private Texture novice;

    private Texture noviceHover;

    private Texture normal;

    private Texture normalHover;

    private Texture elite;

    private Texture eliteHover;

    private Texture legendary;

    private Texture legendaryHover;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private int difficulty;

    public GameDifficulty(
            float positionX,
            float positionY,
            float height,
            float width,
            int difficulty
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;
        this.difficulty = difficulty;


        novice = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Novice.png"), true);
        noviceHover = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Novice Hover.png"), true);
        normal = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Normal.png"), true);
        normalHover = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Normal Hover.png"), true);
        elite = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Elite.png"), true);
        eliteHover = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Elite Hover.png"), true);
        legendary = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Legendary.png"), true);
        legendaryHover = new Texture(Gdx.files.internal("./start/difficulty/Difficulty Legendary Hover.png"), true);

        novice.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        noviceHover.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        normal.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        normalHover.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        elite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        eliteHover.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        legendary.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        legendaryHover.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(positionX, positiveY, width, height);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setDifficulty(getDifficulty()+1);
                if (getDifficulty() > GameState.LEGENDARY) {
                    setDifficulty(GameState.NOVICE);
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

        if (difficulty == GameState.NOVICE) {
            if (hover) {
                batch.draw(noviceHover, positionX, positiveY, width, height);
            }
            else {
                batch.draw(novice, positionX, positiveY, width, height);
            }
        }
        else if (difficulty == GameState.NORMAL) {
            if (hover) {
                batch.draw(normalHover, positionX, positiveY, width, height);
            }
            else {
                batch.draw(normal, positionX, positiveY, width, height);
            }
        }
        else if (difficulty == GameState.ELITE) {
            if (hover) {
                batch.draw(eliteHover, positionX, positiveY, width, height);
            }
            else {
                batch.draw(elite, positionX, positiveY, width, height);
            }
        }
        else if (difficulty == GameState.LEGENDARY) {
            if (hover) {
                batch.draw(legendaryHover, positionX, positiveY, width, height);
            }
            else {
                batch.draw(legendary, positionX, positiveY, width, height);
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

}
