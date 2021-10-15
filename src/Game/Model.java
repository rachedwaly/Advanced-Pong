package Game;


import Entities.*;
import Entities.Bonus.Bonus;
import Entities.Bonus.LengthBonus;
import Entities.Bonus.LifeBonus;
import Entities.Bonus.ShieldBonus;
import Frame.*;
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
    public Stick stick;
    public Ball b;
    private HashMap<String,Image> allImages=new HashMap<>();
    private PlayGround view;
    private Entity entityBuffer1,entityBuffer2;
    private Timer timer;
    private int currentLvl=1;
    private PongEmUp pongEmUp;


    public boolean playing=true;

    //on sépare les backgroundobjects des drawables vu qu'ils doivent etre dessinés avant ces
    // derniers
    private ArrayList<Entity> drawables=new ArrayList<>();
    private ArrayList<BackGroundObject> backgroundObjects=new ArrayList<>();


    public ArrayList<Entity> physicalObjects = new ArrayList<>();

    //il faut qu'on genere les ennemis après loadphotos()
    private ArrayList<Enemy> ennemies = new ArrayList<>();
    private ArrayList<Bonus> bonuses=new ArrayList<>();

    public Model(PongEmUp pongEmUp) throws IOException {
        this.pongEmUp=pongEmUp;
        loadPhotos();

        generateEnemies();
        view = new PlayGround(this);
        VerticalWall wallRight = new VerticalWall(WIDTH - 10, 0, 10, HEIGHT,this);
        VerticalWall wallLeft = new VerticalWall(0, 0, 10, HEIGHT,this);
        HorizontalWall wallUp = new HorizontalWall(10, 0, WIDTH-20, 10,this);
        stick = new Stick(WIDTH / 2, HEIGHT - 20,this,50);
        b = new Ball(250, 580,this);
        addPhysicalObject(wallRight);
        addPhysicalObject(wallLeft);
        addPhysicalObject(wallUp);
        addPhysicalObject(stick);
        for(Projectile projectile : stick.projectiles)
            addPhysicalObject(projectile);

        if(!DEBUGMODE){
            for(Enemy enemy : ennemies){
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

    private void generateEnemies() {
        ennemies.add(new Enemy(Enemy.SENTRY,100,0,100,200,this));
        ennemies.add(new Enemy(Enemy.SENTRY,250,0,250,200,this));
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
    stick.keyPressed(e);
    }
    @Override
    public void keyReleased(KeyEvent e) {
    stick.keyReleased(e);
    }
    private void update() {
        solveCollisions();
        for (int i = 0; i < physicalObjects.size(); i++) {
            physicalObjects.get(i).update();
        }

        for (int i=0;i<backgroundObjects.size();i++){
            backgroundObjects.get(i).update();
        }



    }

    private void addDrawable(Entity e){
        drawables.add(e);
    }

    public void solveCollisions(){
        for(int i = 0; i < physicalObjects.size() - 1; i++){
            entityBuffer1 = physicalObjects.get(i);
            for(int j = i + 1; j < physicalObjects.size(); j++){
                entityBuffer2 = physicalObjects.get(j);
                if(entityBuffer1.getShape().intersects(entityBuffer2.getShape())){//Order of collision
                    //entityBuffer1.debugLog();
                    //System.out.println("with");
                    //entityBuffer2.debugLog();
                    physicalObjects.get(i).whenCollided(entityBuffer2);
                    physicalObjects.get(j).whenCollided(entityBuffer1);
                }
            }
        }
    }
    public PlayGround getView() {
        return view;
    }

    public int getPlayerSpawnLeft(){
        return stick.getLives();
    }
    public int getPlayerScore(){
        return stick.getScore();
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
        BufferedImage ph4= ImageIO.read(new File("Resources/explosion_animation/1death.png"));
        allImages.put("1death",(Image) ph4);
        BufferedImage ph5= ImageIO.read(new File("Resources/explosion_animation/2death.png"));
        allImages.put("2death",(Image) ph5);
        BufferedImage ph6= ImageIO.read(new File("Resources/explosion_animation/3death.png"));
        allImages.put("3death",(Image) ph6);
        BufferedImage ph7= ImageIO.read(new File("Resources/explosion_animation/4death.png"));
        allImages.put("4death",(Image) ph7);
        BufferedImage ph8= ImageIO.read(new File("Resources/explosion_animation/5death.png"));
        allImages.put("5death",(Image) ph8);
        BufferedImage ph9= ImageIO.read(new File("Resources/explosion_animation/6death.png"));
        allImages.put("6death",(Image) ph9);
        BufferedImage ph10= ImageIO.read(new File("Resources/explosion_animation/7death.png"));
        allImages.put("7death",(Image) ph10);
        BufferedImage ph11= ImageIO.read(new File("Resources/explosion_animation/8death.png"));
        allImages.put("8death",(Image) ph11);
        BufferedImage ph12= ImageIO.read(new File("Resources/explosion_animation/9death.png"));
        allImages.put("9death",(Image) ph12);
        BufferedImage ph13= ImageIO.read(new File("Resources/sentry.png"));
        allImages.put("sentry",(Image) ph13);
        BufferedImage ph13r = ImageIO.read(new File("Resources/sentryDamaged.png"));
        allImages.put("sentryRed",(Image) ph13r);
        BufferedImage ph14= ImageIO.read(new File("Resources/plane.png"));
        allImages.put("plane",(Image) ph14);
        BufferedImage ph15= ImageIO.read(new File("Resources/gameover.png"));
        allImages.put("gameover",(Image) ph15);
        BufferedImage ph16= ImageIO.read(new File("Resources/shield.png"));
        allImages.put("shield",(Image) ph16);
        BufferedImage ph17= ImageIO.read(new File("Resources/shieldStick.png"));
        allImages.put("shieldStick",(Image) ph17);
        BufferedImage ph18= ImageIO.read(new File("Resources/muscle.png"));
        allImages.put("muscle",(Image) ph18);



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
                addBackGroundObject(new BackGroundObject("cloud",50,60,this,new float[]{0.5f,
                        0.0f}));
                addBackGroundObject(new BackGroundObject("cloud",200,80,this,new float[]{-0.5f,
                        0.0f}));
                addBackGroundObject(new BackGroundObject("cloud",150,105,this,new float[]{1f,
                        0.0f}));
                addBackGroundObject(new BackGroundObject("plane",100,200,this,new float[]{-0.5f,
                        0.0f}));
                break;
            }
        }

    }

    public ArrayList<BackGroundObject> getBackgroundObjects() {
        return backgroundObjects;
    }

    private void addBackGroundObject(BackGroundObject backgroundObject){
        backgroundObjects.add(backgroundObject);
    }

    //this method is called whenever an enemy is dead
    public void removeEnemy(Enemy enemy){
        ennemies.remove(enemy);
        physicalObjects.remove(enemy);
        drawables.remove(enemy);
        spawnBonus(enemy.getX(), enemy.getY());
        enemy = null;
    }

    //this method is called when a bonus is deleted
    public void removeBonus(Bonus bonus){
        physicalObjects.remove(bonus);
        drawables.remove(bonus);
        bonus = null;
    }

    private void spawnBonus(float x, float y) {
        int gen=random.nextInt(2);
        switch (gen){
            case 0:{
                ShieldBonus shieldBonus=new ShieldBonus("shield",x,y,1000, stick,this);
                addDrawable(shieldBonus);
                addPhysicalObject(shieldBonus);
                break;
            }
            case 1:{
                LengthBonus lengthBonus=new LengthBonus("lengthBonus",x,y,3000,stick,50,this);
                addDrawable(lengthBonus);
                addPhysicalObject(lengthBonus);
                break;
            }
            case 2:{
                LifeBonus lifeBonus=new LifeBonus("lifeBonus",x,y,0,stick,this);
                addDrawable(lifeBonus);
                addPhysicalObject(lifeBonus);
                break;
            }

        }

    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void stopTheGame(){
        setPlaying(false);
        timer.stop();
        //TODO add retry button on the left side of the frame
        pongEmUp.gameOver();
    }



}
