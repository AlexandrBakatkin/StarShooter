package ru.bakatkin.sprite;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.bakatkin.base.Ship;
import ru.bakatkin.math.Rect;
import ru.bakatkin.pool.BulletPool;

public class EnemyShip extends Ship {

    private final float SPEED = 0.0005f;

    public EnemyShip(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sound/laser.wav"));
        move = new Vector2();
        bulletVelocity = new Vector2();
        this.worldBounds = worldBounds;
        reloadInterval = 0.01f;
    }

    @Override
    public void update(float delta) {
       move();
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval){
            reloadTimer = 0f;
            shot();
            System.out.println("Shot");
        }
    }

    private void move() {
        pos.add(move.setLength(SPEED));
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
    }
}
