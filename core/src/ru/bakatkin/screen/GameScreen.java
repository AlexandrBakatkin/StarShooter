package ru.bakatkin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.bakatkin.base.BaseScreen;
import ru.bakatkin.math.Rect;
import ru.bakatkin.sprite.Background;
import ru.bakatkin.sprite.SpaceShip;
import ru.bakatkin.sprite.Star;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star[] stars;
    private int STAR_COUNT = 64;

    private SpaceShip spaceShip;

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }
        spaceShip = new SpaceShip(atlas);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
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
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        bg.dispose();
    }

    private void update(float delta){
        for (Star star: stars
        ) {
            star.update(delta);
        }
        spaceShip.update(delta);
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
        spaceShip.draw(batch);

        batch.end();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        spaceShip.touchDown(touch, pointer ,button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        spaceShip.touchUp(touch, pointer ,button);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        spaceShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        spaceShip.keyUp(keycode);
        return false;
    }
}
