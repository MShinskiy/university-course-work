package code;

import javax.swing.*;
import java.awt.*;

public class Rules extends JFrame {
    /**
     * The frame will display the rules
     */

    //JPanel
    JPanel panel;

    //JLabel
    JLabel label;

    //JButton
    JButton button;

    public Rules() {
        initialise();

        //standard configuration
        pack();
        setTitle("Rules");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void initialise(){
        //Panel
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(350, 600));
        panel.setBackground(new Color(138,130,122));
        panel.setLayout(new BorderLayout( ));

        //Label
        label = new JLabel();
        Font labelFont = new Font("Helvetica", Font.BOLD, 14);
        label.setFont(labelFont);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(350, 400));
        label.setBorder(BorderFactory.createLineBorder(new Color(138,130,122), 12));
        String rules = "As already was mentioned, you lose if the ball goes below paddle. " +
                "The goal of the game is to get as many points as you can get. " +
                "After destroying all blocks, the level goes up, speed increases, " +
                "amount of points per block increases. " +
                "The points are counted by special function, " +
                "so on level 1 you get 3 p/b (points per block); " +
                "level 2 - 5; " +
                "level 3 - 8; " +
                "level 4 - 12; " +
                "level 5 - 17 and so on... (doubt you can get even to level 4). ";
        String features = "The paddle works in a way that it reflects the ball depending on where the ball hits the paddle. " +
                "That means that if the ball hits the paddle on very left, " +
                "the ball will rebounce to left with a very small angle. Same with the right angle. " +
                "The angle is in the range between 30 and 150. " +
                "If hits in the middle, it will go straight up. Other than this nothing should be abnormal.";
        label.setText(
                "<html>" +
                        "<div style = 'text-align: left;'>" + "<h2>" +  "Rules: " + "</h2>" +
                        "<div style = 'text-align: justify;'>" + rules +
                        "<div style = 'text-align: left;'>" + "<br>" +"<h2>" + "Features: " + "</h2>" +
                        "<div style = 'text-align: justify;'>" + features +
                 "</html>"
        );


        //Button
        button = new JButton("Close");
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(Color.WHITE);
        button.addActionListener(e -> dispose());

        //add
        add(panel);
        panel.add(label, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);
    }
}
