package ru.bakatkin.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.bakatkin.base.BaseScreen;
import ru.bakatkin.math.Rect;
import ru.bakatkin.sprite.Background;
import ru.bakatkin.sprite.ButtonExit;
import ru.bakatkin.sprite.ButtonPlay;
import ru.bakatkin.sprite.Star;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.HOME;

public class MenuScreen extends BaseScreen {

    private Game game;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star[] stars;
    private int STAR_COUNT = 256;
    private Music musicMenu;

    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;

    private Texture img;


    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        img = new Texture("spaceship.png");
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }
        buttonExit = new ButtonExit(atlas);
        buttonPlay = new ButtonPlay(atlas, game);
        musicMenu = Gdx.audio.newMusic(Gdx.files.internal("sound/a31df44c3944ea6.mp3"));
        musicMenu.play();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);

        for (Star star: stars
             ) {
            star.resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        bg.dispose();
        img.dispose();
        musicMenu.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        return false;
    }

    private void update (float delta){

        for (Star star: stars
        ) {
            star.update(delta);
        }
    }

    private void draw (){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);

        for (Star star: stars
        ) {
            star.draw(batch);
        }

        buttonExit.draw(batch);
        buttonPlay.draw(batch);

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case ENTER:
                game.setScreen(new GameScreen());
            case ESCAPE:
                Gdx.app.exit();
            case HOME:
                Gdx.app.exit();
        }
        return false;
    }
}