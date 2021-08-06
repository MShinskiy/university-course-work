package code;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Paddle extends Objects{
    /**
     * This class defines Object code.Paddle
     * to create paddle on the screen
     *
     * Class consists of draw method to draw code.Paddle
     * border, which is the rectangle around paddle
     * to calculate collisions, and mouseMove to change
     * direction of movement and to control the paddle itself
     */

    //Object code.Paddle
    public Paddle(int width, int height, int xPos, int yPos, Color color){
        super(width, height, xPos, yPos, color);
    }

    void mouseMove(MouseEvent e){
        if(e.getX() < 500 + width/2 && e.getX() > width/2) {
            xPos = e.getX() - width/2;
        }
    }

    boolean isValid() {
       return xPos < 0 || xPos >  600;
    }

    //Get the borders of object for collision detection
    Rectangle border(){
        return new Rectangle(xPos, yPos, width, height);
    }

    //Draw paddle
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(xPos, yPos, width, height);
        //Create a white border around a shape
        g.setColor(Color.WHITE);
        g.drawRect(xPos, yPos, width, height);
    }
}
