import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventListener;




public class PlayGround extends JPanel  {


    private ArrayList<Entity> drawables=new ArrayList<>();
    public JButton gameToMenu;

    public PlayGround(){
        super(new FlowLayout()); //BorderLayout instead ?

        gameToMenu = new JButton("Menu");
        add(gameToMenu);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for (Entity entity : drawables) {
            entity.drawEntity(g);
        }
    }

    public void addDrawable(Entity e){
        drawables.add(e);
    }
    public void removeDrawable(Entity e){
        drawables.remove(e);
    }



    public void update(){
        repaint();
    }

}
