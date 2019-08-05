package ru.bakatkin.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.bakatkin.base.ScaledTouchButton;
import ru.bakatkin.math.Rect;
import ru.bakatkin.screen.GameScreen;
import ru.bakatkin.screen.MenuScreen;


public class NewGameButton extends ScaledTouchButton {

    private Game game;

    public NewGameButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("button_new_game"));
        this.game = game;
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        setBottom(worldBounds.getBottom() + 0.1f);
        setRight(worldBounds.getLeft() + 0.725f);
    }
}
