package Entities;

import Game.Model;

import java.awt.*;

public class Bonus extends Entity {
private Model model;
    public Bonus(String name, float x, float y, Model model){
        super((int)x,(int)y);
        this.model=model;
        this.name=name;
        this.x=x;
        this.y=y;
        this.width=10;
        this.height=10;
        this.speed[0]=0;
        this.speed[1]=2;
        color=Color.PINK;
    }

    @Override
    public void update() {
        move();
    }

    private void move() {
        y+=speed[1];

        shape.update(this);
        if (y>600){
            model.removeEntity(this);
        }
    }

    @Override
    public void whenCollided(Entity entity) {

        if (entity instanceof Stick){
            model.applyBonus(this);
        }
    }
    @Override
    public String getEntityTypeName() {
        return name;
    }

    @Override
    public void drawEntity(Graphics g) {
        g.setColor(color);
        g.fillRect((int)x,(int)y,width,height);
    }
}
