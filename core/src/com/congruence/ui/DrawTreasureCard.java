package com.congruence.ui;

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
import com.congruence.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class DrawTreasureCard extends Group {

    private static Logger logger = LoggerFactory.getLogger(DrawTreasureCard.class);

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture Background;

    private Texture COFTexture;

    private Texture SOTWTexture;

    private Texture OCTexture;

    private Texture ESTexture;

    private Texture SandbagCardTexture;

    private Texture HelicopterCardTexture;

    private Texture WatersRiseTexture;

    private ArrayList<ClaimButton> claimButtons;

    private CloseButton closeButton;

    public static int UNCLAIMED;

    public static int CLAIMED;

    private int cardState1;

    private int cardState2;

    private Observable observable;

    public DrawTreasureCard(
            float positionX,
            float positionY,
            float height,
            float width,
            GameState state,
            GameUI gameUI
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;
        this.state = state;

        super.setBounds(0, 0, 0, 0);

        Background = ForbiddenIsland.assetManager.get("custom-ui/draw-screen/Draw Treasure Cards Background.png", Texture.class);
        COFTexture = ForbiddenIsland.assetManager.get("treasure-deck/Crystal of Fire.png", Texture.class);
        SOTWTexture = ForbiddenIsland.assetManager.get("treasure-deck/Statue of the Wind.png", Texture.class);
        OCTexture = ForbiddenIsland.assetManager.get("treasure-deck/Ocean's Chalice.png", Texture.class);
        ESTexture = ForbiddenIsland.assetManager.get("treasure-deck/Earth Stone.png", Texture.class);
        SandbagCardTexture = ForbiddenIsland.assetManager.get("treasure-deck/Sandbag.png", Texture.class);
        HelicopterCardTexture = ForbiddenIsland.assetManager.get("treasure-deck/Helicopter.png", Texture.class);
        WatersRiseTexture = ForbiddenIsland.assetManager.get("treasure-deck/Waters Rise.png", Texture.class);

        Background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        COFTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        SOTWTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        OCTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        ESTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        SandbagCardTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        HelicopterCardTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        WatersRiseTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        float x = 0+(getWidth()/2)-5-HelicopterCardTexture.getWidth();
        float y = 0+(getHeight()*2/3)-HelicopterCardTexture.getHeight()/2f;
        logger.info(getWidth() + "");
        logger.info(HelicopterCardTexture.getWidth() + "");
        logger.info(x + "");
        claimButtons = new ArrayList<>();
        claimButtons.add(new ClaimButton(x, y - HelicopterCardTexture.getHeight()/2f - 10f, HelicopterCardTexture.getWidth(), HelicopterCardTexture.getWidth() * 10/16f ));
        claimButtons.add(new ClaimButton(x + HelicopterCardTexture.getWidth()+10, y - HelicopterCardTexture.getHeight()/2f - 10f, HelicopterCardTexture.getWidth(), HelicopterCardTexture.getWidth() * 10/16f ));
//
//        this.addActor(claimButtons.get(0));
//        this.addActor(claimButtons.get(1));

        claimButtons.get(0).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Player e = state.getPlayers().get(state.getPlayerOrder().get(state.getTurnNumber()));
                if (e.getCardsAtHand().size() < 5 && !claimButtons.get(0).isClaimed()) {
                    claimButtons.get(0).setClaimed(true);
                    cardState1 = CLAIMED;
                    e.addTreasureToHand(state.getCurrentDrawnTreasureCards().get(0));
                }
            }
        });
        claimButtons.get(1).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Player e = state.getPlayers().get(state.getPlayerOrder().get(state.getTurnNumber()));
                if (e.getCardsAtHand().size() < 5 && !claimButtons.get(1).isClaimed()) {
                    claimButtons.get(1).setClaimed(true);
                    cardState2 = CLAIMED;
                    e.addTreasureToHand(state.getCurrentDrawnTreasureCards().get(1));
                }
            }
        });

        closeButton = new CloseButton(
                getWidth() - 50,
                getHeight() - 50,
                40,
                40
        );
        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setOpen(false, observable);
                observable.onFinished();
                state.setDrawingTreasureCards(false);

                if (!claimButtons.get(0).isClaimed()) {
                    state.getTreasureCardDiscardDeck().add(state.getCurrentDrawnTreasureCards().get(0));
                }
                if (!claimButtons.get(1).isClaimed()) {
                    state.getTreasureCardDiscardDeck().add(state.getCurrentDrawnTreasureCards().get(1));
                }
            }
        });
        this.addActor(closeButton);

        super.setBounds(0, 0, 0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isOpen) {
            batch.end();
            batch.begin();
            batch.draw(Background, getPositionX(), getPositiveY(), getWidth(), getHeight());
            float x = positionX+(getWidth()/2)-5-HelicopterCardTexture.getWidth();
            float y = positiveY+(getHeight()*2/3)-HelicopterCardTexture.getHeight()/2f;
            if (state.getCurrentDrawnTreasureCards() != null) {
                for (TreasureCard e : state.getCurrentDrawnTreasureCards()) {
                    if (e.getCardType() == TreasureCard.HELICOPTER_CARD) {
                        batch.draw(HelicopterCardTexture, x, y);
                    }
                    else if (e.getCardType() == TreasureCard.SANDBAG_CARD) {
                        batch.draw(SandbagCardTexture, x, y);
                    }
                    else if (e.getCardType() == TreasureCard.WATERS_RISE_CARD) {
                        batch.draw(WatersRiseTexture, x, y);
                    }
                    else if (e.getName().equals("Ocean's Chalice")) {
                        batch.draw(OCTexture, x, y);
                    }
                    else if (e.getName().equals("Statue of the Wind")) {
                        batch.draw(SOTWTexture, x, y);
                    }
                    else if (e.getName().equals("Earth Stone")) {
                        batch.draw(ESTexture, x, y);
                    }
                    else if (e.getName().equals("Crystal of Fire")) {
                        batch.draw(COFTexture, x, y);
                    }
                    //batch.draw(ClaimButton, x, y - HelicopterCardTexture.getHeight()/2f - 10f, HelicopterCardTexture.getWidth(), HelicopterCardTexture.getWidth() * 10/16f);
                    //logger.info("X" + x);
                    //logger.info("Y" + (y - HelicopterCardTexture.getHeight()/2f - 10f));
                    //logger.info("W" + (HelicopterCardTexture.getWidth()));
                    //logger.info("H" + (HelicopterCardTexture.getWidth() * 10/16f));
                    x += HelicopterCardTexture.getWidth()+10;
                }
            }
            super.draw(batch, parentAlpha);
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        super.setBounds(0, 0, 0, 0);
        float x = 0+(getWidth()/2)-5-HelicopterCardTexture.getWidth();
        float y = 0+(getHeight()*2/3)-HelicopterCardTexture.getHeight()/2f;
        claimButtons.get(0).setPositionX(x);
        claimButtons.get(0).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(0).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(0).setHeight(HelicopterCardTexture.getWidth() * 10/16f);
        claimButtons.get(1).setPositionX(x + HelicopterCardTexture.getWidth()+10);
        claimButtons.get(1).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(1).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(1).setHeight(HelicopterCardTexture.getWidth() * 10/16f);
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
        super.setBounds(0, 0, 0, 0);
        float x = 0+(getWidth()/2)-5-HelicopterCardTexture.getWidth();
        float y = 0+(getHeight()*2/3)-HelicopterCardTexture.getHeight()/2f;
        claimButtons.get(0).setPositionX(x);
        claimButtons.get(0).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(0).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(0).setHeight(HelicopterCardTexture.getWidth() * 10/16f);
        claimButtons.get(1).setPositionX(x + HelicopterCardTexture.getWidth()+10);
        claimButtons.get(1).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(1).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(1).setHeight(HelicopterCardTexture.getWidth() * 10/16f);
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        super.setBounds(0, 0, 0, 0);
        float x = 0+(getWidth()/2)-5-HelicopterCardTexture.getWidth();
        float y = 0+(getHeight()*2/3)-HelicopterCardTexture.getHeight()/2f;
        claimButtons.get(0).setPositionX(x);
        claimButtons.get(0).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(0).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(0).setHeight(HelicopterCardTexture.getWidth() * 10/16f);
        claimButtons.get(1).setPositionX(x + HelicopterCardTexture.getWidth()+10);
        claimButtons.get(1).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(1).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(1).setHeight(HelicopterCardTexture.getWidth() * 10/16f);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        super.setBounds(0, 0, 0, 0);
        float x = 0+(getWidth()/2)-5-HelicopterCardTexture.getWidth();
        float y = 0+(getHeight()*2/3)-HelicopterCardTexture.getHeight()/2f;
        claimButtons.get(0).setPositionX(x);
        claimButtons.get(0).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(0).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(0).setHeight(HelicopterCardTexture.getWidth() * 10/16f);
        claimButtons.get(1).setPositionX(x + HelicopterCardTexture.getWidth()+10);
        claimButtons.get(1).setPositiveY(y - HelicopterCardTexture.getHeight()/2f - 10f);
        claimButtons.get(1).setWidth(HelicopterCardTexture.getWidth());
        claimButtons.get(1).setHeight(HelicopterCardTexture.getWidth() * 10/16f);

        closeButton.setPositionX(getWidth()-50);
        closeButton.setPositiveY(getHeight()-50);
        closeButton.setWidth(40);
        closeButton.setHeight(40);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open, Observable observable) {
        isOpen = open;
        this.observable = observable;
        if (isOpen) {
            super.setBounds(positionX, positiveY, width, height);

            if (state.getCurrentDrawnTreasureCards().get(0).getCardType() != TreasureCard.WATERS_RISE_CARD) {
                super.addActor(claimButtons.get(0));
            }
            else {
                super.removeActor(claimButtons.get(0));
                state.setWaterHeight(state.getWaterHeight()+1);
                if (state.getWaterHeight() == 10) {
                    state.setGameEnd(true);
                    state.setGameResult(GameState.WATER_METER_FULL);
                }
            }
            if (state.getCurrentDrawnTreasureCards().get(1).getCardType() != TreasureCard.WATERS_RISE_CARD) {
                super.addActor(claimButtons.get(1));
            }
            else {
                super.removeActor(claimButtons.get(1));
                state.setWaterHeight(state.getWaterHeight()+1);
                if (state.getWaterHeight() == 10) {
                    state.setGameEnd(true);
                    state.setGameResult(GameState.WATER_METER_FULL);
                }
            }

            claimButtons.get(0).setClaimed(false);
            claimButtons.get(1).setClaimed(false);
            cardState1 = UNCLAIMED;
            cardState2 = UNCLAIMED;

        }
        else {
            super.setBounds(0, 0, 0, 0);
        }
    }
}
