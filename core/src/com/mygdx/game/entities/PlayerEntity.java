package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Constants;

import static com.mygdx.game.Constants.IMPULSE_JUMP;
import static com.mygdx.game.Constants.PIXELS_IN_METER;
import static com.mygdx.game.Constants.PLAYER_SPEED;

public class PlayerEntity extends Actor {
    private Texture texture;
     private World world;
    private Body body;
    private Fixture fixture;
    private boolean alive=true;
    private boolean jumping = false;
    private boolean mustJump = false;
    private int lives;




    public PlayerEntity(World  world, Texture texture, Vector2 position ){
        this.world=world;
        this.texture=texture;

        BodyDef def=new BodyDef();
        def.position.set(position);
        def.type= BodyDef.BodyType.DynamicBody;
        body=world.createBody(def);

        PolygonShape taiShape=new PolygonShape();
        taiShape.setAsBox(0.5f,0.5f);
        fixture=body.createFixture(taiShape,1);
        fixture.setUserData("player");
        taiShape.dispose();
        setSize(PIXELS_IN_METER,PIXELS_IN_METER);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x-0.5f)*PIXELS_IN_METER,
                (body.getPosition().y-0.5f)*PIXELS_IN_METER);
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }

    @Override
    public void act(float delta) {
    if ( mustJump){
        mustJump=false;
        jump();
    }

    if (alive){
        float speedY=body.getLinearVelocity().y;
        body.setLinearVelocity(PLAYER_SPEED,speedY);
    }
    if (jumping){
        body.applyForceToCenter(0,-IMPULSE_JUMP*1.25f,true);
    }

    }

    public void jump() {
         if (!jumping && alive) {
            jumping = true;

            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);
        }
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setJumpin(boolean jumping) {
        this.jumping = jumping;
    }


    public boolean isMustJump() {
        return mustJump;
    }

    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }



}
