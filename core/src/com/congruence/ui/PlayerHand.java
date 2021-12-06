package com.congruence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.state.GameState;
import com.congruence.state.Player;
import com.congruence.state.TreasureCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PlayerHand extends Group {
    private static final Logger logger = LoggerFactory.getLogger(PlayerHand.class);

    private GameState state;

    private float positionX;

    private float positionY;

    private float height;

    private float width;

    private Player player;

    private boolean hovered;

    private boolean focused;

    private Texture COFTexture;

    private Texture SOTWTexture;

    private Texture OCTexture;

    private Texture ESTexture;

    private Texture SandbagCardTexture;

    private Texture HelicopterCardTexture;

    private boolean[] collectedTreasures;

    private static TreasureCardUI selectedCard = null;

    private ArrayList<TreasureCardUI> treasureCardUIS;

    public PlayerHand(
            GameState state,
            float positionX,
            float positionY,
            float width,
            float height,
            Player player
    ) {
        this.state = state;
        this.positionX = positionX;
        this.positionY = positionY;
        this.player = player;
        this.width = width;
        this.height = height;

        COFTexture = ForbiddenIsland.assetManager.get("treasure-deck/Crystal of Fire.png", Texture.class);
        SOTWTexture = ForbiddenIsland.assetManager.get("treasure-deck/Statue of the Wind.png", Texture.class);
        OCTexture = ForbiddenIsland.assetManager.get("treasure-deck/Ocean's Chalice.png", Texture.class);
        ESTexture = ForbiddenIsland.assetManager.get("treasure-deck/Earth Stone.png", Texture.class);
        SandbagCardTexture = ForbiddenIsland.assetManager.get("treasure-deck/Sandbag.png", Texture.class);
        HelicopterCardTexture = ForbiddenIsland.assetManager.get("treasure-deck/Helicopter.png", Texture.class);

        COFTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        SOTWTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        OCTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        ESTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        SandbagCardTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        HelicopterCardTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        treasureCardUIS = new ArrayList<>();

        float cardX = 0;
        float cardY = 0;
        float cardHeight = height;
        float cardWidth = height * 128/188f;
        if (player.getCardsAtHand() != null) {
            for (int i = 0; i < player.getCardsAtHand().size(); i++) {
                TreasureCard e = player.getCardsAtHand().get(i);
                if (e.getCardType() == TreasureCard.HELICOPTER_CARD) {
                    treasureCardUIS.add(new TreasureCardUI(
                            state,
                            cardX,
                            cardY,
                            cardHeight,
                            cardWidth,
                            HelicopterCardTexture,
                            "Helicopter",
                            i
                    ));
                }
                else if (e.getCardType() == TreasureCard.SANDBAG_CARD) {
                    treasureCardUIS.add(new TreasureCardUI(
                            state,
                            cardX,
                            cardY,
                            cardHeight,
                            cardWidth,
                            SandbagCardTexture,
                            "Sandbag",
                            i
                    ));
                }
                else if (e.getName().equals("Ocean's Chalice")) {
                    treasureCardUIS.add(new TreasureCardUI(
                            state,
                            cardX,
                            cardY,
                            cardHeight,
                            cardWidth,
                            OCTexture,
                            "Ocean's Chalice",
                            i
                    ));
                }
                else if (e.getName().equals("Statue of the Wind")) {
                    treasureCardUIS.add(new TreasureCardUI(
                            state,
                            cardX,
                            cardY,
                            cardHeight,
                            cardWidth,
                            SOTWTexture,
                            "Statue of the Wind",
                            i
                    ));
                }
                else if (e.getName().equals("Earth Stone")) {
                    treasureCardUIS.add(new TreasureCardUI(
                            state,
                            cardX,
                            cardY,
                            cardHeight,
                            cardWidth,
                            ESTexture,
                            "Earth Stone",
                            i
                    ));
                }
                else if (e.getName().equals("Crystal of Fire")) {
                    treasureCardUIS.add(new TreasureCardUI(
                            state,
                            cardX,
                            cardY,
                            cardHeight,
                            cardWidth,
                            COFTexture,
                            "Crystal of Fire",
                            i
                    ));
                }
                cardX += cardWidth/2f;
            }
        }

        for (int i = 0; i < treasureCardUIS.size(); i++) {
            TreasureCardUI e = treasureCardUIS.get(i);
            super.addActor(e);
            e.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (e.isDiscardMode()) {
                        player.removeTreasureFromHand(e.getPosition());
                    }
                    else {
                        state.setTreasureCardUI(e);
                    }
                }
            });
        }

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
                //focused = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                focused = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //logger.info("enter: X: " + String.format("%f", x) + ", Y: " + String.format("%f", y));
                //hovered = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hovered = false;
            }
        });

        player.getCardHandListeners().add(new Player.CardHandListener() {
            @Override
            public void onAdd(TreasureCard treasureCard, int index) {
                float cardX = 0;
                float cardY = 0;
                float cardHeight = PlayerHand.this.height;
                float cardWidth = PlayerHand.this.height * 128/188f;
                if (player.getCardsAtHand() != null) {
                    for (TreasureCardUI e : treasureCardUIS) {
                        e.setWidth(cardWidth);
                        e.setHeight(cardHeight);
                        e.setPositionX(cardX);
                        e.setPositiveY(cardY);
                        cardX += cardWidth/2f;
                        if (e.equals(selectedCard)) {
                        }
                    }
                }
                TreasureCardUI treasureCardUI = new TreasureCardUI(
                        state,
                        cardX,
                        cardY,
                        cardHeight,
                        cardWidth,
                        getTexture(treasureCard),
                        treasureCard.getName(),
                        player.getCardsAtHand().size()-1
                );
                PlayerHand.super.addActor(treasureCardUI);
                treasureCardUIS.add(treasureCardUI);
                treasureCardUI.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (treasureCardUI.isDiscardMode()) {
                            PlayerHand.super.removeActor(treasureCardUI);
                            player.getCardsAtHand().remove(treasureCardUI.getPosition());
                        }
                    }
                });
            }

            @Override
            public void onRemove(TreasureCard removedCard, int index) {
                PlayerHand.super.removeActor(treasureCardUIS.get(index));
                treasureCardUIS.remove(treasureCardUIS.get(index).getPosition());
                state.getTreasureCardDiscardDeck().add(removedCard);
                float cardX = 0;
                float cardY = 0;
                float cardHeight = PlayerHand.this.height;
                float cardWidth = PlayerHand.this.height * 128/188f;
                if (player.getCardsAtHand() != null) {
                    for (int i = 0; i < treasureCardUIS.size(); i++) {
                        TreasureCardUI e = treasureCardUIS.get(i);
                        e.setWidth(cardWidth);
                        e.setHeight(cardHeight);
                        e.setPositionX(cardX);
                        e.setPositiveY(cardY);
                        cardX += cardWidth/2f;
                        if (e.equals(selectedCard)) {
                        }
                        e.setPosition(i);
                    }
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }


    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);

        float cardX = 0;
        float cardY = 0;
        float cardHeight = height;
        float cardWidth = height * 128/188f;
        if (player.getCardsAtHand() != null) {
            for (TreasureCardUI e : treasureCardUIS) {
                e.setWidth(cardWidth);
                e.setHeight(cardHeight);
                e.setPositionX(cardX);
                e.setPositiveY(cardY);
                cardX += cardWidth/2f;
            }
        }
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(this.positionX, this.positionY, this.width, this.height);

        float cardX = 0;
        float cardY = 0;
        float cardHeight = height;
        float cardWidth = height * 128/188f;
        if (player.getCardsAtHand() != null) {
            for (TreasureCardUI e : treasureCardUIS) {
                e.setWidth(cardWidth);
                e.setHeight(cardHeight);
                e.setPositionX(cardX);
                e.setPositiveY(cardY);
                cardX += cardWidth/2f;
            }
        }
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

    public static TreasureCardUI getSelectedCard() {
        return selectedCard;
    }

    public static void setSelectedCard(TreasureCardUI newCard) {
        selectedCard = newCard;
    }

    public Texture getTexture(TreasureCard e) {
        if (e.getCardType() == TreasureCard.HELICOPTER_CARD) {
            return HelicopterCardTexture;
        }
        else if (e.getCardType() == TreasureCard.SANDBAG_CARD) {
            return SandbagCardTexture;
        }
        else if (e.getName().equals("Ocean's Chalice")) {
            return OCTexture;
        }
        else if (e.getName().equals("Statue of the Wind")) {
            return SOTWTexture;
        }
        else if (e.getName().equals("Earth Stone")) {
            return ESTexture;
        }
        else if (e.getName().equals("Crystal of Fire")) {
            return COFTexture;
        }
        else return null;
    }
}
