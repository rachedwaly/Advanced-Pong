package Entities;
import AltLib.Vec2Math;
import Frame.OptionsPane.BallPreview;
import Entities.Bonus.Bonus;
import Game.Model;
import shape.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

/***
 * Ball that bounces around the playfield
 * Credit : Both
 */
public class Ball extends Entity {
    protected float scalarSpeed; //absolute speed of the ball
    public static final int BALL_DAMAGE = 5;

    /***
     * Credit : Both
     * @param model
     */
    public Ball(Model model) {

        super(model);

        this.width = 10;
        this.height = 10;
        name = "ball";

        reset();

        shape = new CircleShape((int)x + width/2,(int)y + height/2,width/2);
    }

    /***
     * Credit : Rached
     * @param g
     */
    @Override
    public void drawEntity(Graphics g){
        g.setColor(BallPreview.colors[abs(Model.ballColor%BallPreview.colors.length)]);
        g.fillOval((int)x,(int)y,width,height);
        g.setColor(Color.WHITE);
    }

    /***
     * Credit : Kevin
     */
    public void move(){

        this.x += speed[0] * scalarSpeed;
        this.y += speed[1] * scalarSpeed;

        if (x < -100 || x > 500 || y>HEIGHT || y < 0 ){ //ball passes stick or glitches out
            reset();
        }

    }

    /***
     * Credit : Kevin
     */
    public void update(){

        if(innerTimer > 8080)
            scalarSpeed = Math.max(2,scalarSpeed - 1f / 750 * Model.DELAY); //loses 1 speed every 6 seconds
        innerTimer += Model.DELAY;
        move();
        shape.update(this);
    }

    /***
     * Credit : Both
     * @param entity the other object in the collision
     */
    @Override
    public void whenCollided(Entity entity) {
        switch(entity.getEntityTypeName()){
            case "stick":
            case "wall":
                break;
            case "enemy":
            case "bonus":
            case "stickprojectile": //hitting something other than wall maintains speed for 8 seconds
            case "enemyprojectile": //hitting something other than wall maintains speed for 8 seconds
                innerTimer = 81;
                break;
            default:
                break;
        }

        if(entity.getEntityTypeName() != "bonus"){
            float[] normal = Vec2Math.normalize( ((CircleShape)shape).getNormalHit() );
            if(normal[0] == 0 && normal[1] == 0)
                System.out.println("bad normal");


            float[] normSpeed = Vec2Math.normalize(entity.speed);
            if(Vec2Math.dot(speed,normal) > 0){
                speed[0] = speed[0] + normSpeed[0]/2;
                speed[1] = speed[1] + normSpeed[1];

            }else{

                speed = Vec2Math.reflectVector(speed,normal); //is normalized
            }

            speed = Vec2Math.normalize(speed);

            int stuckCounter = 0;
            while(this.getShape().intersects(entity.getShape()) && stuckCounter < 10){
                scalarSpeed = Math.max(scalarSpeed,Vec2Math.distance(entity.speed));
                update();
                //stuckCounter++;
            }
            if(stuckCounter == 10)
                reset();
        }


    }

    @Override
    public String getEntityTypeName() {
        return "ball";
    }

    /***
     * Puts the ball back in game when it gets stuck/goes out of bounds
     * Credit : Kevin
     */
    public void reset(){
        //TODO : explosion animation + time wait + lose life
        x=100;
        y=100;

        speed = new float[]{1,1};
        speed = Vec2Math.normalize(speed);

        scalarSpeed = 2;

    }

}





