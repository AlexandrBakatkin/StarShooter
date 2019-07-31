package ru.bakatkin.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.bakatkin.base.Sprite;
import ru.bakatkin.math.Rect;
import ru.bakatkin.pool.BulletPool;

import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.X;

public class SpaceShip extends Sprite {

    private Vector2 touch = new Vector2();
    private Vector2 move = new Vector2();
    private final float SPEED = 0.01f;

    private TextureRegion bulletRegion;
    private Vector2 bulletVelocity = new Vector2(0f, 0.5f);
    private Sound bulletSound;

    private Rect worldBounds;
    private BulletPool bulletPool;

    private float reloadInterval;
    private float reloadTimer;

    public SpaceShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(0.1f);
        bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletPool = bulletPool;
        reloadInterval = 0.2f;
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sound/bullet.wav"));
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
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval){
            reloadTimer = 0f;
            shot();
        }
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
            case LEFT:
                touch.set(-1f, y);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;
            case RIGHT:
                touch.set(1f, y);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;
            case UP:
                touch.set(x, 1f);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;
            case DOWN:
                touch.set(x, -1f);
                move.set(pos);
                move.sub(touch);
                move.setLength(SPEED * 0.7f);
                break;
            case X:
                stop();
                break;
            case SPACE:
                shot();
                break;
        }
        return false;
    }

    private void stop(){
        move.setZero();
    }

    private void shot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletVelocity, 0.01f, worldBounds, 1);
        long id = bulletSound.play(0.05f);
    }
}