package code;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    /**
     * Class contains keyListener to open a new frame code.Rules
     */

    Game game;  //lets attach keyListener to the frame
    PopUp popUp;

    public KeyboardListener(PopUp game){
        this.popUp = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                //open new Frame when "Enter" pressed
                new Rules();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
