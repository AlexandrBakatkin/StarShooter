package ru.bakatkin.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.bakatkin.base.Sprite;
import ru.bakatkin.math.Rect;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.Input.Keys.X;

public class SpaceShip extends Sprite {

    private Vector2 touch = new Vector2();
    private Vector2 move = new Vector2();
    private final float SPEED = 0.01f;

    private Rect worldBounds;

    public SpaceShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + 0.15f);
        setRight(worldBounds.getLeft() + 0.5f);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch = touch;
        move.set(pos);
        move.sub(touch);
        move.setLength(SPEED);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public void update(float delta) {
        if (getRight() > worldBounds.getRight()) {setRight(worldBounds.getRight()) ; stop();}
        if (getLeft() < worldBounds.getLeft()){setLeft(worldBounds.getLeft()); stop();}
        if (getBottom() < worldBounds.getBottom() + 0.05f){setBottom(worldBounds.getBottom() + 0.05f); stop();}
        if (getTop() > worldBounds.getBottom() + 0.4f){setTop(worldBounds.getBottom() + 0.4f); stop();}
        move();
    }

    private void move() {
        Vector2 dest = new Vector2();
        dest.set(pos);
        dest.sub(touch);
        if(dest.len() > SPEED){
            pos.sub(move);
        } else {
            pos.set(touch);
        }
    }

    public boolean keyDown(int keycode) {
        float x;
        float y;

        x = pos.x;
        y = pos.y;

        switch (keycode){
            case A:
                touch.set(-1f, y);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;
            case D:
                touch.set(1f, y);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;
            case W:
                touch.set(x, 1f);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;
            case S:
                touch.set(x, -1f);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;

            case X:
                stop();
        }
        return false;
    }

    private void stop(){
        touch.set(pos);
    }
}