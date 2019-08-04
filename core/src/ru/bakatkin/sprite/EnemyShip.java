package ru.bakatkin.sprite;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.bakatkin.base.Ship;
import ru.bakatkin.math.Rect;
import ru.bakatkin.pool.BulletPool;

public class EnemyShip extends Ship {

    private enum State {DESCENT, FIGHT}
    private State state;

    private final float SPEED_FIGHT = 0.005f;
    private final float SPEED_DESCENT = 0.05f;

    public EnemyShip(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sound/laser.wav"));
        move = new Vector2();
        bulletVelocity = new Vector2();
        this.worldBounds = worldBounds;
        reloadInterval = 0.01f;
        state = State.DESCENT;
    }

    @Override
    public void update(float delta) {
        switch (state) {
            case DESCENT:
                if (getTop() <= worldBounds.getTop()) {
                    move(SPEED_DESCENT);
                    state = State.FIGHT;
                }
                move(SPEED_DESCENT);
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0f;
                    shot();
                }
                move(SPEED_FIGHT);
                if (getBottom() < worldBounds.getBottom()) {
                    destroy();
                }
                break;
        }
    }

    private void move(float speed) {
        pos.add(move.setLength(speed));
    }

    public void set(TextureRegion[] textureRegions,
                    Vector2 velocity,
                    TextureRegion bulletRegion,
                    float bulletHeight,
                    float bulletVelocityY,
                    int damage,
                    float reloadInterval,
                    float height,
                    int hp){
        this.regions = textureRegions;
        this.move = velocity;
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletVelocity.set(0, bulletVelocityY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
        this.hp = hp;
        state = State.DESCENT;
    }
}
