//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH

import java.io.File;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public abstract class Enemy extends Moveable implements Hitbox {

    protected int xSpeed;
    protected int tick;
    protected int deathTick;
    protected boolean dying;
    public Image image;
    public Clip clip;

    private int value = 500;

    public Enemy() {
        this(0, 0, 64, 64, 5, 500);
    }

    public Enemy(int x, int y, int w, int h, int s, int val) {
        super(x, y, w, h);
        xSpeed = s;
        dying = false;
        try {
            image = ImageIO.read(new File("assets\\graphics\\pop.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }

        try {
            File soundFile = new File("assets\\sounds\\pop.wav");
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
    }

    //sets point value for killing
    public void setValue(int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }

    public int getLeftEdge() {
        return getX() - xSpeed;
    }

    public int getRightEdge() {
        return getX() + getWidth() + xSpeed;
    }

    public int getTopEdge() {
        return getY();
    }

    public int getBotEdge() {
        return getY() + getHeight();
    }

    //Check if objects are overlapping
    public boolean collision(Hitbox other) {
        return (getRect().intersects(other.getRect()));
    }

    public boolean checkIfDead(PlayerBot player) {
        return (!collision(player) && player.movingRect().intersects(getWeakPoint()));
    }

    public Rectangle getWeakPoint() {
        return new Rectangle(getX() + 1, getY() - 1, getWidth() - 2, 2);
    }

    public Rectangle getRect() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void draw(Graphics window, Camera cam) {
        if (cam.collision(this)) {
            window.drawImage(image, getX() - cam.getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    //start death animation
    public boolean die() {
        if (dying == false) {
            clip.start();
        }
        dying = true;
        xSpeed = 0;
        if (deathTick >= 20) {
            return true;
        }
        return false;
    }

    public boolean dying() {
        return dying;
    }

    //animation tick
    public void tick() {
        tick++;
        if (tick == Integer.MAX_VALUE) {
            tick = 0;
        }
        if (dying) {
            deathTick++;
        }
    }
}
