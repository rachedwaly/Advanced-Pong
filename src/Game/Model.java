package Game;

import Background.BackgroundObject;
import Entities.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Model implements ActionListener, KeyListener {

    public static final boolean DEBUGMODE = false;
    public static Random random  = new Random();
    public static int HEIGHT=600,WIDTH=300;
    public static final int DELAY = 8;
    public Stick s1;
    public Ball b;
    private HashMap<String,Image> allImages=new HashMap<>();
    private PlayGround view;
    private Entity entityBuffer1,entityBuffer2;
    private Timer timer;
    private int currentLvl=1;
    private ArrayList<Entity> drawables=new ArrayList<>();
    private ArrayList<BackgroundObject> backgroundObjects=new ArrayList<>();

    public ArrayList<Entity> physicalObjects = new ArrayList<>();


    private Enemy[] level1List = {
                                    new Enemy(Enemy.SENTRY,150,-50,150,200),
                                    new Enemy(Enemy.SENTRY,150,-50,200,200),
                                    new Enemy(Enemy.SENTRY,150,-50,50,100),
                                    new Enemy(Enemy.SENTRY,150,-50,100,150),
            new Enemy(Enemy.SENTRY,150,-50,250,350),
            new Enemy(Enemy.SENTRY,150,-50,20,300),
                                    //new Enemy(400,400,500,500)
                                };

    public Model() throws IOException {
        random = new Random();
        loadPhotos();
        view = new PlayGround(this);
        VerticalWall wallRight = new VerticalWall(WIDTH - 10, 0, 10, HEIGHT);
        VerticalWall wallLeft = new VerticalWall(0, 0, 10, HEIGHT);
        HorizontalWall wallUp = new HorizontalWall(10, 0, WIDTH-20, 10);
        s1 = new Stick(WIDTH / 2, HEIGHT - 20);
        b = new Ball(250, 580);

        addPhysicalObject(wallRight);
        addPhysicalObject(wallLeft);
        //addPhysicalObject(wallUp);
        addPhysicalObject(s1);
        for(Projectile projectile : s1.projectiles)
            addPhysicalObject(projectile);

        if(!DEBUGMODE){
            for(Enemy enemy : level1List){
                addPhysicalObject(enemy);
                for(Projectile projectile : enemy.projectiles)
                    addPhysicalObject(projectile);
            }
        }



        for (Entity entity : physicalObjects) {
            if(entity instanceof Enemy){
                Enemy enemy = (Enemy) entity;
                for(Projectile projectile : enemy.projectiles)
                    addDrawable(projectile);
            }
            else if(entity instanceof Stick){
                Stick stick = (Stick) entity;
                for(Projectile projectile : stick.projectiles)
                    addDrawable(projectile);
            }
            addDrawable(entity);
        }
        addDrawable(b);
        addPhysicalObject(b);
        setUpBackgroundObjects();
        timer = new Timer(DELAY, this);
        timer.start();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        this.update();
        view.update();
    }

    public void addPhysicalObject(Entity e){
        physicalObjects.add(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
    s1.keyPressed(e);
    }
    @Override
    public void keyReleased(KeyEvent e) {
    s1.keyReleased(e);
    }
    private void update() {
        solveCollisions();
        for (Entity e : physicalObjects) {
            e.superUpdate();
        }
        for (BackgroundObject backgroundObject:backgroundObjects){
            backgroundObject.update();
        }


    }

    private void addDrawable(Entity e){
        drawables.add(e);
    }
        public void solveCollisions(){
        for(int i = 0; i < physicalObjects.size() - 1; i++){
            entityBuffer1 = physicalObjects.get(i);
            if(entityBuffer1.isActive()){
                for(int j = i + 1; j < physicalObjects.size(); j++){
                    entityBuffer2 = physicalObjects.get(j);
                    if( entityBuffer2.isActive() &&
                            entityBuffer1.getBounds().intersects(entityBuffer2.getBounds())){//Order of collision
                        physicalObjects.get(i).whenCollided(entityBuffer2);
                        physicalObjects.get(j).whenCollided(entityBuffer1);
                    }
                }
            }
        }
    }
    public PlayGround getView() {
        return view;
    }

    public int getPlayerHealth(){
        return s1.getHealth();
    }
    public int getPlayerScore(){
        return s1.getScore();
    }


    public void stopTimer(){
        timer.stop();
    }
    public void startTimer(){
        timer.start();
    }

    private void loadPhotos() throws IOException {
        //This method will import all the photos needed for our game
        BufferedImage ph= ImageIO.read(new File("Resources/health.png"));
        allImages.put("health",(Image) ph);
        BufferedImage ph1= ImageIO.read(new File("Resources/background lvl1.jpg"));
        allImages.put("lvl1",(Image)ph1);
        BufferedImage ph2= ImageIO.read(new File("Resources/background lvl2.jpg"));
        allImages.put("lvl2",(Image)ph2);
        BufferedImage ph3= ImageIO.read(new File("Resources/cloud.png"));
        allImages.put("cloud",(Image) ph3);

    }

    public Image getPhoto(String name){
        return allImages.get(name);
    }

    public ArrayList<Entity> getDrawables() {
        return drawables;
    }

    public int getCurrentLvl() {
        return currentLvl;
    }

    public void setCurrentLvl(int currentLvl) {
        this.currentLvl = currentLvl;
    }

    private void setUpBackgroundObjects() {
        switch(getCurrentLvl()){
            case 1:{
                addBackGroundObject(new BackgroundObject("cloud",50,60,this));
                break;
            }
        }

    }

    public ArrayList<BackgroundObject> getBackgroundObjects() {
        return backgroundObjects;
    }

    private void addBackGroundObject(BackgroundObject backgroundObject){
        backgroundObjects.add(backgroundObject);
    }

}
