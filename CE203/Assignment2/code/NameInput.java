package code;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class NameInput extends JFrame {
    /**
     * On this screen you are told that you lost,
     * the score is displayed
     * and name input asked
     */

    //Content Pane
    private JPanel mainPanel;
    private Image img;
    private File src;
    private InputStream streamSrc;

    //TextField
    private JTextField input;
    private Font inputFont;

    //JLabel
    private JLabel scoreScreen;
    private JLabel message;
    private Font labelFont;

    //Name & score
    String name;
    int inputScore;

    //JButton to submit name
    JButton submit;

    public NameInput(int score) throws IOException {
        inputScore = score;

        initialise();

        pack();

        setTitle("Name");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        pack();
    }

    public void initialise() throws IOException {
        //CREATE JPanel
        streamSrc = this.getClass().getClassLoader().getResourceAsStream("cryingPepe8bit.jpg");
        img = ImageIO.read(streamSrc);

        mainPanel = new JPanel(){
            public void paintComponent(Graphics g) {
                //background
                g.drawImage(img, 0, 0, null);
            }
        };
        mainPanel.setPreferredSize(new Dimension(400, 350));
        BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(layout);
        //add
        add(mainPanel);


        //CREATE JLabel scoreScreen
        scoreScreen = new JLabel("YOUR FINAL SCORE: " + inputScore);
        //size
        scoreScreen.setPreferredSize(new Dimension(400, 100));
        scoreScreen.setMinimumSize(scoreScreen.getPreferredSize());
        //text
        labelFont = new Font("Dialog", Font.BOLD, 18);
        scoreScreen.setForeground(new Color(203, 0, 0));
        scoreScreen.setFont(labelFont);
        scoreScreen.setHorizontalAlignment(SwingConstants.CENTER);
        scoreScreen.setVerticalAlignment(SwingConstants.TOP);
        //add
        scoreScreen.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scoreScreen);


        //CREATE JLabel message
        message = new JLabel();
        //size
        message.setPreferredSize(new Dimension(400, 150));
        //text
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.BOTTOM);
        message.setForeground(new Color(203, 0, 0));
        message.setText("<html>" +
                "<div style = 'text-align: center;'>" + "YOU LOST" +
                "<div style = 'text-align: justify;'>" + "ENTER YOUR NAME IN THE FIELD BELOW:" +
                "</html>"
        );
        message.setFont(labelFont);
        //add
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(message);


        //CREATE JTextfield
        input = new JTextField("Enter 3 characters");
        //size
        input.setPreferredSize(new Dimension(400, 40));
        //text
        input.setHorizontalAlignment(SwingConstants.CENTER);
        inputFont = new Font("Dialog", Font.PLAIN, 15);
        input.setFont(inputFont);
        //add
        input.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(input);


        //CREATE JButton
        submit = new JButton("Submit");
        //size
        submit.setPreferredSize(new Dimension(400, 60));
        submit.setMaximumSize(submit.getPreferredSize());
        //action
        submit.addActionListener(e -> {
            if(input.getText().length() != 3){
                message.setText("<html><div style = 'text-align: justify;'>" + "ENTER EXACTLY 3 CHARACTERS!" + "</html>");
            }else {
                name = input.getText().toUpperCase();
                dispose();
                try {
                    new EndGame(name, inputScore);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        submit.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        submit.setBackground(new Color(0, 153, 0));
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(submit);
    }
}
