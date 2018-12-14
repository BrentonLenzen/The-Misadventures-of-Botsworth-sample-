//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH

import java.io.File;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Class for the mechanics of the player avatar
public class PlayerBot extends Moveable implements Hitbox {

    private int xSpeed;
    private int initSpeed;
    private int ySpeed;

    private Image idle1L;
    private Image idle1R;
    private Image idle2L;
    private Image idle2R;
    private Image jumpL;
    private Image jumpR;
    private Image walk1L;
    private Image walk1R;
    private Image walk2L;
    private Image walk2R;
    private Image happy1;
    private Image happy2;
    private Image death;

    private Clip die;
    private Clip jump;

    private static boolean alive = true;
    private static boolean victorious = false;

    //used for animation
    private int tick;
    private boolean facingLeft;
    private boolean falling;
    private boolean moving;

    //conveyorbelt specific 
    public boolean cvRight;
    public boolean cvLeft;

    public PlayerBot(int x, int y, int w, int h, int s) {
        super(x, y, w, h);
        xSpeed = s;
        initSpeed = s;
        ySpeed = 0;

        try {
            idle1L = ImageIO.read(new File("assets\\graphics\\BotsworthIdle1L.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            idle1R = ImageIO.read(new File("assets\\graphics\\BotsworthIdle1R.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            idle2L = ImageIO.read(new File("assets\\graphics\\BotsworthIdle2L.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            idle2R = ImageIO.read(new File("assets\\graphics\\BotsworthIdle2R.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            jumpL = ImageIO.read(new File("assets\\graphics\\BotsworthJumpL.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            jumpR = ImageIO.read(new File("assets\\graphics\\BotsworthJumpR.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            walk1L = ImageIO.read(new File("assets\\graphics\\BotsworthWalk1L.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            walk1R = ImageIO.read(new File("assets\\graphics\\BotsworthWalk1R.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            walk2L = ImageIO.read(new File("assets\\graphics\\BotsworthWalk2L.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            walk2R = ImageIO.read(new File("assets\\graphics\\BotsworthWalk2R.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            happy1 = ImageIO.read(new File("assets\\graphics\\BotsworthHappy1.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            happy2 = ImageIO.read(new File("assets\\graphics\\BotsworthHappy2.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            death = ImageIO.read(new File("assets\\graphics\\deadbots.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }

        try {
            File soundFile = new File("assets\\sounds\\death.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            die = AudioSystem.getClip();
            die.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            File soundFile = new File("assets\\sounds\\jump.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            jump = AudioSystem.getClip();
            jump.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        facingLeft = false;
        falling = false;
        moving = false;
        tick = 0;
        cvRight = false;
        cvLeft = false;
    }

    public void setXSpeed(int s) {
        xSpeed = s;
    }

    @Override
    public void move(String direction) {
        switch (direction) {
            case "LEFT":
                facingLeft = true;
                moving = true;
                setPos(getX() - xSpeed, getY());
                break;
            case "RIGHT":
                facingLeft = false;
                moving = true;
                setPos(getX() + xSpeed, getY());
                break;
            case "VERTICAL":
                setPos(getX(), getY() + ySpeed);
                break;
        }
    }

    public void jump(ArrayList<Block> blocks) {
        boolean grounded = false;
        boolean topHit = false;
        for (Block block : blocks) {
            if (collision(block)) {
                grounded = true;
                jump.stop();
                jump.setMicrosecondPosition(0);
                break;
            }
        }
        topHit = collisionSide(blocks)[3];

        if (grounded && !topHit && ySpeed == 0) {
            ySpeed -= 19;
            move("VERTICAL");
            jump.start();
        }
    }

    //hoping after enemy kill
    public void hop() {
        move("VERTICAL");
        ySpeed = -19;
        move("VERTICAL");
    }

    //Gravity
    public void fall(ArrayList<Block> blocks) {
        boolean grounded = false;
        for (Block block : blocks) {
            if (block instanceof Fan) {
                if (getX() + getWidth() > ((Fan) block).getLeftEdge() && getX() < ((Fan) block).getRightEdge() && getY() > ((Fan) block).getPeakEffect() && getY() < block.getY()) {
                    ySpeed -= 2;
                }
            } else if (collision(block)) {
                if (block instanceof Balloon) {
                    hop();
                } else {
                    grounded = true;
                    if (block instanceof Cloud && tick % 2 == 0) {
                        setPos(getX(), getY() + 1);
                    } else if (block instanceof ConveyorBelt) {
                        if (((ConveyorBelt) block).right()) {
                            cvRight = true;
                            cvLeft = false;
                        } else {
                            cvLeft = true;
                            cvRight = false;
                        }
                    } else {
                        cvLeft = false;
                        cvRight = false;
                    }
                    falling = false;
                    break;
                }
            }
        }
        if (!grounded) {
            if (ySpeed < 12) {
                ySpeed++;
            }
            falling = true;
            cvRight = false;
            cvLeft = false;
            move("VERTICAL");
        } else {
            ySpeed = 0;
        }
    }

    @Override
    public int getLeftEdge() {
        return getX() - xSpeed;
    }

    @Override
    public int getRightEdge() {
        return getX() + getWidth() + xSpeed;
    }

    @Override
    public int getTopEdge() {
        return getY() + ySpeed;
    }

    @Override
    public int getBotEdge() {
        return getY() + getHeight() + ySpeed;
    }

    @Override
    public boolean collision(Hitbox other) {
        return getRightEdge() > other.getLeftEdge() && getLeftEdge() < other.getRightEdge()
                && getTopEdge() < other.getBotEdge()
                && getBotEdge() > other.getTopEdge();
    }

    //check side of collision
    public static int left = 2, top = 3, right = 0, bottom = 1;

    public boolean[] collisionSide(ArrayList<Block> others) {

        boolean[] leftUp = new boolean[4];

        Rectangle[] boxes = new Rectangle[4];
        boxes[0] = new Rectangle(this.getRect().x + this.getRect().width - 1, this.getRect().y + 1, xSpeed + 2, this.getRect().height - 2);
        boxes[1] = new Rectangle(this.getRect().x, this.getRect().y + this.getRect().height - 1, this.getRect().width, ySpeed + 1);
        boxes[2] = new Rectangle(this.getRect().x - xSpeed - 1, this.getRect().y + 1, xSpeed + 2, this.getRect().height - 2);
        if (facingLeft) {
            boxes[3] = new Rectangle(this.getRect().x - xSpeed, this.getRect().y - ySpeed - 2, this.getRect().width + xSpeed, ySpeed + 1);
        } else {
            boxes[3] = new Rectangle(this.getRect().x, this.getRect().y - ySpeed - 2, this.getRect().width + xSpeed, ySpeed + 1);
        }

        for (int b = 0; b < 4; b++) {
            leftUp[b] = false;
        }
        for (Hitbox box : others) {
            for (int bbb = 0; bbb < 4; bbb++) {
                if (!(box instanceof Cloud) && boxes[bbb].intersects(box.getRect())) {
                    leftUp[bbb] = true;
                }
            }
        }

        return leftUp;
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getX(), getY(), getWidth() + xSpeed, getHeight());
    }

    public Rectangle movingRect() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight() + ySpeed);
    }

    //check if Botsworth has died
    public boolean checkIfDead(ArrayList<Enemy> enemies) {
        boolean death = false;
        if (getY() > 775) {
            death = true;
        }
        if (!death) {
            for (Enemy en : enemies) {
                if (en.collision(this)) {
                    death = true;
                    break;
                }
            }
        }
        if (death) {
            kill();
        }
        return death;
    }

    //game over procedure
    public void kill() {
        die.start();
        alive = false;
        setXSpeed(0);
        ySpeed = 0;
    }

    //reset
    public void revive() {
        alive = true;
        facingLeft = false;
        victorious = false;
        die.stop();
        die.setMicrosecondPosition(0);
        setX(0);
        setY(200);
        setXSpeed(initSpeed);
    }

    //tick for animation
    public void tick() {
        tick++;
        if (tick == Integer.MAX_VALUE) {
            tick = 0;
        }
    }

    //stop movement animation
    public void stop() {
        moving = false;
    }

    //change to victory animation
    public void win() {
        victorious = true;
    }

    public void draw(Graphics window, Camera cam) {
        if (alive) {
            if (victorious) {
                if (tick % 30 < 15) {
                    window.drawImage(happy1, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                } else {
                    window.drawImage(happy2, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                }
            } else if (facingLeft) {
                if (falling) {
                    window.drawImage(jumpL, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                } else if (moving) {
                    if (tick % 60 < 15) {
                        window.drawImage(walk1L, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                    } else if (tick % 60 > 30 && tick % 60 <= 45) {
                        window.drawImage(walk2L, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                    } else {
                        window.drawImage(idle2L, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                    }
                } else {
                    if (tick % 30 < 15) {
                        window.drawImage(idle1L, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                    } else {
                        window.drawImage(idle2L, getX() - cam.getX() - xSpeed, getY(), getWidth() + xSpeed, getHeight(), null);
                    }
                }
            } else {
                if (falling) {
                    window.drawImage(jumpR, getX() - cam.getX(), getY(), getWidth() + xSpeed, getHeight(), null);
                } else if (moving) {
                    if (tick % 60 < 15) {
                        window.drawImage(walk1R, getX() - cam.getX(), getY(), getWidth() + xSpeed, getHeight(), null);
                    } else if (tick % 60 > 30 && tick % 60 <= 45) {
                        window.drawImage(walk2R, getX() - cam.getX(), getY(), getWidth() + xSpeed, getHeight(), null);
                    } else {
                        window.drawImage(idle2R, getX() - cam.getX(), getY(), getWidth() + xSpeed, getHeight(), null);
                    }
                } else {
                    if (tick % 30 < 15) {
                        window.drawImage(idle1R, getX() - cam.getX(), getY(), getWidth() + xSpeed, getHeight(), null);
                    } else {
                        window.drawImage(idle2R, getX() - cam.getX(), getY(), getWidth() + xSpeed, getHeight(), null);
                    }
                }
            }
        } else {
            window.drawImage(death, getX() - cam.getX(), getY(), getWidth() + xSpeed, getHeight(), null);
        }
    }

    public String toString() {
        return super.toString();
    }

}
