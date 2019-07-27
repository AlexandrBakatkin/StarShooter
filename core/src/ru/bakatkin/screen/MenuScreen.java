package ru.bakatkin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.bakatkin.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Vector2 touch;
    private Vector2 pos;
    private Vector2 move;
    private final float SPEED = 0.01f;                //Задаем скорость перемещения космического корабля

    @Override
    public void show() {
        super.show();
        img = new Texture("spaceship.png");
        touch = new Vector2();
        pos = new Vector2();
        move = new Vector2();
        pos.set(0f, -0.4f);
        touch.set(pos);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        move();

        batch.draw(img, pos.x, pos.y, 0.1f, 0.1f);
        batch.end();
    }

    private void move() {
        Vector2 dest = new Vector2();           //Вводим новый вектор, который отслеживает расстояние от корабля до точки назначения
        dest.set(pos);
        dest.sub(touch);

        if(dest.len() > SPEED){
            pos.sub(move);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        float x;
        float y;

        x = pos.x;
        y = pos.y;

        switch (keycode){
            case 29:
                pos.set(x - 0.01f, y);
                break;
            case 32:
                pos.set(x + 0.01f, y);
                break;
            case 51:
                pos.set(x, y + 0.01f);
                break;
            case 47:
                pos.set(x, y - 0.01f);
                break;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch = touch;
        move.set(pos);
        move.sub(touch);
        move.setLength(SPEED);
        return false;
    }
}