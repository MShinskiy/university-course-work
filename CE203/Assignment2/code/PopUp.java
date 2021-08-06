package code;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class PopUp extends JFrame {
    /**
     * This would be the first screen user sees
     * it has records on it and written introduction into a game
     */

    //Components
    private JPanel mainPanel;
    private JLabel label;
    private JButton play;

    //Borders
    private Border raised;
    private Border lowered;
    private Border compoundBorder;

    //Font
    private Font labelFont;
    private Font listFont;

    //JList to display score
    private JList<String> frameList;
    private DefaultListModel<String> formatModel;

    //File reading/writing related
    private Scanner reader;     //Scanner
    private InputStream recordsRead;    //Reader
    private String[] records;   //store records as Strings
    private String filepath = "records.txt";

    public PopUp() throws IOException {
        initialise();

        pack();
        setTitle("Welcome");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        fileRead();
        addRecords();

        //add KeyListener
        addKeyListener(new KeyboardListener(this));
        setFocusable(true);
        requestFocusInWindow();

    }

    public void initialise(){
        mainPanel = new JPanel();
        label= new JLabel();
        play = new JButton("Enough. Let's start!");

        //JButton to close the window and go to game---
        play.addActionListener(actionEvent -> dispose());
        play.setBackground(Color.WHITE);
        //---------------------------------------------
        //JLabel-------------------------------------
        //Border
        raised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        lowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        compoundBorder = BorderFactory.createCompoundBorder(raised, lowered);

        //Text of label & design
        String text = "Welcome to the game of Brick Breaker." +
                " Above are the records." +
                " Maybe you can get up there too?" +
                " To move the paddle simply use your mouse at the area of the screen." +
                " The game will start when you will press button 'START'." +
                " If the ball goes below the paddle, you will lose." +
                " For full list of rules and features press 'Enter' key," +
                " when at the game screen, before start of the game."
                ;

        //I used HTML because I could find any other way to wrap text, not sure if that's allowed :c
        //Took idea of using html from: https://stackoverflow.com/questions/2420742/make-a-jlabel-wrap-its-text-by-setting-a-max-width
        label.setText("<html><div style = 'text-align: center;'>" + "Hello!" +"<div style = 'text-align: justify;'>"  + text + "<div style = 'text-align: center;'>" + "Good luck!" + "</html>");
        label.setVerticalAlignment(SwingConstants.TOP);
        labelFont = new Font("Monospaced", Font.BOLD, 14);
        label.setFont(labelFont);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        //Settings
        label.setPreferredSize(new Dimension(280, 300));
        label.setBorder(compoundBorder);
        //------------------------------------------
        //JList-------------------------
        formatModel = new DefaultListModel<>();
        frameList = new JList<>(formatModel);

        //JList
        listFont = new Font("Monospaced", Font.BOLD, 15);

        frameList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        frameList.setLayoutOrientation(JList.VERTICAL);
        frameList.setFont(listFont);
        frameList.setPreferredSize(new Dimension(280, 150));
        frameList.setBorder(compoundBorder);
        //-----------------------------

        //JPanel
        //Set the size of the panel and window
        mainPanel.setPreferredSize(new Dimension(300, 500));
        mainPanel.setBackground(Color.BLACK);

        //Adding everything on the panel
        add(mainPanel);
        mainPanel.add(frameList);
        mainPanel.add(label);
        mainPanel.add(play);
    }

    //Add to formatList to display on JList
    public void addRecords(){
        formatModel.addElement(String.format("%6s %20s", "Name", "Score"));
        for(int x = 0; x < 5; x++){
            String name = records[x].substring(0, 3);
            String score = records[x].substring(4);
            String complete = String.format("%-5s %17s", name, score);

            formatModel.addElement(x+1 + ". "  + complete);
        }
    }

    //Read file to extract the records
    public void fileRead(){
        recordsRead = this.getClass().getClassLoader().getResourceAsStream(filepath);
        reader = new Scanner(recordsRead);
        records = new String[5];
        int i = 0;
        //loop to separately add strings and integers
        while (reader.hasNext()) {
            records[i] = reader.nextLine();
            i++;
        }
        reader.close();
    }
}