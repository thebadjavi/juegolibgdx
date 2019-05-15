package com.mygdx.game;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
 import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.FloorEntity;
import com.mygdx.game.entities.ObstacleEntity;
 import com.mygdx.game.entities.PlayerEntity;

import java.util.ArrayList;
import java.util.List;


public class GameScreen extends BaseScreen {

    private Stage stage;

    private World world;

    private PlayerEntity player;

    private List<FloorEntity> floorList= new ArrayList<FloorEntity>();
    private List<ObstacleEntity> drakoList= new ArrayList<ObstacleEntity>();

    private Sound jumpSound,dieSound;
    private Music bgMusic;

    private Vector3 position;

    private int puntuacion;

    private Hud hud;

    Texture PayerTexture=game.getManager().get("tai.png");
    Texture floorTexture=game.getManager().get("floor.png");
    Texture overfloodTexture=game.getManager().get("overfloor.png");
    Texture drakoTexture=game.getManager().get("drako.PNG");




    public GameScreen(final MainGame game) {
        super(game);
        this.game = game;

        jumpSound = game.getManager().get("audio/jump.ogg");
        dieSound = game.getManager().get("audio/die.ogg");
        bgMusic = game.getManager().get("audio/song.ogg");
        stage= new Stage(new FitViewport(640,360));
        position = new Vector3(stage.getCamera().position);
        float startTime = System.currentTimeMillis();
        hud = new Hud(game.batch);

        System.out.println("Time elapsed in seconds = " + ((System.currentTimeMillis() - startTime) / 1000));




        world=new World(new Vector2(0,-10),true);

        world.setContactListener(new ContactListener() {
            private boolean areCollided(Contact contact, Object userA, Object userB) {
                Object userDataA = contact.getFixtureA().getUserData();
                Object userDataB = contact.getFixtureB().getUserData();

                if (userDataA == null || userDataB == null) {
                    return false;
                }

                 return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                        (userDataA.equals(userB) && userDataB.equals(userA));
            }

            @Override
            public void beginContact(Contact contact) {
                if (areCollided(contact, "player", "floor")) {
                    player.setJumpin(false);

                    if (Gdx.input.isTouched()) {
                        jumpSound.play();

                        player.setMustJump(true);
                    }
                }
                if (areCollided(contact, "player", "spike")) {

                     if (player.isAlive()) {
                        player.setAlive(false);

                        bgMusic.stop();
                        dieSound.play();

                        stage.addAction(
                                Actions.sequence(
                                        Actions.delay(1.5f),
                                        Actions.run(new Runnable() {

                                            @Override
                                            public void run() {
                                                game.setScreen(game.gameOverScreen);
                                            }
                                        })
                                )
                        );
                    }
                }
                if (areCollided(contact,"player","Bone")){
                   puntuacion= puntuacion+5;
                    System.out.print("huesoooo ");
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

    }

    @Override
    public void show() {
        player=new PlayerEntity(world,PayerTexture,new Vector2(1.5f,1.5f));
        floorList.add(new FloorEntity(world,floorTexture,overfloodTexture,0,1000,1));
        floorList.add(new FloorEntity(world,floorTexture,overfloodTexture,12,10,2));
        drakoList.add(new ObstacleEntity(world,drakoTexture,6,1));
        drakoList.add(new ObstacleEntity(world,drakoTexture,18,2));


        for (int i = 0; i<9;i++){  LLamadaDrako();  }

        stage.addActor(player);
        for (FloorEntity floor:floorList){
            stage.addActor(floor);
        }
        for (ObstacleEntity drako:drakoList){
            stage.addActor(drako);
        }

        stage.getCamera().position.set(position);
        stage.getCamera().update();

        bgMusic.setVolume(0.75f);
        bgMusic.play();
    }


    @Override
    public void hide() {
        stage.clear();
        player.detach();

        for (FloorEntity floor : floorList)
            floor.detach();

        for (ObstacleEntity drako : drakoList)
            drako.detach();

        floorList.clear();
        drakoList.clear();
    }

    public void LLamadaDrako(){
        int x = 0;
        x+=100;
        x = (int)(Math.random()*101);
        drakoList.add(new ObstacleEntity(world,drakoTexture,x,1));
    }


    public void update(float dt){
        hud.update(dt);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f,0.5f,0.8f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);


        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        if (player.getX() > 150 && player.isAlive()) {
            float speed = Constants.PLAYER_SPEED * delta * Constants.PIXELS_IN_METER;
            stage.getCamera().translate(speed, 0, 0);
        }
        if (Gdx.input.justTouched()){
            jumpSound.play();
            player.jump();
        }
        stage.act();
        world.step(delta,6,2);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        hud.dispose();

    }
    public Hud getHud(){ return hud; }

}
