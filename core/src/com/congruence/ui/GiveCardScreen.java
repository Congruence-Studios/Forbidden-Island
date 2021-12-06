package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.congruence.ForbiddenIsland;
import com.congruence.state.*;
import com.congruence.util.Observable;

import java.util.ArrayList;
import java.util.HashMap;

public class GiveCardScreen extends Group {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture Background;

    ArrayList<GiveDialogButtons> buttons;

    private ArrayList<Player> players;

    private Observable observable;

    public GiveCardScreen(
            float positionX,
            float positionY,
            float height,
            float width,
            GameState state
    ) {
        this.positionX = positionX;
        this.positiveY = positionY;
        this.height = height;
        this.width = width;
        this.state = state;

        super.setBounds(0, 0, 0, 0);

        Background = ForbiddenIsland.assetManager.get("custom-ui/draw-screen/Draw Flood Cards Background.png", Texture.class);

        Background.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        players = new ArrayList<>();
        players.addAll(state.getPlayers().values());
        players.remove(state.getPlayers().get(state.getPlayerOrder().get(state.getTurnNumber())));

        buttons = new ArrayList<>();
        float cardHeight = getHeight() * 1/3f;
        float cardWidth = cardHeight * 601/376f;
        float x = 0+(getWidth()/2)-(players.size() > 1 ? 5 : 0)-(cardWidth * players.size() * 0.5f);
        float y = 0+(getHeight()/2)-cardHeight/2f;
        for (Player e: players) {
            GiveDialogButtons b = new GiveDialogButtons(x, y, cardHeight, cardWidth, e.getPlayerName());
            buttons.add(b);
            x += cardWidth+10;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            batch.draw(Background, getPositionX(), getPositiveY(), getWidth(), getHeight());
            super.draw(batch, parentAlpha);
        }
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositiveY() {
        return positiveY;
    }

    public void setPositiveY(float positiveY) {
        this.positiveY = positiveY;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        float cardHeight = getHeight() * 1/3f;
        float cardWidth = cardHeight * 601/376f;
        float x = 0+(getWidth()/2)-(players.size() > 1 ? 5 : 0)-(cardWidth * players.size() * 0.5f);
        float y = 0+(getHeight()/2)-cardHeight/2f;
        for (GiveDialogButtons e: buttons) {
            e.setPositionX(x);
            e.setPositiveY(y);
            e.setWidth(cardWidth);
            e.setHeight(cardHeight);
            x += cardWidth+10;
        }
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        float cardHeight = getHeight() * 1/3f;
        float cardWidth = cardHeight * 601/376f;
        float x = 0+(getWidth()/2)-(players.size() > 1 ? 5 : 0)-(cardWidth * players.size() * 0.5f);
        float y = 0+(getHeight()/2)-cardHeight/2f;
        for (GiveDialogButtons e: buttons) {
            e.setPositionX(x);
            e.setPositiveY(y);
            e.setWidth(cardWidth);
            e.setHeight(cardHeight);
            x += cardWidth+10;
        }

    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open, Observable observable) {
        isOpen = open;
        this.observable = observable;
        if (isOpen) {

            players = new ArrayList<>();
            players.addAll(state.getPlayers().values());
            players.remove(state.getPlayers().get(state.getPlayerOrder().get(state.getTurnNumber())));

            buttons = new ArrayList<>();
            float cardHeight = getHeight() * 1/3f;
            float cardWidth = cardHeight * 601/376f;
            float x = 0+(getWidth()/2)-(players.size() > 1 ? 5 : 0)-(cardWidth * players.size() * 0.5f);
            float y = 0+(getHeight()/2)-cardHeight/2f;
            for (int i = 0; i < players.size(); i++) {
                Player e = players.get(i);
                GiveDialogButtons b = new GiveDialogButtons(x, y, cardHeight, cardWidth, e.getPlayerName());
                buttons.add(b);
                addActor(b);
                x += cardWidth+10;
                b.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Player currentPlayer = state.getPlayers().get(state.getPlayerOrder().get(state.getTurnNumber()));
                        TreasureCard treasureCard = currentPlayer.getCardsAtHand().get(state.getTreasureCardUI().getPosition());
                        currentPlayer.removeTreasureFromHand(state.getTreasureCardUI().getPosition());
                        e.addTreasureToHand(treasureCard);
                        observable.onFinished();
                    }
                });
            }

            super.setBounds(positionX, positiveY, width, height);
        }
        else {
            for (GiveDialogButtons e : buttons) {
                removeActor(e);
            }
            super.setBounds(0, 0, 0, 0);
        }
    }

    public Observable getObservable() {
        return observable;
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }
}
