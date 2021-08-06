package code;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends JFrame {
    /**
     * This class defines the screen that has a game running on it
     * it has a main method too
    */

    //Swing Timer
    private MouseMoveListener listener;
    Timer timer;
    //I took idea that i need to use timer from here
    //https://codereview.stackexchange.com/questions/27197/pong-game-in-java

    //Label
    JLabel scoreLabel;

    //Panels
    private JPanel mainPanel;
    private JPanel optionPanel;

    //Button
    private JButton buttonPause;
    private JButton buttonRestart;
    private Dimension buttonSize;   //just for buttons

    //Image for background /read image
    private BufferedImage img;
    private InputStream streamSrc;

    //code.Objects
    Paddle paddle;
    private Ball ball;

    //ArrayLists
    private ArrayList<Color> colorList;
    CopyOnWriteArrayList<Block> blockList;
    //Used copyOnWrite because arrayList threw exception
    //https://stackoverflow.com/questions/20639098/in-java-can-you-modify-a-list-while-iterating-through-it/26292671

    //Random
    private Random randomState;

    //Score count
    int score;

    public Game() throws IOException {
        //set up game environment
        initialise();

        //create timer
        listener = new MouseMoveListener(this, this.paddle, this.ball);
        timer = new Timer(16, this.listener);   //refresh at 60Hz

        //add MouseListener
        addMouseMotionListener(listener);

        //standard configuration
        pack();

        setTitle("1804336");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        pack();
    }

    private void initialise() throws IOException {
        //create panels and buttons
        mainPanel = new JPanel();
        optionPanel = new JPanel();

        buttonPause = new JButton("PAUSE");
        buttonRestart = new JButton("START");

        buttonSize = new Dimension(100, 50);

        //----------------Visual_Design------------------------------
        //Set up a color palette
        colorList = new ArrayList<>();
        colorList.add(new Color(196,79,148 ));
        colorList.add(new Color(176, 89, 166));
        colorList.add(new Color(152, 98, 179));
        colorList.add(new Color(123, 106, 186));
        colorList.add(new Color(92, 113, 188));
        colorList.add(new Color(58, 118, 185));

        Color paddleColor = new Color(160, 160, 160);

        Color ballColor = Color.YELLOW;

        //set up background
        streamSrc = this.getClass().getClassLoader().getResourceAsStream("spacePepeBack8bit.jpg");
        img = ImageIO.read(streamSrc);
        //-----------------------------------------------------------

        //SET UP & DRAW OBJECTS
        //Set up a List of tiles
        generateBlocks();

        //paddle&ball
        paddle = new Paddle(100,15,250,600, paddleColor);
        ball = new Ball(10, 295, 589, 0, 0, 6, ballColor,this);

        //draw objects on the JPanel
        mainPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                //background
                g.drawImage(img, 0, 0, null);
                //blocks
                for (Block x : blockList) {
                    x.draw(g);
                }
                paddle.draw(g);
                ball.draw(g);
            }
        };

        //Define pause button, it will pause the game and display option window
        buttonPause.setBackground(new Color(236,231,241));
        buttonPause.setPreferredSize(buttonSize);
        buttonPause.setBorder(BorderFactory.createRaisedBevelBorder());
        buttonPause.addActionListener(e -> {
            timer.stop();
            Object[] options = {"Continue", "Done, exit!"};
            int n = JOptionPane.showOptionDialog(
                    getOwner(),
                    "Pause.",
                    "So... are you going to play?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if(n == 0)
                timer.start();
            else if(n == 1)
                System.exit(0);
        });

        //Define restart button,
        // it doesnt restart the game progress but position of the ball
        buttonRestart.setBackground(new Color(236,231,241));
        buttonRestart.setPreferredSize(buttonSize);
        buttonRestart.setBorder(BorderFactory.createRaisedBevelBorder());
        buttonRestart.addActionListener(actionEvent -> {
            //if not started yet
            if(!timer.isRunning()) {
                timer.start();
                buttonRestart.setText("RESTART");
            //else restart the game
            }else {
                timer.stop();
                dispose();
                try {
                    new Game();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });

        //Define JLabel
        scoreLabel = new JLabel();
        Font labelFont = new Font("Dialog", Font.BOLD, 20);
        scoreLabel.setFont(labelFont);
        scoreLabel.setForeground(new Color(236,231,241));
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setPreferredSize(new Dimension(250, 50));
        score = 0;
        scoreLabel.setText("Your score is here!");

        //add panels and buttons
        add(mainPanel);
        mainPanel.add(optionPanel);
        optionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        optionPanel.setBorder(BorderFactory.createLineBorder(new Color(1,22,80), 5));
        optionPanel.setBackground(new Color(24,0,48));
        optionPanel.add(scoreLabel);
        optionPanel.add(buttonPause);
        optionPanel.add(buttonRestart);
        //Set size of the panel and window/layout
        mainPanel.setPreferredSize(new Dimension(600,800));
        FlowLayout mainPanelLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        mainPanel.setLayout(mainPanelLayout);
        optionPanel.setPreferredSize(new Dimension(600, 100));
    }

    //Generate blocks in the list, to generate initial state
    public void generateBlocks(){
        blockList = new CopyOnWriteArrayList<>();
        randomState = new Random(); //for color pick and number of tiles
        //In case of re-initializing, to not have duplicates
        if(!blockList.isEmpty()){
            blockList.clear();
        }

        for(int c = 1;c < 7;c++){
            for (int r = 0; r < 10; r++) {
                //Sets up random start up with ~4/5 tiles in field
                int tiles = randomState.nextInt(5);
                if(tiles < 4) {
                    Block block = new Block(60, 30, r, c, colorList.get(c-1));
                    blockList.add(block);
                }
            }
        }
    }

    public static void main(String[] args){
        //Use concurrency to avoid freezes in JList
        //https://stackoverflow.com/questions/17815320/jlist-doesnt-show-content-50-of-the-time (comments)
        EventQueue.invokeLater(() -> {
            try {
                new Game();
                new PopUp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

