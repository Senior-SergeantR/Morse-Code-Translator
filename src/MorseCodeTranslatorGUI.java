import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//key listener is used here so that i can listen to key press (typing)
public class MorseCodeTranslatorGUI extends JFrame implements KeyListener {
    private MorseCodeController morseCodeController;

    //text input Area - user text to be translated
    //morseCode Area - translated text into morse code
    private JTextArea textInputArea, morseCodeArea;
    public MorseCodeTranslatorGUI(){

        // basically adds text to the title bar
        super("Morse Code Translator");

        //sets the size of the frame  to 560x 760 pixels
        setSize(new Dimension(540,760));

        //prevents GUI from being resized
        setResizable(false);

        //setting the layout to be null allows us to manually position and set the size of the components in our GUI
        setLayout(null);

        //exits the program when closing the GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //changes the color of the background
        getContentPane().setBackground(Color.decode("#264653"));

        //places the GUI at the centre of the screen
        setLocationRelativeTo(null);

        morseCodeController = new MorseCodeController();

        addGUIComponents();

    }

    private void addGUIComponents(){
        //the title label
        JLabel titleLabel = new JLabel("Morse Code Translator");

        //changes the font size for the label and the font weight
        titleLabel.setFont(new Font("Dialog", Font.BOLD,32));

        //changes font color of the text to white
        titleLabel.setForeground(Color.WHITE);

        // centers text relative to its container width
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //sets the x,y position and the width and height dimensions
        // to make sure the title aligns to the center of our GUI, we made it the dame width
        titleLabel.setBounds(0,0,540,100);

        //text input
        JLabel textInputLabel = new JLabel("Text");
        textInputLabel.setFont(new Font("Dialog",Font.BOLD,16));
        textInputLabel.setForeground(Color.WHITE);
        textInputLabel.setBounds(20,100,200,30);

        textInputArea = new JTextArea();
        textInputArea.setFont(new Font("Dialog", Font.PLAIN,18));

        //makes it so that we are listening to key presses whenever we are typing in this text area
        textInputArea.addKeyListener(this);

        //simulates padding of 10px in the text area
        textInputArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //makes it so that words wrap to the next line after reaching the end of the text area
        textInputArea.setLineWrap(true);

        //makes it so that when the words do get wrap, the word doesn't split
        textInputArea.setWrapStyleWord(true);

        //adds scrolling ability to input text area
        JScrollPane textInputScroll = new JScrollPane(textInputArea);
        textInputScroll.setBounds(20,132,484,236);

        //morse code input
        JLabel morseCodeInputLabel = new JLabel("Morse code");
        morseCodeInputLabel.setFont(new Font("Dialog",Font.PLAIN,16));
        morseCodeInputLabel.setForeground(Color.WHITE);
        morseCodeInputLabel.setBounds(20,390,200,30);

        morseCodeArea = new JTextArea();
        morseCodeArea.setFont(new Font("Dialog",Font.PLAIN,18));
        morseCodeArea.setEditable(false);
        morseCodeArea.setLineWrap(true);
        morseCodeArea.setWrapStyleWord(true);
        morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //adds scrolling ability to morse code text area
        JScrollPane morseCodeScroll = new JScrollPane(morseCodeArea);
        morseCodeScroll.setBounds(20,430,484,236);

        //play sound Button
        JButton playSoundButton = new JButton("play sound");
        playSoundButton.setBounds(210,680,100,30);
        playSoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //disable the play button : prevents the sound from being interrupted
                playSoundButton.setEnabled(false);

                Thread playMorseCodeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                            //attempt to play the morse code sound
                        try{
                            String[] morseCodeMessage = morseCodeArea.getText().split(" ");
                            morseCodeController.playSound(morseCodeMessage);
                        } catch (LineUnavailableException lineUnavailableException) {
                            lineUnavailableException.printStackTrace();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }finally {
                            //enable play sound button
                            playSoundButton.setEnabled(true);
                        }
                    }
                });

                playMorseCodeThread.start();
            }
        });


        //add to Gui
        add(titleLabel);
        add(textInputLabel);
        add(textInputScroll);
        add(morseCodeInputLabel);
        add(morseCodeScroll);
        add(playSoundButton);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        //ignore shift key press
        if (e.getKeyCode() != KeyEvent.VK_SHIFT){

            //retrieve text input
            String inputText = textInputArea.getText();

            //update the GUI With the translated text
            morseCodeArea.setText(morseCodeController.translateToMorse(inputText));
        }

    }
}
