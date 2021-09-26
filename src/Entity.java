import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {
    protected int x,y,width,height;
    static final int HEIGHT=PlayGround.HEIGHT; //height of the game
    static final int WIDTH=PlayGround.WIDTH; //width of the game
    protected  int[] speed =new int[2];
    protected boolean orientation=false; //the orientation of the object
    public Color color = Color.BLACK;
    protected String name;


    public boolean isOrientation() {
        return orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }

    public Entity(){
        this(Main.random.nextInt(WIDTH),Main.random.nextInt(HEIGHT),
                Main.random.nextInt(30),Main.random.nextInt(30),true);
    }

    public Entity(int x,int y){
        this.x=x;
        this.y=y;
        this.width=0;
        this.height=0;

    }

    public Entity(int x,int y, int w, int h){
        this.x=x;
        this.y=y;
        this.width=w;
        this.height=h;

    }

    public Entity(int x,int y, int w, int h,boolean orientation){
        this.orientation=orientation;
        this.x=x;
        this.y=y;
        this.width=w;
        this.height=h;

    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    public void drawEntity(Graphics g){}

    public ArrayList<PhysicalBoundarie> getPhysicalBoundaries(){ return new ArrayList<>();}


}
