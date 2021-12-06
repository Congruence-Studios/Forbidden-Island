package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.state.GameState;
import com.congruence.state.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreasureCardUI extends Actor {

    private Logger logger = LoggerFactory.getLogger(TreasureCardUI.class);

    private GameState state;

    private boolean hover;

    private boolean focused;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private String cardName;

    private Texture CardTexture;

    private Texture HoverTexture;

    public TreasureCardUI(
            GameState state,
            float positionX,
            float positionY,
            float height,
            float width,
            Texture CardTexture,
            String cardName
    ) {
        this.state = state;
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;
        this.CardTexture = CardTexture;

        HoverTexture = ForbiddenIsland.assetManager.get("treasure-deck/Treasure Card Hover.png", Texture.class);
        HoverTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        super.setBounds(positionX, positiveY, width, height);

        super.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //focused = !focused;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerHand.setSelectedCard(TreasureCardUI.this);
                if (cardName.equals("Helicopter")) {
                    boolean isWin = true;
                    boolean[] totalTreasures = new boolean[4];
                    for (Player p : state.getPlayers().values()) {
                        if (p.getTileX() != state.getFoolsLandingCoordinates().x || p.getTileY() != state.getFoolsLandingCoordinates().y) {
                            isWin = false;
                            break;
                        }
                        for (String treasure : p.getTreasuresAtHand()) {
                            if (treasure.equals("The Crystal of Fire")) {
                                totalTreasures[GameState.CRYSTAL_OF_FIRE] = true;
                            } else if (treasure.equals("The Earth Stone")) {
                                totalTreasures[GameState.EARTH_STONE] = true;
                            } else if (treasure.equals("The Ocean's Chalice")) {
                                totalTreasures[GameState.OCEANS_CHALICE] = true;
                            } else if (treasure.equals("The Statue of the Wind")) {
                                totalTreasures[GameState.STATUE_OF_THE_WIND] = true;
                            }
                        }
                    }
                    if (isWin && totalTreasures[0] && totalTreasures[1] && totalTreasures[2] && totalTreasures[3]) {
                        state.setGameEnd(true);
                        state.setGameResult(GameState.WIN);
                    }
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

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

        batch.draw(CardTexture, positionX, positiveY, width, height);
        if (focused || PlayerHand.getSelectedCard() == this) {
            batch.draw(HoverTexture, positionX, positiveY, width, height);
        } else if (hover) {
            batch.draw(HoverTexture, positionX, positiveY, width, height);
        }

    }

    public void selectNewCard () {
        PlayerHand.setSelectedCard(this);
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

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
