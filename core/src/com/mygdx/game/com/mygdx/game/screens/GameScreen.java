package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.AssetsManager.Asset;
import com.mygdx.game.com.mygdx.game.controllers.Enemy;
import com.mygdx.game.com.mygdx.game.controllers.FlyingObjectControler;
import com.mygdx.game.com.mygdx.game.controllers.ScoreControler;
import com.mygdx.game.com.mygdx.game.enemies.Turtle;
import com.mygdx.game.com.mygdx.game.enemies.Worm;
import com.mygdx.game.com.mygdx.game.entities.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class GameScreen extends AbstractScreen {

    private static final String PAUSE_INFORMATION_BUTTON_TEXT = "PAUSE";
    private static final String PAUSE_BUTTON_TEXT = "P";
    private static final int HEIGHT_BETWEEN_PLATFORMS = 200;
    private static final float CAMERA_MOVEMENT_SPEED = 10;
    private static final int PLATFORMS = 12;
    private static float enemyTimer = 0;
    private static int POINTS_FOR_ENEMY = 0;
    private static int AMOUT_OF_ENEMIES = 0;
    private static int LEVEL_CLOUDS = 1;
    private static int LEVEL_PLATFORMS = 1;
    private float cameraVelocity = 0.0f;
    private int CLOUDS = 1;

    private boolean first = true;
    private boolean die = false;

    private BitmapFont font;
    private AnimatedImage anim;
    private Background background;
    private Texture logoTexture;

    private Label scoreLabel;
    private Label pauseLabel;
    private TextButton pauseButton;

    private Floor floor;
    private Player player;
    private MenuScreen menuScreen;
    private ScoreControler scoreControler;
    private FlyingObjectControler flyingObjectControler;

    private ArrayList<Platform> platformArray;
    private ArrayList<Cloud> cloudArray;
    private ArrayList<Enemy> enemyArray;
    private ArrayList<Enemy> removeEnemyArray;

    private Stage constantStage;

    public GameScreen(IcyTower game){
        super(game);

        assets = new Asset();
        assets.load();
        assets.manager.finishLoading();

        if (assets.manager.update())
            init();

        logoTexture = assets.manager.get("assets/logo.png", Texture.class);
    }

    public void init() {
        constantStage = new Stage(new StretchViewport(IcyTower.SCREEN_WIDTH, IcyTower.SCREEN_HEIGHT, cameraScore));

        initClouds();
        initPlatforms();
        initEnemy();

        initBackground();
        initFloor();
        initFont();

        initPlayer();
        initMenuScreen();
        initPauseLabel();

        initScoreControler();
        initScoreLabel();

        initPauseButton();

        initFlyingObjectControler();
    }

    private void initPauseButton() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        pauseButton = new TextButton(PAUSE_BUTTON_TEXT, style);
        pauseButton.setPosition(cameraScore.position.x + IcyTower.SCREEN_WIDTH / 3,
                cameraScore.position.y + IcyTower.SCREEN_HEIGHT / 3 + 40);

        pauseButton.addListener(new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setPause(!game.isPause());

                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void initScoreControler() {

        scoreControler = new ScoreControler();
    }

    private void initFlyingObjectControler() {

        flyingObjectControler = new FlyingObjectControler(assets, stage);
    }

    private void initScoreLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        scoreLabel = new Label("MARIO\n" + menuScreen.getPoints(scoreControler.getSCORE()), style);
        scoreLabel.setPosition(-90, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 140);
    }

    private void initPauseLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        pauseLabel = new Label(PAUSE_INFORMATION_BUTTON_TEXT, style);
        pauseLabel.setPosition(camera.position.x, camera.position.y);
    }

    private void initMenuScreen() {
        menuScreen = new MenuScreen(stage, assets.manager.get("assets/logo.png", Texture.class));
    }

    private void initClouds() {
        cloudArray = new ArrayList<>();

        for (int i = 0; i < CLOUDS; i++) {
            Cloud c = null;
            c = new Cloud(MathUtils.random(IcyTower.SCREEN_WIDTH),
                    LEVEL_CLOUDS * IcyTower.SCREEN_HEIGHT + MathUtils.random(IcyTower.SCREEN_HEIGHT + 300) + 300,
                    assets.manager.get("assets/cloud.png", Texture.class));

            LEVEL_CLOUDS++;

            stage.addActor(c);
            cloudArray.add(c);
        }
    }

    private void initPlatforms() {
        platformArray = new ArrayList<>();

        for (int i = LEVEL_PLATFORMS; i < PLATFORMS; i++) {
            int length = MathUtils.random(3) + 3;

            Platform p = new Platform(MathUtils.random(IcyTower.SCREEN_WIDTH - length * 64),
                    LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS,
                    length,
                    assets);

            LEVEL_PLATFORMS++;

            p.onStage(stage);
            platformArray.add(p);
        }
    }

    private void initEnemy() {
        removeEnemyArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
    }

    private void initBackground() {
        background = new Background(30, 200);
        stage.addActor(background);
    }

    private void initFloor() {
        floor = new Floor(0, -IcyTower.SCREEN_HEIGHT / 2 - 100 + 10);
        stage.addActor(floor);
    }

    private void initFont() {
        font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);
    }

    private void initPlayer() {
        player = new Player(assets.manager.get("assets/mario.png", Texture.class));

        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + 250, 0);
        cameraScore.position.set(camera.position.x + 70, camera.position.y, 0);

        anim = new AnimatedImage(player.getAnimation());
        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());
        anim.setOrigin(player.getWidth() / 2, player.getHeight() / 2);

        stage.addActor(anim);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if ((first || !menuScreen.getMenuState()) && !game.isPause()) {
            first = false;
            update();
        }

        if (!menuScreen.getMenuState()) {
            constantStage.addActor(pauseButton);
            Gdx.input.setInputProcessor(constantStage);

        } else
            Gdx.input.setInputProcessor(stage);


        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();
        batch.end();

        batch.begin();
        batch.setProjectionMatrix(cameraScore.combined);

        constantStage.draw();
        batch.end();

        batch.begin();
        batch.setProjectionMatrix(cameraScore.combined);

        if (!menuScreen.getMenuState()) {
            scoreLabel.setText("MARIO\n" + menuScreen.getPoints(scoreControler.getSCORE()));
            scoreLabel.draw(batch, 1);
        }

        if (game.isPause())
            pauseLabel.draw(batch, 1);

        batch.end();
    }

    private void update() {
        stage.act();

        backgroundUpdate();
        playerUpdate();
        platformUpdate();
        cloudsUpdate();
        scoreControler.update();

        flyingObjectControler.update(player);
        flyingObjectControler.setRandomize(menuScreen.getMenuState());
    }

    private void backgroundUpdate() {
        background.update(player.getX() / 300, player.getWidth() / 2 + player.getY() / 30);

        if(player.getX() > IcyTower.SCREEN_WIDTH)
            player.setX( -player.getWidth() );

        if(player.getX() < -player.getWidth())
            player.setX( IcyTower.SCREEN_WIDTH );

        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());

        if (!player.getDie())
            camera.position.set(IcyTower.SCREEN_WIDTH / 2, player.getY() + 200 + player.getJumpVelocity() / 100 + cameraVelocity, 0);


        if (player.getJumpVelocity() == 0)
            cameraVelocity += (CAMERA_MOVEMENT_SPEED * Gdx.graphics.getDeltaTime());

        else if (player.getJumpVelocity() > 0 && cameraVelocity > 0)
            cameraVelocity -= (CAMERA_MOVEMENT_SPEED * 2 * Gdx.graphics.getDeltaTime());


        if (player.getX() > IcyTower.SCREEN_WIDTH)
            player.setX(-player.getWidth());

        if (player.getX() < -player.getWidth())
            player.setX(IcyTower.SCREEN_WIDTH);
    }

    private void playerUpdate() {
        player.update();
        playerDie();
        playerRotation();

        if (player.getJumpVelocity() < -1500) {
            player.setDie(true);
            player.setJump(false);
            player.setJumpVelocity(300);
        }

        if (player.getJumpVelocity() < 0)
            scoreControler.setAddPoints(true);
    }

    private void playerRotation() {
        if (player.getRotate()) {
            if (player.angle > 360) {
                player.angle = 0;
                player.setRotate(false);
                player.setFlip(false);
            }

            anim.setRotation(player.angle);
            player.angle += 10.25f;
        }
    }

    private void playerDie() {
        if (!player.getDie() && player.getY() + player.getHeight() < camera.position.y - IcyTower.SCREEN_HEIGHT / 2) {
            player.setDie(true);
            player.setJump(false);
            player.setJumpVelocity(700);
        } else if (player.getDie() && player.getY() + player.getHeight() < camera.position.y - IcyTower.SCREEN_HEIGHT / 2)
            die = true;

        if (player.getDie()) {
            if (die) {
                pauseButton.remove();

                player.setDie(false);
                first = true;
                die = false;

                menuScreen.setLastScore(scoreControler.getSCORE());
                cameraVelocity = 0;

                try {
                    scoreControler.increaseScore(scoreControler.getScoreToAdd());
                    scoreControler.setScoreToAdd(0);

                    saveNewRecord();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                scoreControler.setSCORE(0);

                player.setPosition(0, 100);
                anim.setPosition(0, 100);
                anim.setAnimation(player.getAnimation());

                LEVEL_PLATFORMS = 1;
                LEVEL_CLOUDS = 1;

                for (Platform p : platformArray) {
                    p.removeBlocks();
                    p.addAction(Actions.removeActor());
                }

                for (Enemy e : enemyArray) {
                    e.animation.addAction(Actions.removeActor());
                    e.addAction(Actions.removeActor());
                }

                for (Cloud c : cloudArray)
                    c.remove();

                player.addAction(Actions.removeActor());
                anim.addAction(Actions.removeActor());

                platformArray.clear();
                cloudArray.clear();
                enemyArray.clear();

                camera.position.set(IcyTower.SCREEN_WIDTH / 2, player.getY() + 200 + player.getJumpVelocity() / 100, 0);

                initClouds();
                initPlatforms();
                initPlayer();

                menuScreen.showMenu(stage);
            }
        }
    }

    private void platformUpdate() {
        for (Platform p : platformArray) {
            if (isPlayerOnPlatform(p) && !player.getDie()) {
                player.setJump(true);
                player.setJumpVelocity(0);
                player.setY(p.getY() + p.getHeight() - 4);
                player.setCollision(true);

                if (scoreControler.isAddPoints() && p.isPoints()) {
                    scoreControler.increaseScoreToAdd(2 * LEVEL_PLATFORMS);
                    scoreControler.setAddPoints(false);

                    p.setPoints(false);
                }
            }

            if (player.getY() - IcyTower.SCREEN_HEIGHT * 1.5f > p.getY()) {
                int length = 0;

                if (LEVEL_PLATFORMS % 10 != 0) {
                    length = MathUtils.random(3) + 3;

                    p.setSIZE(length);
                    p.setPos(MathUtils.random(IcyTower.SCREEN_WIDTH - (length * 64)), LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS);
                    p.onStage(stage);
                    p.setPoints(true);
                } else {
                    length = 10;

                    p.setSIZE(length);
                    p.setPos(-100, LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS);
                    p.onStage(stage);
                    p.setPoints(true);
                }

                anim.remove();
                stage.addActor(anim);

                LEVEL_PLATFORMS++;

                if (LEVEL_PLATFORMS % 10 == 0 && AMOUT_OF_ENEMIES <= 8)
                    AMOUT_OF_ENEMIES++;

                getRandomEnemy(p);
            }

            enemyUpdate(p);
        }
    }

    private void getRandomEnemy(Platform p) {
        int value = MathUtils.random(6);

        if (value < 3) {
            Enemy e = null;
            int type = MathUtils.random(AMOUT_OF_ENEMIES);

            switch (type) {
                case 0:
                    e = new Worm(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_1_red.png", Texture.class));
                    break;

                case 2:
                    e = new Turtle(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_2_green.png", Texture.class));
                    break;

                case 4:
                    e = new Worm(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_1_blue.png", Texture.class));
                    break;

                case 6:
                    e = new Turtle(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_3_black.png", Texture.class));
                    break;

                case 8:
                    e = new Turtle(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_2_red.png", Texture.class));
                    break;

                case 10:
                    e = new Turtle(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_3_blue.png", Texture.class));
                    break;

                case 12:
                    e = new Worm(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_1_grey.png", Texture.class));
                    break;

                case 14:
                    e = new Turtle(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_2_blue.png", Texture.class));
                    break;

                case 16:
                    e = new Turtle(p.getX() + 15, p.getY() + p.getHeight() - 4, assets.manager.get("assets/mob_3_grey.png", Texture.class));
                    break;

                default:

                    break;
            }

            if (e != null) {
                e.animation.setAnimation(e.getAnimation());
                e.animation.setPosition(e.getX(), e.getY());
                e.setSPEED(e.getSPEED() + (LEVEL_PLATFORMS / 10));

                stage.addActor(e.animation);
                enemyArray.add(e);
            }
        }
    }

    private void enemyUpdate(Platform p) {
        for(Enemy e : enemyArray) {

            if (isEnemyOnPlatform(e, p) && (e.getX() > p.getX() + p.getBounds().getWidth() - e.getWidth() || e.getX() < p.getX())) {
                e.oppositeSPEED();
            }

            if (!player.getDie() && e.getMove()) {
                if (player.getBottomBound().overlaps(e.getTopBound())) {
                    e.die();
                    player.setJumpVelocity(1475);
                    player.setRotate(true);
                    player.setFlip(true);
                    scoreControler.increaseScoreToAdd(LEVEL_PLATFORMS * 100);

                    removeEnemyArray.add(e);
                }

                if (player.getRightBound().overlaps(e.getLeftBound()) ||
                        player.getLeftBound().overlaps(e.getRightBound()) ||
                        player.getTopBound().overlaps(e.getBottomBound())) {

                    player.setDie(true);
                    player.setJumpVelocity(500);

                    try {
                        saveNewRecord();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            e.update();
        }

        enemyDie();
    }

    private void enemyDie() {
        if (removeEnemyArray.size() > 0) {
            enemyTimer += Gdx.graphics.getDeltaTime();

            if (enemyTimer > 1.0f) {
                for (Enemy e : removeEnemyArray) {
                    e.animation.addAction(Actions.removeActor());
                    enemyArray.remove(e);
                }

                removeEnemyArray.clear();
                enemyTimer = 0;
            }
        }
    }

    private void saveNewRecord() throws FileNotFoundException {
        if (scoreControler.getSCORE() > menuScreen.getBestScore()) {
            PrintWriter zapis = new PrintWriter("assets/bestScore.txt");
            zapis.println(scoreControler.getSCORE());

            zapis.close();
        }
    }

    private void cloudsUpdate() {
        Cloud cl = null;

        for(Cloud c : cloudArray){
            c.update();

            if (c.getY() < camera.position.y - IcyTower.SCREEN_HEIGHT && cloudArray.size() < 2) {
                c.setPosition(-c.getWidth(), MathUtils.random(camera.position.y, camera.position.y + IcyTower.SCREEN_HEIGHT));

                c.setNewSpeed();

                LEVEL_CLOUDS++;
            }

            if (c.getY() < camera.position.y - IcyTower.SCREEN_HEIGHT) {
                cl = c;
            }

            if(c.getX() >= IcyTower.SCREEN_WIDTH)
                c.setX( -c.getWidth() );

            else if(c.getX() <= -c.getWidth())
                c.setX( IcyTower.SCREEN_WIDTH );
        }

        if (cl != null) {
            cloudArray.remove(cl);
        }
    }

    private boolean isEnemyOnPlatform(Enemy e, Platform p) {
        return e.getBottomBound().overlaps(p.getBounds());
    }

    private boolean isPlayerOnPlatform(Platform p) {
        return player.getJumpVelocity() < 0 && player.getBottomBound().overlaps(p.getBounds()) && !(player.getY() < p.getY());
    }
}
