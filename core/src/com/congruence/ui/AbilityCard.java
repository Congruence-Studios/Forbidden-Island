package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
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
        outlinedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/ability-button/Ability-Button.png", Texture.class);
        hoverButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/ability-button/Ability-Button-Hovered.png", Texture.class);
        focusedButtonTexture = ForbiddenIsland.assetManager.get("custom-ui/ability-button/Ability-Button-Focused.png", Texture.class);
        outlinedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hoverButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        focusedButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        if (ability == Player.PILOT) {
            abilityIcon = ForbiddenIsland.assetManager.get("ability-icon/RoleTable_Icon_Pilot@2x.png", Texture.class);
        } else if (ability == Player.ENGINEER) {
            abilityIcon = ForbiddenIsland.assetManager.get("ability-icon/RoleTable_Icon_Engineer@2x.png", Texture.class);
        } else if (ability == Player.MESSENGER) {
            abilityIcon = ForbiddenIsland.assetManager.get("ability-icon/RoleTable_Icon_Messenger@2x.png", Texture.class);
        } else if (ability == Player.EXPLORER) {
            abilityIcon = ForbiddenIsland.assetManager.get("ability-icon/RoleTable_Icon_Explorer@2x.png", Texture.class);
        } else if (ability == Player.DIVER) {
            abilityIcon = ForbiddenIsland.assetManager.get("ability-icon/RoleTable_Icon_Diver@2x.png", Texture.class);
        } else if (ability == Player.NAVIGATOR) {
            abilityIcon = ForbiddenIsland.assetManager.get("ability-icon/RoleTable_Icon_Navigator@2x.png", Texture.class);
        }

        abilityIcon.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(this.positionX, this.positionY, this.width, this.height);
        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //logger.info("X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //logger.info("touchDown: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                focused = true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                focused = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //logger.info("enter: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
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
        batch.draw(abilityIcon, positionX, positionY, width, height);
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);
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
