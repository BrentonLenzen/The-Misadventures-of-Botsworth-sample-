//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//graphics and controls for game menus
public class Menus extends Canvas implements KeyListener, Runnable {

    private Image selector;
    private Image lvSelect;
    private Image mainMenu;
    private Image credits;
    private Image ch1;
    private Image ch2;
    private Image ch3;
    private Image ch4;
    private Image ch5;
    private Image lock;

    //needed for changing scences
    static Curtain curt = new Curtain(-50, -1000, 1100, 900, 10);
    private boolean changing;
    private int changeIndex;

    private boolean lvChange;

    BufferedImage back;
    Color bgColor;

    //0-W, 1-UP, 2-S, 3-DOWN, 4-ENTER, 5-SPACE
    private boolean[] keys;

    //index of currently displayed menu
    //0-MAIN, 1-LEVELS, 2-OPTIONS
    private int menuIndex;
    //index of currently selected button
    private int buttonIndex;
    //number of buttons on current menu;
    private int numButtons;
    //current level playing
    public int currLevel;

    //if menu is displayed
    private boolean mainVis;
    private boolean lvVis;
    private boolean optionsVis;

    //display story
    private int styIndex;

    //level highscores and display
    static final Font FONT = new Font("Courier", Font.PLAIN, 18);
    private int[] scores;
    private boolean[] unlocked;
    
    //select sound
    public Clip clip;

    public Menus(int[] sc, boolean[] ul) {
        keys = new boolean[11];
        menuIndex = 0;
        buttonIndex = 0;
        currLevel = 0;

        lvChange = false;

        scores = sc;
        unlocked = ul;

        styIndex = 0;

        initMain();

        try {
            selector = ImageIO.read(new File("assets\\graphics\\selector.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            lvSelect = ImageIO.read(new File("assets\\graphics\\levelSelect.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            mainMenu = ImageIO.read(new File("assets\\graphics\\mainMenu.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            credits = ImageIO.read(new File("assets\\graphics\\Credits.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            ch1 = ImageIO.read(new File("assets\\graphics\\ch1.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            ch2 = ImageIO.read(new File("assets\\graphics\\ch2.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            ch3 = ImageIO.read(new File("assets\\graphics\\ch3.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            ch4 = ImageIO.read(new File("assets\\graphics\\ch4.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            ch5 = ImageIO.read(new File("assets\\graphics\\ch5.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            lock = ImageIO.read(new File("assets\\graphics\\lock.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        
         try {
            File soundFile = new File("assets\\sounds\\jump.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(1000, 750));

        this.addKeyListener(this);
        new Thread(this).start();
        setVisible(true);
    }

    public void update(Graphics window) {
        paint(window);
    }

    public void paint(Graphics window) {
        Graphics2D twoDGraph = (Graphics2D) window;
        if (back == null) {
            back = (BufferedImage) (createImage(getWidth(), getHeight()));
        }

        Graphics graphToBack = back.createGraphics();

        graphToBack.setColor(bgColor);
        graphToBack.fillRect(0, 0, 1000, 750);

        //navigating menu
        //up
        if (!lvChange && styIndex == 0 && (keys[0] || keys[1])) {
            buttonIndex--;
            if (buttonIndex < 0) {
                buttonIndex = numButtons - 1;
            }
            keys[0] = false;
            keys[1] = false;
        }
        //down
        if (!lvChange && styIndex == 0 && (keys[2] || keys[3])) {
            buttonIndex++;
            buttonIndex = buttonIndex % numButtons;
            keys[2] = false;
            keys[3] = false;
        }
        //right
        if (!lvChange && menuIndex == 1 && styIndex == 0 && (keys[7] || keys[8])) {
            buttonIndex += 4;
            buttonIndex = buttonIndex % numButtons;
            keys[7] = false;
            keys[8] = false;
        }
        //left
        if (!lvChange && menuIndex == 1 && styIndex == 0 && (keys[9] || keys[10])) {
            buttonIndex -= 4;
            if (buttonIndex < 0) {
                buttonIndex = numButtons + buttonIndex;
            }
            keys[9] = false;
            keys[10] = false;
        }
        //select
        if (keys[4] == true || keys[5]) {
            //main menu
            if (menuIndex == 0) {
                clip.setMicrosecondPosition(0);
                clip.start();
                //to levels
                if (buttonIndex == 0) {
                    sceneChange();
                    changeIndex = 1;
                } //to options
                else if (buttonIndex == 1) {
                    sceneChange();
                    changeIndex = 2;
                } //exit
                else if (buttonIndex == 2) {
                    System.exit(0);
                }
            } //level select
            else if (menuIndex == 1 && styIndex == 0 && unlocked[buttonIndex+1]) {
                clip.setMicrosecondPosition(0);
                clip.start();
                //start each level / display story
                if (buttonIndex == 0) {
                    styIndex = 1;
                } else if (buttonIndex == 1) {
                    currLevel = 1;
                } else if (buttonIndex == 2) {
                    currLevel = 2;
                } else if (buttonIndex == 3) {
                    currLevel = 3;
                } else if (buttonIndex == 4) {
                    styIndex = 2;
                } else if (buttonIndex == 5) {
                    currLevel = 4;
                } else if (buttonIndex == 6) {
                    currLevel = 5;
                } else if (buttonIndex == 7) {
                    currLevel = 6;
                } else if (buttonIndex == 8) {
                    styIndex = 3;
                } else if (buttonIndex == 9) {
                    currLevel = 7;
                } else if (buttonIndex == 10) {
                    currLevel = 8;
                } else if (buttonIndex == 11) {
                    currLevel = 9;
                } else if (buttonIndex == 12) {
                    styIndex = 4;
                } else if (buttonIndex == 13) {
                    currLevel = 10;
                } else if (buttonIndex == 14) {
                    currLevel = 11;
                } else if (buttonIndex == 15) {
                    currLevel = 12;
                } else if (buttonIndex == 16) {
                    styIndex = 5;
                } else if (buttonIndex == 17) {
                    currLevel = 13;
                } else if (buttonIndex == 18) {
                    currLevel = 14;
                } else if (buttonIndex == 19) {
                    currLevel = 15;
                }
            } //credis
            else if (menuIndex == 2) {
                //nothing
            }
            keys[4] = false;
            keys[5] = false;
        }
        //back
        if (keys[6]) {
            if (menuIndex == 0) {
                System.exit(0);
            } else {
                if (styIndex == 0) {
                    sceneChange();
                    changeIndex = 0;
                } else {
                    styIndex = 0;
                }
            }
            keys[6] = false;
        }

        //draw current menu
        if (mainVis) {
            graphToBack.drawImage(mainMenu, 0, 0, this);
            if (buttonIndex == 0) {
                graphToBack.drawImage(selector, 285, 525, this);
            } else if (buttonIndex == 1) {
                graphToBack.drawImage(selector, 340, 602, this);
            } else if (buttonIndex == 2) {
                graphToBack.drawImage(selector, 370, 678, this);
            }
        }

        if (lvVis) {
            graphToBack.drawImage(lvSelect, 0, 0, this);
            //draw selection index
            if (buttonIndex == 0) {
                graphToBack.drawImage(selector, 10, 135, this);
            } else if (buttonIndex == 1) {
                graphToBack.drawImage(selector, 10, 300, this);
            } else if (buttonIndex == 2) {
                graphToBack.drawImage(selector, 10, 455, this);
            } else if (buttonIndex == 3) {
                graphToBack.drawImage(selector, 10, 610, this);
            } else if (buttonIndex == 4) {
                graphToBack.drawImage(selector, 220, 135, this);
            } else if (buttonIndex == 5) {
                graphToBack.drawImage(selector, 220, 300, this);
            } else if (buttonIndex == 6) {
                graphToBack.drawImage(selector, 220, 455, this);
            } else if (buttonIndex == 7) {
                graphToBack.drawImage(selector, 220, 610, this);
            } else if (buttonIndex == 8) {
                graphToBack.drawImage(selector, 415, 135, this);
            } else if (buttonIndex == 9) {
                graphToBack.drawImage(selector, 415, 300, this);
            } else if (buttonIndex == 10) {
                graphToBack.drawImage(selector, 415, 455, this);
            } else if (buttonIndex == 11) {
                graphToBack.drawImage(selector, 415, 610, this);
            } else if (buttonIndex == 12) {
                graphToBack.drawImage(selector, 615, 135, this);
            } else if (buttonIndex == 13) {
                graphToBack.drawImage(selector, 615, 300, this);
            } else if (buttonIndex == 14) {
                graphToBack.drawImage(selector, 615, 455, this);
            } else if (buttonIndex == 15) {
                graphToBack.drawImage(selector, 615, 610, this);
            } else if (buttonIndex == 16) {
                graphToBack.drawImage(selector, 820, 135, this);
            } else if (buttonIndex == 17) {
                graphToBack.drawImage(selector, 820, 300, this);
            } else if (buttonIndex == 18) {
                graphToBack.drawImage(selector, 820, 455, this);
            } else if (buttonIndex == 19) {
                graphToBack.drawImage(selector, 820, 610, this);
            }
            graphToBack.setColor(Color.WHITE);
            graphToBack.setFont(FONT);
            if (scores[buttonIndex + 1] != -1) {
                graphToBack.drawString("HIGH SCORE: " + scores[buttonIndex + 1], 405, 700);
            }
            
            //draw locked levels
            if(!unlocked[3]){
                graphToBack.drawImage(lock, 130, 450, this);
            }
            if(!unlocked[4]){
                graphToBack.drawImage(lock, 130, 605, this);
            }
            if(!unlocked[5]){
                graphToBack.drawImage(lock, 363, 130, this);
            }
            if(!unlocked[6]){
                graphToBack.drawImage(lock, 340, 295, this);
            }
            if(!unlocked[7]){
                graphToBack.drawImage(lock, 340, 450, this);
            }
            if(!unlocked[8]){
                graphToBack.drawImage(lock, 340, 605, this);
            }
            if(!unlocked[9]){
                graphToBack.drawImage(lock, 563, 130, this);
            }
            if(!unlocked[10]){
                graphToBack.drawImage(lock, 535, 295, this);
            }
            if(!unlocked[11]){
                graphToBack.drawImage(lock, 535, 450, this);
            }
            if(!unlocked[12]){
                graphToBack.drawImage(lock, 535, 605, this);
            }
            if(!unlocked[13]){
                graphToBack.drawImage(lock, 763, 130, this);
            }
            if(!unlocked[14]){
                graphToBack.drawImage(lock, 735, 295, this);
            }
            if(!unlocked[15]){
                graphToBack.drawImage(lock, 735, 450, this);
            }
            if(!unlocked[16]){
                graphToBack.drawImage(lock, 735, 605, this);
            }
            if(!unlocked[17]){
                graphToBack.drawImage(lock, 963, 130, this);
            }
            if(!unlocked[18]){
                graphToBack.drawImage(lock, 940, 295, this);
            }
            if(!unlocked[19]){
                graphToBack.drawImage(lock, 940, 450, this);
            }
            if(!unlocked[20]){
                graphToBack.drawImage(lock, 940, 605, this);
            }
            
            //draw story
            if (styIndex == 1){
                graphToBack.drawImage(ch1, 125, 95, 750, 560, this);
            } else if (styIndex == 2){
                graphToBack.drawImage(ch2, 125, 95, 750, 560, this);
            } else if (styIndex == 3){
                graphToBack.drawImage(ch3, 125, 95, 750, 560, this);
            } else if (styIndex == 4){
                graphToBack.drawImage(ch4, 125, 95, 750, 560, this);
            } else if (styIndex == 5){
                graphToBack.drawImage(ch5, 125, 95, 750, 560, this);
            }   
            
        }
        if (optionsVis) {
            graphToBack.drawImage(credits, 0, 0, this);
        }

        //curtain raise/lower upon reset
        if ((lvChange || changing) && (curt.getY() != -50 || curt.getY() != 900)) {
            curt.move("LOWER");
            curt.draw(graphToBack);
        }
        if (changing && curt.getY() == -50) {
            if (changeIndex == 0) {
                initMain();
            }
            if (changeIndex == 1) {
                initLevels();
            }
            if (changeIndex == 2) {
                initOptions();
            }
            curt.move("LOWER");
        }
        if (changing && curt.getY() == 900) {
            curt.move("RAISE");
            changing = false;
        }

        twoDGraph.drawImage(back, null, 0, 0);
    }

    //lowers curtain to change menus
    public void sceneChange() {
        changing = true;
    }

    //enter and exit levels - curtain change
    public void enterLevel() {
        lvChange = true;
    }

    public void exitLevel() {
        lvChange = false;
        changing = true;
        curt.move("LOWER");
    }

    //initialize variables for scenes
    public void initMain() {
        menuIndex = 0;
        buttonIndex = 0;
        numButtons = 3;
        mainVis = true;
        lvVis = false;
        optionsVis = false;
        bgColor = Color.BLACK;
    }

    public void initLevels() {
        menuIndex = 1;
        buttonIndex = 0;
        numButtons = 20;
        mainVis = false;
        lvVis = true;
        optionsVis = false;
        bgColor = Color.GREEN;
    }

    public void initOptions() {
        menuIndex = 2;
        buttonIndex = 0;
        numButtons = 0;
        mainVis = false;
        lvVis = false;
        optionsVis = true;
        bgColor = Color.BLACK;
    }

    //called by Botsworth - gets level to be displayed
    public int getLevel() {
        return currLevel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //does not need to be implemented
    }

    //controls menu navigation
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keys[0] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[1] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keys[2] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[3] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            keys[4] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            keys[5] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keys[6] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            keys[7] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keys[8] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keys[9] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keys[10] = true;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keys[0] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[1] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keys[2] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[3] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            keys[4] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            keys[5] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keys[6] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            keys[7] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keys[8] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keys[9] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keys[10] = false;
        }
        repaint();
    }

    public void updateScores(int[] sc) {
        for (int i = 0; i < scores.length; i++) {
            scores[i] = sc[i];
        }
    }
    
    public void updateUnlocks(boolean[] ul) {
        for(int i = 0; i < ul.length; i++) {
            unlocked[i] = ul[i];
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.currentThread().sleep(15);
                repaint();

            }
        } catch (Exception e) {
        }
    }

}
