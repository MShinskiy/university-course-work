package code;

import java.awt.*;

public abstract class Objects {
    /**
     * Abstract class,
     * super class of code.Ball, code.Paddle, code.Block
     */

    int xPos, yPos;     //coords of top left
    int width, height;  //size
    Color color;

    public Objects(int width, int height, int xPos, int yPos, Color color){
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.color = color;
    }

    public abstract void draw(Graphics g);
}
