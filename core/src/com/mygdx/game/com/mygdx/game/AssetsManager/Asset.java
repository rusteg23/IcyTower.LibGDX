package com.mygdx.game.com.mygdx.game.AssetsManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by pawel_000 on 2016-06-11.
 */
public class Asset implements Disposable {

    public final AssetManager manager = new AssetManager();

    public void loadTextures() {
        manager.load("assets/mario.png", Texture.class);
        manager.load("assets/cloud.png", Texture.class);
        manager.load("assets/font.png", Texture.class);
        manager.load("assets/moutain.png", Texture.class);
        manager.load("assets/floor.png", Texture.class);
        manager.load("assets/logo.png", Texture.class);

        manager.load("assets/brick.png", Texture.class);
        manager.load("assets/brick_blue.png", Texture.class);
        manager.load("assets/brick_green.png", Texture.class);
        manager.load("assets/brick_grey.png", Texture.class);
        manager.load("assets/questionMark.png", Texture.class);

        manager.load("assets/hedgehog.png", Texture.class);

        manager.load("assets/mob_1_red.png", Texture.class);
        manager.load("assets/mob_1_blue.png", Texture.class);
        manager.load("assets/mob_1_grey.png", Texture.class);

        manager.load("assets/deal_black.png", Texture.class);
        manager.load("assets/deal_pink.png", Texture.class);
        manager.load("assets/deal_blue.png", Texture.class);
        manager.load("assets/deal_grey.png", Texture.class);

        manager.load("assets/mob_2_green.png", Texture.class);
        manager.load("assets/mob_2_red.png", Texture.class);
        manager.load("assets/mob_2_blue.png", Texture.class);

        manager.load("assets/mob_3_black.png", Texture.class);
        manager.load("assets/mob_3_blue.png", Texture.class);
        manager.load("assets/mob_3_grey.png", Texture.class);

        manager.load("assets/pipe.png", Texture.class);

        manager.load("assets/plant.png", Texture.class);

        manager.load("assets/bomb_black.png", Texture.class);
        manager.load("assets/bomb_blue.png", Texture.class);
        manager.load("assets/bomb_grey.png", Texture.class);

        manager.load("assets/bird_red.png", Texture.class);
        manager.load("assets/bird_blue.png", Texture.class);
        manager.load("assets/bird_green.png", Texture.class);

        manager.load("assets/flying_turtle_dark.png", Texture.class);
        manager.load("assets/flying_turtle_green.png", Texture.class);
        manager.load("assets/flying_turtle_grey.png", Texture.class);
        manager.load("assets/flying_turtle_red.png", Texture.class);

        manager.load("assets/medusa_pink.png", Texture.class);
        manager.load("assets/medusa_blue.png", Texture.class);
        manager.load("assets/medusa_white.png", Texture.class);

        manager.load("assets/cloud_green.png", Texture.class);
        manager.load("assets/cloud_blue.png", Texture.class);
        manager.load("assets/cloud_white.png", Texture.class);

        manager.load("assets/dragon_blue.png", Texture.class);
        manager.load("assets/dragon_green.png", Texture.class);
        manager.load("assets/dragon_grey.png", Texture.class);

        manager.load("assets/bigturtle_green.png", Texture.class);
        manager.load("assets/bigturtle_blue.png", Texture.class);
        manager.load("assets/bigturtle_grey.png", Texture.class);
    }

    public void loadSounds() {
        manager.load("assets/jump.wav", Music.class);
    }

    public void dispose() {
        manager.dispose();
    }
}
