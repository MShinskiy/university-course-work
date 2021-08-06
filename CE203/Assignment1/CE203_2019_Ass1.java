import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Program made by Maxim id: 1804336
 * The main() is in method contents()
 */

public class CE203_2019_Ass1 extends JFrame {
    //Initialize components
    //Textfields
    JTextField inputField;
    JTextField redInput;
    JTextField greenInput;
    JTextField blueInput;
    //Label
    JLabel disLabel;    //Show message
    //Buttons
    private JButton displayB;
    private JButton addB;
    private JButton clearB;
    private JButton removeB;
    private JButton printListB;
    private JButton colorB;
    //ArrayList and Iterator related
    private List<String> wordList;  //Store strings
    private Iterator<String> go;    //Iterate through List
    //JList related
    private DefaultListModel<String> formatModel;
    private JList<String> frameList;

    //----------Methods-----------------------
    /*Check if word contains anything other then:
                letters, numbers and hyphen (-) */
    private boolean isLegal(String word) {
        boolean rState = false;
        if(word.length() > 0) {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (i == 0) {
                    rState = (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
                    if(!rState) return false;
                } else {
                    rState = (c == 45) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c >= 48 && c <= 57);
                    if (!rState) return false;
                }
            }
        }
        return rState;
    }

    //Check if input for display func is legal
    boolean isLegalDisplay(char c) {
        return (c == 45 || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c >= 48 && c <= 57));
    }

    //Check for color range
    boolean isNotLegalRange (int r, int g, int b) {
        return ((r > 255 || r < 0) || (g > 255 || g < 0) ||(b > 255 || b < 0));
    }

    //Add to List
    String listAdd(String word) {
        String defReturn = "First enter a string to add";
        if(word.length() > 0) {
            if(isLegal(word)) {
                wordList.add(word);
                defReturn = "'" + word + "' has been added";
            } else if(!isLegal(word)) {
                defReturn = "Illegal string";
            }
        }
        return defReturn;
    }

    //Print whole list
    String listDisplayWhole(){
        formatModel.removeAllElements();
        //Need Iterator here to avoid ConcurrentModificationException
        go = wordList.iterator();
        String rState = "Empty ArrayList";
        while(go.hasNext()) {
            String temp = go.next();
            formatModel.addElement(temp);
            rState = "List printed";
        }
        return  rState;
    }

    //Display list
    String listDisplay(char letter){
        if(letter == 0) {
            return "Illegal character";
        }
        formatModel.removeAllElements();
        //Need Iterator here to avoid ConcurrentModificationException
        go = wordList.iterator();
        String rState = "Empty ArrayList";
        int count = 0;
        while(go.hasNext()) {
            String temp = go.next();
            //Bringing word and a letter to the same condition
            String LCTemp = temp.toLowerCase();
            letter = Character.toLowerCase(letter);
            if(LCTemp.charAt(LCTemp.length()-1) == letter) {
                formatModel.addElement(temp);
                count++;
            }
            rState = "Words with: '" + letter + "' at the end are displayed";
        }
        //No matching elements
        if (count == 0 && wordList.size() > 0) {
            rState = "No elements with '" + letter +"' in the end";
        }
        return rState;
    }

    //Clear list
    String listClear() {
        String rState;
        if(wordList.size() > 0) {
            wordList.clear();
            rState = "List Cleared ";
        } else {
            rState = "List is empty anyway";
        }
        return rState;
    }

    //Remove specified word from the list
    String listRemove(String word) {
        //Need Iterator here to avoid ConcurrentModificationException
        go = wordList.iterator();
        //bring both strings to the same condition
        String fromInput = word.toLowerCase();
        String rState = "Empty ArrayList";
        int count = 0;
        while (go.hasNext()) {
            String fromArray = go.next().toLowerCase();
            if (fromArray.equals(fromInput)) {
                go.remove();
                rState = "All occurrences of '" + fromInput + "' were removed";
                count++;
            }
        }
        //No matching elements
        if (count == 0 && wordList.size() > 0){
            rState = "No elements matching '" + word +"'";
        }
        return rState;
    }

    //Random Color
    void colorRandom() {
        Random fun = new Random();
        int r;
        int g;
        int b;
        //create first random color
        r = fun.nextInt(256);
        g = fun.nextInt(256);
        b = fun.nextInt(256);
        Color randomColor = new Color(r, g, b);

        //method to show the actual color of components
        colorRep(r, g, b);

        //else elements to color into random color
        disLabel.setForeground(randomColor);
        colorB.setBackground(randomColor);
        frameList.setForeground(randomColor);
        inputField.setForeground(randomColor);
        addB.setForeground(randomColor);
        removeB.setForeground(randomColor);
        displayB.setForeground(randomColor);
        printListB.setForeground(randomColor);
        clearB.setForeground(randomColor);

        //Create opposite color for better contrast
        r = 255 - r;
        g = 255 - g;
        b = 255 - b;
        randomColor = new Color(r, g, b);

        //Set new color to some of the components
        colorB.setForeground(randomColor);
        frameList.setSelectionForeground(randomColor);
    }

    //Change to specified color
    void colorSpecial(int r, int g, int b) {
        //Color
        Color newColor = new Color(r, g, b);
        /*
        Change the color of components into
        specified color
        */
        disLabel.setForeground(newColor);
        addB.setForeground(newColor);
        removeB.setForeground(newColor);
        displayB.setForeground(newColor);
        printListB.setForeground(newColor);
        clearB.setForeground(newColor);
        frameList.setForeground(newColor);
        inputField.setForeground(newColor);

        //method to show the actual color of components
        colorRep(r, g, b);
    }

    /*
    Represent color.
    This method will color the input-textfields
    into colors of their component
     */
    private void colorRep(int r, int g, int b) {
        //red input box
        Color redComp = new Color(r, 0, 0);
        redInput.setText(Integer.toString(r));
        redInput.setBackground(redComp);

        //green input box
        Color greenComp = new Color(0, g, 0);
        greenInput.setText(Integer.toString(g));
        greenInput.setBackground(greenComp);

        //blue input box
        Color blueComp = new Color(0, 0, b);
        blueInput.setText(Integer.toString(b));
        blueInput.setBackground(blueComp);
    }

    //Draw the frame
    private CE203_2019_Ass1() {
        component();
        setSize(816, 600);
    }

    private void component() {
        //JList
        formatModel = new DefaultListModel<>();
        frameList = new JList<>(formatModel);

        //ScrollPane for JList
        JScrollPane scroll = new JScrollPane(frameList);

        //ArrayList
        wordList = new ArrayList<>();

        //Label to print the list
        disLabel = new JLabel("Start the program by adding elements into ArrayList");

        //Define Panels
        JPanel buttonPanel = new JPanel();
        JPanel inputPanel = new JPanel();
        JPanel outputPanel = new JPanel();

        //TextField for input
        inputField = new JTextField("Input Field");
        redInput = new JTextField("Red");
        greenInput = new JTextField("Green");
        blueInput = new JTextField("Blue");

        //Define buttons
        displayB = new JButton("Display");
        addB = new JButton("Add");
        removeB = new JButton("Remove");
        clearB = new JButton("Clear");
        printListB = new JButton("Print List");
        colorB = new JButton("Tap 'N' Swap");

        //ActionListeners for buttons
        displayB.addActionListener(new ButtonHandler(this, 1));
        addB.addActionListener(new ButtonHandler(this, 2));
        removeB.addActionListener(new ButtonHandler(this, 3));
        clearB.addActionListener(new ButtonHandler(this, 4));
        printListB.addActionListener(new ButtonHandler(this, 5));
        colorB.addActionListener(new ButtonHandler(this, 6));

        //Place panels
        add(buttonPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.SOUTH);
        add(outputPanel, BorderLayout.CENTER);

        //Place items on the panels
        //Buttons
        buttonPanel.add(displayB);
        buttonPanel.add(addB);
        buttonPanel.add(removeB);
        buttonPanel.add(clearB);
        buttonPanel.add(printListB);

        //Output panel
        outputPanel.add(disLabel);
        outputPanel.add(scroll);

        //More specific layout for BorderLayout.NORTH(buttonPanel)
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));

        //More specific layout for BorderLayout.CENTER(outputPanel)
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        disLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frameList.setAlignmentX(Component.CENTER_ALIGNMENT);

        //More specific layout for BorderLayout.SOUTH(inputPanel)
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcInput = new GridBagConstraints();
        GridBagConstraints gbcRed = new GridBagConstraints();
        GridBagConstraints gbcGreen = new GridBagConstraints();
        GridBagConstraints gbcBlue = new GridBagConstraints();
        GridBagConstraints gbcButton = new GridBagConstraints();

        //Input
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.gridy = 0;
        gbcInput.gridx = 0;
        gbcInput.gridwidth = 4;
        //Red
        gbcRed.fill = GridBagConstraints.HORIZONTAL;
        gbcRed.gridy = 1;
        gbcRed.gridx = 0;
        //Green
        gbcGreen.fill = GridBagConstraints.HORIZONTAL;
        gbcGreen.gridy = 1;
        gbcGreen.gridx = 1;
        //Blue
        gbcBlue.fill = GridBagConstraints.HORIZONTAL;
        gbcBlue.gridy = 1;
        gbcBlue.gridx = 2;
        //Button
        gbcButton.fill = GridBagConstraints.HORIZONTAL;
        gbcButton.gridy = 1;
        gbcButton.gridx = 3;

        //Input panel
        inputPanel.add(inputField, gbcInput);
        inputPanel.add(redInput, gbcRed);
        inputPanel.add(greenInput, gbcGreen);
        inputPanel.add(blueInput, gbcBlue);
        inputPanel.add(colorB ,gbcButton);

        //Design########################
        //FONT--------------------------
        //Create fonts------------------
        Font labelFont = new Font("Helvetica", Font.ITALIC, 25);
        Font buttonFont = new Font("Helvetica", Font.BOLD, 20);
        Font JListFont = new Font("Helvetica", Font.BOLD, 20);
        Font inputFont = new Font("Helvetica", Font.ITALIC, 25);
        Font color_inputFont = new Font("Monospaced", Font.BOLD, 18);
        //Set-Fonts---------------------
        disLabel.setFont(labelFont);

        displayB.setFont(buttonFont);
        addB.setFont(buttonFont);
        removeB.setFont(buttonFont);
        clearB.setFont(buttonFont);
        printListB.setFont(buttonFont);

        frameList.setFont(JListFont);

        inputField.setFont(inputFont);
        redInput.setFont(color_inputFont);
        greenInput.setFont(color_inputFont);
        blueInput.setFont(color_inputFont);

        //JLIST MODEL-------------------
        //JList-------------------------
        frameList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        frameList.setLayoutOrientation(JList.VERTICAL);
        //Scroll Pane for JList---------
        scroll.setViewportView(frameList);

        //SIZE--------------------------
        int width_InD = 800;
        int diff_RGB = 20;
        int height = diff_RGB*2;
        int width_RGB = width_InD/4 - diff_RGB;
        int width_B = width_InD - width_RGB*4;

        Dimension sizeB = new Dimension(800/5, 80);
        Dimension sizeRGB = new Dimension(width_RGB, height);

        displayB.setPreferredSize(sizeB);
        addB.setPreferredSize(sizeB);
        removeB.setPreferredSize(sizeB);
        clearB.setPreferredSize(sizeB);
        printListB.setPreferredSize(sizeB);
        colorB.setPreferredSize(new Dimension(width_B, height));
        inputField.setPreferredSize(new Dimension(width_InD, height));
        redInput.setPreferredSize(sizeRGB);
        greenInput.setPreferredSize(sizeRGB);
        blueInput.setPreferredSize(sizeRGB);

        //COLOR-------------------------
        //Set-background-color----------
        displayB.setBackground(Color.LIGHT_GRAY);
        addB.setBackground(Color.LIGHT_GRAY);
        removeB.setBackground(Color.LIGHT_GRAY);
        clearB.setBackground(Color.LIGHT_GRAY);
        printListB.setBackground(Color.LIGHT_GRAY);
        colorB.setBackground(Color.LIGHT_GRAY);
        //Other-colors------------------
        Color antiRed = new Color(0, 255, 255);
        Color antiGreen = new Color(255, 0, 255);
        Color antiBlue = new Color(255, 255, 0);
        redInput.setForeground(antiRed);
        greenInput.setForeground(antiGreen);
        blueInput.setForeground(antiBlue);

        //Alignment---------------------
        inputField.setHorizontalAlignment(JTextField.CENTER);
        redInput.setHorizontalAlignment(JTextField.CENTER);
        greenInput.setHorizontalAlignment(JTextField.CENTER);
        blueInput.setHorizontalAlignment(JTextField.CENTER);
        //##############################
    }

    public static void main(String[] args) {
        CE203_2019_Ass1 frame = new CE203_2019_Ass1();
        frame.setTitle("Assignment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class ButtonHandler implements ActionListener {
    private  CE203_2019_Ass1 theApp;
    private int action;

    ButtonHandler(CE203_2019_Ass1 theApp, int action) {
        this.theApp = theApp;
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        //Variables--------------
        //Color Input
        int red, green, blue;

        try {
            red = Integer.parseInt(theApp.redInput.getText());
            green = Integer.parseInt(theApp.greenInput.getText());
            blue = Integer.parseInt(theApp.blueInput.getText());
            if (this.theApp.isNotLegalRange(red, green, blue)) {
                red = 0;
                green = 0;
                blue = 0;
                JOptionPane.showMessageDialog(theApp,
                        "One or more inputs are in wrong range. " +
                        "Change your input for colour. " +
                        "Please enter integers in the range 0 <= integer <= 255.",
                        "OutOfRangeException",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }catch (NumberFormatException ex) {
            red = 0;
            green = 0;
            blue = 0;
            JOptionPane.showMessageDialog(theApp,
                    "Wrong format. Please enter integers in the range 0 <= integer <= 255.",
                    "WrongFormatException",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        //Else input
        String aInput = theApp.inputField.getText();
        char charInput;
        if (aInput.length() > 0 && this.theApp.isLegalDisplay(aInput.charAt(aInput.length() - 1))) {
            charInput = aInput.charAt(aInput.length() - 1);
        } else {
            charInput = 0;
        }
        //Action----------
        //Display
        if(this.action == 1) {
            this.theApp.disLabel.setText(this.theApp.listDisplay(charInput));
            this.theApp.colorSpecial(red, green , blue);
        }

        //Add
        if(this.action == 2) {
            this.theApp.disLabel.setText(this.theApp.listAdd(aInput));
            this.theApp.colorSpecial(red, green , blue);
        }

        //Remove matching elements
        if(this.action == 3) {
            this.theApp.disLabel.setText(this.theApp.listRemove(aInput));
            this.theApp.listDisplayWhole();
            this.theApp.colorSpecial(red, green , blue);
        }

        //Clear list
        if(this.action == 4) {
            this.theApp.disLabel.setText(this.theApp.listClear());
            this.theApp.listDisplayWhole();
            this.theApp.colorSpecial(red, green , blue);
        }

        //Print whole ArrayList
        if(this.action == 5) {
            this.theApp.disLabel.setText(this.theApp.listDisplayWhole());
            this.theApp.colorSpecial(red, green , blue);
        }

        //Change color
        if(this.action == 6) {
            this.theApp.colorRandom();
        }
    }
}