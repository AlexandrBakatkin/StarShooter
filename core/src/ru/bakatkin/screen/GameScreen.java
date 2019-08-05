package ru.bakatkin.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.bakatkin.base.BaseScreen;
import ru.bakatkin.math.Rect;
import ru.bakatkin.pool.BulletPool;
import ru.bakatkin.pool.EnemyPool;
import ru.bakatkin.pool.ExplosionPool;
import ru.bakatkin.sprite.Background;
import ru.bakatkin.sprite.Bullet;
import ru.bakatkin.sprite.EnemyShip;
import ru.bakatkin.sprite.GameOver;
import ru.bakatkin.sprite.NewGameButton;
import ru.bakatkin.sprite.SpaceShip;
import ru.bakatkin.sprite.Star;
import ru.bakatkin.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star[] stars;
    private int STAR_COUNT = 64;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyGenerator enemyGenerator;
    private ExplosionPool explosionPool;
    private Sound explosionSound;

    private SpaceShip spaceShip;
    private GameOver gameOver;
    private NewGameButton newGameButton;

    private enum State {PLAYING, PAUSED, GAME_OVER}
    private State state = State.PLAYING;
    private State prevState = State.PLAYING;

    private Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        bg = new Texture("textures/bg.png");
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sound/explosion.wav"));

        background = new Background(new TextureRegion(bg));
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }
        explosionPool = new ExplosionPool(atlas, explosionSound);
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyGenerator = new EnemyGenerator(enemyPool, atlas, worldBounds);
        spaceShip = new SpaceShip(atlas, explosionPool, bulletPool);
        gameOver = new GameOver(atlas);
        newGameButton = new NewGameButton(atlas, game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyedSprites();
        draw();
    }

    @Override
    public void pause() {
        super.pause();
        prevState = state;
        state = State.PAUSED;
    }

    @Override
    public void resume() {
        super.resume();
        state = prevState;
    }

    private void checkCollisions() {
        if(state != State.PLAYING ){return;};
        for (EnemyShip enemyShip: enemyPool.getActiveObjects()
             ) {
            float minDist = enemyShip.getHalfHeight() + spaceShip.getHalfHeight();
            if(enemyShip.pos.dst(spaceShip.pos) < minDist/1.25){
                enemyShip.destroy();
                spaceShip.destroy();
                state = State.GAME_OVER;
            }

            for (Bullet bullet: bulletPool.getActiveObjects()
            ) {
                if (bullet.getOwner().equals(spaceShip)){
                    if(enemyShip.isBulletCollision(bullet)){
                        enemyShip.damage(spaceShip.getDamage());
                        bullet.destroy();
                    }
                } else {
                    if (spaceShip.isBulletCollision(bullet)){
                        spaceShip.damage(bullet.getDamage());
                        if (spaceShip.isDestroyed()){
                            state = State.GAME_OVER;
                        }
                        bullet.destroy();
                    }
                }
            }
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star: stars
        ) {
            star.resize(worldBounds);
        }
        spaceShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGameButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        spaceShip.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
        gameOver.dispose();
        newGameButton.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING){
            spaceShip.touchDown(touch, pointer ,button);
        }
        if (state == State.GAME_OVER){
            newGameButton.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING){
            spaceShip.touchUp(touch, pointer ,button);
        }
        if (state == State.GAME_OVER){
            newGameButton.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLAYING){
            spaceShip.keyDown(keycode);
        }
        return false;
    }

    private void update(float delta){
        for (Star star: stars
        ) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(state == State.PLAYING){
            spaceShip.update(delta);
            enemyGenerator.generate(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
        }
    }

    private void freeAllDestroyedSprites(){
        explosionPool.freeAllDestroyedActiveSprites();
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star: stars
        ) {
            star.draw(batch);
        }
        if(state == State.PLAYING){
            spaceShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        }
        if(state == State.GAME_OVER){
            gameOver.draw(batch);
            newGameButton.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }
}