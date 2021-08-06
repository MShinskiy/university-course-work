package code;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class EndGame extends JFrame {
    /**
     * This is the very last screen in the game
     * in which you can see the highscores and
     * decide whether you want to play again or quit
     */

    //Components
    private JPanel mainPanel;
    private JLabel label;
    private JButton play;
    private JButton stop;

    //Borders' styles
    private Border raised;
    private Border lowered;
    private Border compoundBorder;

    //Fonts
    private Font labelFont;
    private Font listFont;

    //JList to display score
    private JList<String> frameList;
    private DefaultListModel<String> formatModel;

    //File reading/writing related and record related
    private String filepath = "records.txt";
    private Scanner reader;
    private InputStream recordsRead;
    private FileWriter recordsWrite;
    private boolean isRecord = false;
    private String[] recordLine;        //array to store lines
    private String inputName;
    private int inputScore;

    public EndGame(String name, int score) throws IOException {
        inputName = name;
        inputScore = score;
        initialise();

        pack();
        setTitle("Such a loser");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fileRead();
        fileWrite(inputScore, inputName);
        addRecords();
    }

    public void initialise() {
        //Components
        mainPanel = new JPanel();
        label = new JLabel();
        play = new JButton("I wanna lose again");
        stop = new JButton("Nah, byeee");
        formatModel = new DefaultListModel<>();
        frameList = new JList<>(formatModel);


        //JButton-------------------------------------
        //JButton to stop
        stop.addActionListener(actionEvent -> System.exit(0));
        stop.setBackground(Color.WHITE);

        //JButton to play
        play.addActionListener(actionEvent -> {
                    dispose();
            try {
                new Game();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        play.setBackground(Color.WHITE);
        //---------------------------------------------


        //JLabel-------------------------------------
        //Border
        raised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        lowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        compoundBorder = BorderFactory.createCompoundBorder(raised, lowered);

        //Text of label
        String text =
                "Now you can start again," +
                " if you want, leave and do something more useful. " +
                "You will lose anyway, so what is the point?";

        //I used HTML because I could find any other way to wrap text, not sure if that's allowed :c <-sad face
        label.setText(
                "<html>" +
                "<div style = 'text-align: center;'>" + "You tried!" +
                "<div style = 'text-align: justify;'>" + text +
                "<div style = 'text-align: center;'>" + "Try harder next time!" +
                "</html>"
        );
        label.setVerticalAlignment(SwingConstants.TOP);
        labelFont = new Font("Monospaced", Font.BOLD, 14);
        label.setFont(labelFont);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        //Settings
        label.setPreferredSize(new Dimension(280, 130));
        label.setBorder(compoundBorder);
        //------------------------------------------


        //JList-------------------------
        listFont = new Font("Monospaced", Font.BOLD, 15);

        frameList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        frameList.setLayoutOrientation(JList.VERTICAL);
        frameList.setFont(listFont);
        frameList.setPreferredSize(new Dimension(280, 150));
        frameList.setBorder(compoundBorder);
        //-----------------------------


        //Adding everything on the panel
        add(mainPanel);
        mainPanel.add(frameList);
        mainPanel.add(label);
        mainPanel.add(play);
        mainPanel.add(stop);

        //Set the size of the panel and window
        mainPanel.setPreferredSize(new Dimension(300, 330));
        mainPanel.setBackground(new Color(0, 153, 0));
    }

    //Add, sort and update records
    public void fileWrite(int finalScore, String name) throws IOException {
        int newPosition = 6;
        String newLine = name + " " + finalScore;
        //compare with other records
        for(int x = 0; x < recordLine.length; x++){
            if(finalScore > Integer.parseInt(recordLine[x].substring(4))) {
                newPosition = x;
                isRecord = true;
                break;
            }
        }
        if(isRecord){
            //fit score inside a table and sort it
            //if score is a record than
            //replace the last place with the score
            //and move it up until new position
            recordLine[recordLine.length-1] = newLine;
            for(int x = recordLine.length-1; x > newPosition; x--){
                String temp = recordLine[x-1];
                recordLine[x-1] = recordLine[x];
                recordLine[x] = temp;
            }

            //update table
            recordsWrite = new FileWriter(filepath);
            for(int x = 0; x < 5; x++){
              recordsWrite.write(recordLine[x]);
               recordsWrite.write(System.lineSeparator());
            }
            recordsWrite.close();
        }
    }

    //Read the record before writing a new one
    public void fileRead(){
        recordsRead = this.getClass().getClassLoader().getResourceAsStream(filepath);
        reader = new Scanner(recordsRead);
        recordLine = new String[5];
        int i = 0;
        //loop to separately add strings and integers
        while (reader.hasNext()) {
            recordLine[i] = reader.nextLine();
            i++;
        }
        reader.close();
    }

    //add records to the JList to display
    public void addRecords(){
        formatModel.addElement(String.format("%6s %20s", "Name", "Score"));
        for(int x = 0; x < 5; x++){
            String name = recordLine[x].substring(0, 3);
            String score = recordLine[x].substring(4);
            String complete = String.format("%-5s %17s", name, score);

            formatModel.addElement(x+1 + ". "  + complete);
        }
    }
}