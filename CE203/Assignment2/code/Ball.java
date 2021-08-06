package code;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

public class Ball extends Objects{
    /**
     * This class defines Object code.Ball
     * to create ball on the screen
     *
     * Class consists of draw method to draw code.Ball.
     * start, creates initial movement of the ball
     * move, moves the ball
     * newDirectionXXX defines new vector after collision
     * border, creates a RECTANGLE around OVAL ball to indicate collision
     * deleteBlock to delete block
     * collDetectXXX defines collisions with all other code.Objects
     */

    double xVec, yVec;  //vectors (movement)
    private Game game;  //game reference to access objects
    int speed;          //speed of the game
    int diameter;       //size

    //Object code.Ball
    public Ball(int diameter, int xPos, int yPos, double xDir, double yDir, int s, Color color ,Game g){
        super(diameter, diameter, xPos, yPos, color);
        this.diameter = diameter;
        this.game = g;
        this.xVec = xDir;
        this.yVec = yDir;
        this.speed = s;
    }

    //MOVEMENT------------------------------------------------------------
    //Generating random direction in the start of the game
    public void start(){
        // get a random angle in the range 30 <= angle <= 150
        Random ran = new Random();
        double angle = ran.nextInt(121) + 30;
        angle = Math.toRadians(angle);

        //assign with new angle
        xVec = Math.cos(angle) * speed;
        yVec = Math.sin(angle) * speed;
        move();
    }


    //Moving code.Ball in straight line
    public void move(){
        xPos -= xVec;
        yPos -= yVec;
    }

    //Calculate new direction if hitting horizontal surface and move
    public void newDirectionHorizontal() {
        yVec = -yVec;
        if(yPos < 100) yPos = 100;
        if(yPos > 800 - diameter) yPos = 800 - diameter;
        move();
    }

    //Calculate new direction if hitting vertical surface and move
    public void newDirectionVertical() {
        xVec = -xVec;
        if(xPos < 0) xPos = 0;
        if(xPos > 600 - diameter) xPos = 600 - diameter;
        move();
    }

    //Calculate new direction after hitting paddle,
    // the further from the center the bigger the angle
    public void newDirectionPaddle() {
        //distance from center of a ball to center of paddle
        double dist = xPos + diameter/2.0 - game.paddle.xPos;
        double ratio = dist / game.paddle.width;
        double newAngle = Math.toRadians(ratio * 120 + 30); //creates new angle in range 30 <= angle <= 150

        //Update vector
        xVec = Math.cos(newAngle) * speed;
        yVec = Math.sin(newAngle) * speed;
        move();
    }
    //--------------------------------------------------------------------

    //Get the borders of object for collision detection
    public Rectangle border(){
        return new Rectangle(xPos, yPos, diameter, diameter);
    }

    //Delete block that ball intersected with
    public void deleteBlock() {
        for (Block x : game.blockList) {
            //to check with which of the block, ball intersects
            Rectangle block = x.border();
            if(block.intersects(border())) {
                game.blockList.remove(x);
                //function to count the score and display it
                game.score += speed*10/game.timer.getDelay();
                game.scoreLabel.setText("Score:   " + game.score);
                break; //Break to prevent multiple deletion
            }
        }
    }

    //COLLISION DETECTION SECTION----------------------------
    //Idea of the way of detecting the collisions was taken from here
    //http://zetcode.com/tutorials/javagamestutorial/collision/
    public boolean collDetectBlockAll(){
        return collDetectBlockDown() || collDetectBlockLeft() || collDetectBlockRight() || collDetectBlockUp();
    }

    public boolean collDetectBlockUp(){
        for(Block x : game.blockList){
            Area intersection = new Area(x.hitBoxUp().intersection(border()));
            if(!intersection.isEmpty()) return true;
        }
        return false;
    }

    public boolean collDetectBlockDown(){
        for(Block x : game.blockList){
            Area intersection = new Area(x.hitBoxDown().intersection(border()));
            if(!intersection.isEmpty()) return true;
        }
        return false;
    }

    public boolean collDetectBlockLeft(){
        for(Block x : game.blockList){
            Area intersection = new Area(x.hitBoxLeft().intersection(border()));
            if(!intersection.isEmpty()) return true;
        }
        return false;
    }

    public boolean collDetectBlockRight(){
        for(Block x : game.blockList){
            Area intersection = new Area(x.hitBoxRight().intersection(border()));
            if(!intersection.isEmpty()) return true;
        }
        return false;
    }

    //Return true if there is a collision with BORDER
    public boolean collDetectVerticalBorder(){
        return xPos < 0 || xPos > 600 - diameter;}

    //Return true if there is a collision with TOP-BOTTOM BORDER
    public boolean collDetectHorizontalBorder(){
        return yPos < 101 || yPos > 800 - diameter;}

    //Return true if there is a collision with PADDLE
    public boolean collDetectPaddle() { return game.paddle.border().intersects(border()); }
    //-------------------------------------------------------

    //Draw the code.Ball
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(xPos, yPos, diameter, diameter);
    }
}
