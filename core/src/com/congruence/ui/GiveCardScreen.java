package com.congruence.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.congruence.ForbiddenIsland;
import com.congruence.state.FloodCard;
import com.congruence.state.GameState;
import com.congruence.state.Player;
import com.congruence.state.Resources;
import com.congruence.util.Observable;

import java.util.ArrayList;
import java.util.HashMap;

public class GiveCardScreen extends Actor {

    private final GameState state;

    public boolean isOpen = false;

    private float positionX;

    private float positiveY;

    private float height;

    private float width;

    private Texture Background;

    private Texture Diver;

    private Texture Engineer;

    private Texture Explorer;

    private Texture Messenger;

    private Texture Navigator;

    private Texture Pilot;

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

        Diver = ForbiddenIsland.assetManager.get("ability-icon/Diver Icon.png", Texture.class);
        Engineer = ForbiddenIsland.assetManager.get("ability-icon/Engineer Icon.png", Texture.class);
        Explorer = ForbiddenIsland.assetManager.get("ability-icon/Explorer Icon.png", Texture.class);
        Messenger = ForbiddenIsland.assetManager.get("ability-icon/Messenger Icon.png", Texture.class);
        Navigator = ForbiddenIsland.assetManager.get("ability-icon/Navigator Icon.png", Texture.class);
        Pilot = ForbiddenIsland.assetManager.get("ability-icon/Pilot Icon.png", Texture.class);

        Diver.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        Engineer.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        Explorer.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        Messenger.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        Navigator.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        Pilot.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();

        if (isOpen) {
            batch.draw(Background, getPositionX(), getPositiveY(), getWidth(), getHeight());
            float x = positionX+(getWidth()/2);
            float y = positiveY+(getHeight()/2);
            if (players.size() == 1) {
                x -= Diver.getWidth()/2f;
            } else if (players.size() == 2) {
                x -= 5 + Diver.getWidth();
            } else if (players.size() == 3) {
                x -= 10 + Diver.getWidth() + Diver.getWidth()/2f;
            }
            for (Player p : players) {
                if (p.getAbility() == Player.DIVER) {
                    batch.draw(Diver, x, y, Diver.getWidth(), Diver.getHeight());
                } else if (p.getAbility() == Player.ENGINEER) {
                    batch.draw(Engineer, x, y, Engineer.getWidth(), Engineer.getHeight());
                } else if (p.getAbility() == Player.EXPLORER) {
                    batch.draw(Explorer, x, y, Explorer.getWidth(), Explorer.getHeight());
                } else if (p.getAbility() == Player.MESSENGER) {
                    batch.draw(Messenger, x, y, Messenger.getWidth(), Messenger.getHeight());
                } else if (p.getAbility() == Player.NAVIGATOR) {
                    batch.draw(Navigator, x, y, Navigator.getWidth(), Navigator.getHeight());
                } else if (p.getAbility() == Player.PILOT) {
                    batch.draw(Pilot, x, y, Pilot.getWidth(), Pilot.getHeight());
                }
            }
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
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open, Observable observable) {
        isOpen = open;
        this.observable = observable;
        if (isOpen) {
            super.setBounds(positionX, positiveY, width, height);
        }
        else {
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