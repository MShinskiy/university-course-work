package code;

import java.awt.event.*;
import java.io.IOException;

public class MouseMoveListener implements ActionListener, MouseMotionListener {
    /**
     * This class uses listeners to let user
     * to interact with the game
     * go method defines the decision taken to consider the direction of the ball
     */

    private Game game; // game passed through to allow for game manipulation
    private Paddle paddle;
    private Ball ball;
    private int i = 0;

    public MouseMoveListener(Game game, Paddle paddle, Ball ball){
        this.game = game;
        this.paddle = paddle;
        this.ball = ball;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        paddle.mouseMove(e);
    }

    //MOVE THE BALL---------------------------------------------------------------------------
    public void go() throws IOException {
        if (i++ == 1) ball.start();
        else {
            if(game.blockList.isEmpty()){

                int delay = game.timer.getDelay();
                if(delay > 6) {
                    ball.speed += 1;
                    game.timer.setDelay(delay - 3);
                }else
                    ball.speed += 3;
                ball.xPos = 295;
                ball.yPos = 585;
                ball.xVec = 0;
                ball.yVec = Math.abs(ball.yVec);
                game.generateBlocks();
            }
            //If collides with BLOCK
            if (ball.collDetectBlockAll()) {
                //Hits Horizontal
                if (ball.collDetectBlockUp() || ball.collDetectBlockDown()) {
                    ball.deleteBlock();
                    ball.newDirectionHorizontal();

                    //Hits Vertical
                } else if (ball.collDetectBlockLeft() || ball.collDetectBlockRight()) {
                    ball.deleteBlock();
                    ball.newDirectionVertical();
                }
            //If collides with other object
            //Hits Horizontal
            }else if(ball.collDetectHorizontalBorder()) {
                ball.newDirectionHorizontal();
            //Hits code.Paddle
            } else if(ball.collDetectPaddle()) {
                ball.newDirectionPaddle();
                //Hits Vertical
            } else if(ball.collDetectVerticalBorder()) {
                ball.newDirectionVertical();
            }else{
                ball.move();
            }
            //if ball below paddle
            if(ball.yPos > paddle.yPos + 30){
                //close the game frame and open frame to enter name
                game.timer.stop();
                game.dispose();
                new NameInput(game.score);
            }
        }
    }//---------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            go();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        game.repaint();
    }
}
