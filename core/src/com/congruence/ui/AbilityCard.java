package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.state.GameState;
import com.congruence.state.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbilityCard extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(AbilityCard.class);

    private float positionX;

    private float positionY;

    private float width;

    private float height;

    private int ability;

    private Texture outlinedButtonTexture;

    private Texture hoverButtonTexture;

    private Texture focusedButtonTexture;

    private Texture abilityIcon;

    private boolean focused;

    private boolean hovered;

    public AbilityCard(
            float positionX,
            float positionY,
            float width,
            float height,
            int ability
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.ability = ability;
        outlinedButtonTexture = new Texture(Gdx.files.internal("./ability-button/Ability-Button"));
        hoverButtonTexture = new Texture(Gdx.files.internal("./ability-button/Ability-Button-Hovered"));
        focusedButtonTexture = new Texture(Gdx.files.internal("./ability-button/Ability-Button-Focused"));
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        if (ability == Player.PILOT) {
            abilityIcon = new Texture(Gdx.files.internal("./ability-icon/RoleTable_Icon_Pilot@2x.png"));
        } else if (ability == Player.ENGINEER) {
            abilityIcon = new Texture(Gdx.files.internal("./ability-icon/RoleTable_Icon_Engineer@2x.png"));
        } else if (ability == Player.MESSENGER) {
            abilityIcon = new Texture(Gdx.files.internal("./ability-icon/RoleTable_Icon_Messenger@2x.png"));
        } else if (ability == Player.EXPLORER) {
            abilityIcon = new Texture(Gdx.files.internal("./ability-icon/RoleTable_Icon_Explorer@2x.png"));
        } else if (ability == Player.DIVER) {
            abilityIcon = new Texture(Gdx.files.internal("./ability-icon/RoleTable_Icon_Diver@2x.png"));
        } else if (ability == Player.NAVIGATOR) {
            abilityIcon = new Texture(Gdx.files.internal("./ability-icon/RoleTable_Icon_Navigator@2x.png"));
        }

        super.setBounds(this.positionX, this.positionY, this.width, this.height);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                logger.info("touchDown: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                focused = true;
                //pop up new window with more details
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                focused = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                logger.info("enter: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                hovered = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hovered = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        if (hovered) {
            batch.draw(outlinedButtonTexture, positionX, positionY, width, height);
        }
        else if (focused) {
            batch.draw(hoverButtonTexture, positionX, positionY, width, height);
        }
        else {
            batch.draw(focusedButtonTexture, positionX, positionY, width, height);
        }
        batch.draw(abilityIcon, (positionX - abilityIcon.getWidth()) / 2, (positionY - abilityIcon.getHeight()) - 2, abilityIcon.getWidth(), abilityIcon.getHeight());
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
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

    public int getAbility () {
        return ability;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }
}
