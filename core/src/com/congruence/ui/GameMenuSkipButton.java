package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.GameConfiguration;
import com.congruence.state.GameState;

public class GameMenuSkipButton extends Actor {

    private boolean hover;

    private boolean focused;

    private Texture outlinedButtonTexture;

    private Texture hoverButtonTexture;

    private Texture focusedButtonTexture;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    public GameMenuSkipButton(
            float positionX,
            float positionY,
            float height,
            float width
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;

        outlinedButtonTexture = new Texture(Gdx.files.internal("./custom-ui/skip-button/Skip Button.png"));
        hoverButtonTexture = new Texture(Gdx.files.internal("./custom-ui/skip-button/Skip Button Hover.png"));
        focusedButtonTexture = new Texture(Gdx.files.internal("./custom-ui/skip-button/Skip Button Pressed.png"));

        super.setBounds(positionX + 10f, positiveY + 10f, outlinedButtonTexture.getWidth(), hoverButtonTexture.getHeight());
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                focused = true;
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

        if (focused) {
            batch.draw(focusedButtonTexture, positionX + 10f, positiveY + 10f,
                    height * 200f/75f,
                    height);
        }
        else if (hover) {
            batch.draw(hoverButtonTexture, positionX + 10f, positiveY + 10f,
                    height * 200f/75f,
                    height);
        }
        else {
            batch.draw(outlinedButtonTexture, positionX + 10f, positiveY + 10f,
                    height * 200f/75f,
                    height);
        }

    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 200f/75f,
                height);
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 200f/75f,
                height);
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 200f/75f,
                height);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(positionX + 10f, positiveY + 10f,
                height * 200f/75f,
                height);
    }

}
