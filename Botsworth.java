//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

//The main game
public class Botsworth extends JFrame {

    private final static int WIDTH = 1000;
    private final static int HEIGHT = 750;

    Menus menus;
    Level lv;

    int[] scores;
    boolean[] unlocked;
    
    Image icon;
    

    public Botsworth() throws FileNotFoundException, IOException {
        super("THE MISADVENTURES OF BOTSWORTH");
        setSize(WIDTH, HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width - WIDTH) / 2, (dim.height - HEIGHT) / 2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        scores = new int[]{-1, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0};
        unlocked = new boolean[]{false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        load();
        
        try {
            icon = ImageIO.read(new File("assets\\graphics\\cog.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        
        menus = new Menus(scores,unlocked);
        lv = new LV1_1();

        ((Component) lv).setFocusable(true);
        ((Component) menus).setFocusable(true);

        getContentPane().add(menus);
        //getContentPane().add(lv);
        
        setIconImage(icon);
        setVisible(true);
        setResizable(false);
        
        //audio
        try {
        File soundFile = new File("assets\\sounds\\Comic Game Loop - Mischief.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }


        run();
    }

    public void run() throws FileNotFoundException, IOException {
        int level;
        int curr = 0;
        int result = -2;
        int index = 0;
        while (true) {
            level = menus.getLevel();
            System.out.println(level);
            //change to levels
            if (level == 1) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV1_1();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 1;
                    index = 2;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 2) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV1_2();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 2;
                    index = 3;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 3) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV1_3();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 3;
                    index = 4;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 4) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV2_1();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 4;
                    index = 6;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            } 
            if (level == 5) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV2_2();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 5;
                    index = 7;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 6) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV2_3();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 6;
                    index = 8;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 7) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV3_1();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 7;
                    index = 10;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 8) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV3_2();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 8;
                    index = 11;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 9) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV3_3();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 9;
                    index = 12;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 10) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV4_1();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 10;
                    index = 14;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 11) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV4_2();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 11;
                    index = 15;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 12) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV4_3();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 12;
                    index = 16;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 13) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV5_1();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 13;
                    index = 18;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 14) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV5_2();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 14;
                    index = 19;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }
            if (level == 15) {
                menus.enterLevel();
                if (Menus.curt.getY() == -50) {
                    lv = new LV5_3();
                    ((Component) lv).setFocusable(true);
                    menus.currLevel = 0;
                    level = 0;
                    curr = 15;
                    index = 20;
                    getContentPane().remove(menus);

                    lv.reset();

                    getContentPane().add(lv);
                    requestFocusInWindow();
                    transferFocus();
                    setVisible(true);
                }
            }


            
            //back to menus, set high score
            if (lv.getFinishStatus() > -2) {
                result = lv.getFinishStatus();
                //highscore
                if (result > scores[index]) {
                    scores[index] = result;
                    menus.updateScores(scores);
                }
                //unlock level
                if(result >= 0){
                    if(curr == 15){
                        
                    } else if((curr-1) % 3 == 2){
                        unlocked[index+1] = true;
                        unlocked[index+2] = true;
                    } else {
                        unlocked[index+1] = true;
                    }
                    menus.updateUnlocks(unlocked);
                }
                save();
                lv.reset();

                getContentPane().remove(lv);
                getContentPane().add(menus);
                
                requestFocusInWindow();
                transferFocus();
                setVisible(true);
                
                menus.exitLevel();
            }
        }
    }
    
    public void save() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter("saves\\TMOBSAVE.sv"));
        for(int i : scores)
            writer.write(i + " ");
        for(boolean b : unlocked)
            writer.write(b + " ");
        writer.flush();
        writer.close();
    }
    
    public void load(){
        Scanner file;
        try {
            file = new Scanner(new File("saves\\TMOBSAVE.sv"));
        } catch (FileNotFoundException ex) {
            System.out.println("NO SAVES");
            return;
        }
        int i = 0;
        while(file.hasNextInt()){
            scores[i] = file.nextInt();
            i++;
        }
        i = 0;
        while(file.hasNextBoolean()){
            unlocked[i] = file.nextBoolean();
            i++;
        }
    }

    public static void main (String[] args) throws FileNotFoundException, IOException {
        Botsworth run = new Botsworth();
    }

}
