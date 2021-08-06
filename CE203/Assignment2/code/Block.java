package code;

import java.awt.*;

public class Block extends Objects{
    /**
     * This class defines Object code.Block
     * to create blocks on the screen
     *
     * Class consists of draw method to draw code.Block
     * and hitboxes, which are the rectangles inside the code.Block
     * on the perimeter to calculate collisions
     */

    public Block(int w, int h, int r, int c, Color color){
        super(w, h, w*r, (h * c) + 100, color);
    }

    //Define hit boxes--------------------------------
    public Rectangle hitBoxUp() { return new Rectangle(xPos, yPos, width, 1); }

    public Rectangle hitBoxDown(){
        return new Rectangle(xPos, yPos + height - 1, width, 1);
    }

    public Rectangle hitBoxLeft(){
        return new Rectangle(xPos, yPos, 1, height);
    }

    public Rectangle hitBoxRight(){
        return new Rectangle(xPos + width - 1, yPos, 1, height);
    }
    //------------------------------------------------

    //Create border(used in ball)
    Rectangle border(){
        return new Rectangle(xPos, yPos, width, height);
    }

    public void draw(Graphics g) {
        //Draw the rectangular block
        g.setColor(color);
        g.fillRect(xPos, yPos, width, height);
    }
}
