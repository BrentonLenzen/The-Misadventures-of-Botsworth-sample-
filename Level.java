//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//Abstract class - Individual levels will be derived from this
public abstract class Level extends Canvas implements KeyListener, Runnable {

    ArrayList<Block> blocks;
    ArrayList<Cog> cogs;
    ArrayList<Enemy> enemies;
    ChargingStation levelEnd;
    PlayerBot player;
    Camera cam;

    //loading
    //top at -50, -1000 // 1100,900
    static Curtain curt = new Curtain(-50, -50, 1100, 900, 10);
    //level start and end
    Messages mess;

    boolean[] keys;
    boolean[] playerCollisions;

    boolean resetting;
    boolean exiting;
    boolean paused;
    boolean done;
    boolean finish;
    boolean exit;

    int highscore;
    int score;
    int time;
    int tick;

    BufferedImage back;
    Image bg;
    Color bgColor;
    Color ogBG;

    //Pause menu mechanics
    Image menu;
    Image selector;
    int menuIndex;

    String fileName;

    static final Font FONT = new Font("Courier", Font.PLAIN, 18);

    public Level() throws FileNotFoundException {
        keys = new boolean[10];
        mess = new Messages(600, 275, 350, 200, 5);
        player = new PlayerBot(0, 200, 64, 64, 4);
        mess.getPlayerX(player);
        cam = new Camera(-350, 0, 1000, 750, 4);
        resetting = true;
        exiting = false;
        paused = false;
        done = false;
        finish = false;
        exit = false;
        score = 0;
        time = 0;
        tick = 0;
        //temporary arraylist set
        blocks = new ArrayList<Block>();
        cogs = new ArrayList<Cog>();
        enemies = new ArrayList<Enemy>();
        setPreferredSize(new Dimension(1000, 750));

        bgColor = Color.BLACK;
        ogBG = bgColor;

        try {
            menu = ImageIO.read(new File("assets\\graphics\\pause.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        try {
            selector = ImageIO.read(new File("assets\\graphics\\pauseSelect.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE");
        }
        
        bg = null;
        
        menuIndex = 0;

        fileName = "";

        //necessary for testing
        levelEnd = new ChargingStation();

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

        playerCollisions = player.collisionSide(blocks);

        //other movement
        if (!finish && !paused && !playerCollisions[2] && player.cvLeft) {
            player.move("LEFT");
            cam.move("LEFT");
        }
        if (!finish && !paused && !playerCollisions[0] && player.cvRight) {
            player.move("RIGHT");
            cam.move("RIGHT");
        }
        //Key ==> Movement
        if (!finish && !paused && !playerCollisions[2] && (keys[0] || keys[1])) {
            player.move("LEFT");
            cam.move("LEFT");

        }
        if (!finish && !paused && !playerCollisions[0] && (keys[2] || keys[3])) {
            player.move("RIGHT");
            cam.move("RIGHT");

        }
        if (!finish && !keys[0] && !keys[1] && !keys[2] && !keys[3]) {
            player.stop();
        }

        //JUMP
        if (!paused && keys[4]) {
            player.jump(blocks);
            keys[4] = false;
        }

        //Pause
        if (!finish && keys[5] && !paused) {
            paused = true;
            menuIndex = 0;
            keys[5] = false;
        }
        //Unpause
        if (!finish && keys[5] && paused) {
            paused = false;
            keys[5] = false;
        }
        //menu mechanics
        if (paused && (keys[6] || keys[7])) {
            menuIndex--;
            if (menuIndex < 0) {
                menuIndex = 2;
            }
            keys[6] = false;
            keys[7] = false;
        }
        if (paused && (keys[8] || keys[9])) {
            menuIndex++;
            menuIndex = menuIndex % 3;
            keys[8] = false;
            keys[9] = false;
        }
        if (paused && keys[4]) {
            if (menuIndex == 0) {
                paused = false;
            }
            if (menuIndex == 1) {
                resetting = true;
            }
            if (menuIndex == 2) {
                exiting = true;
            }
        }

        //remove uneccessary items
        for (int i = cogs.size() - 1; i >= 0; i--) {
            if (cogs.get(i).collision(player)) {
                score += cogs.get(i).getValue();
                cogs.get(i).sound();
                cogs.remove(i);
            }
        }

        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).checkIfDead(player)) {
                player.hop();
                score += enemies.get(i).getValue();
                enemies.get(i).die();
            }
            if (enemies.get(i).dying()) {
                if (enemies.get(i).die()) {
                    enemies.remove(i);
                }
            }
        }

        if (player.checkIfDead(enemies)) {
            cam.kill();
            resetting = true;
            bgColor = Color.RED;
        }

        //gravity
        if (!paused) {
            player.fall(blocks);
        }

        //finish scenario
        if (player.getX() >= levelEnd.getFinishPos() && !mess.done) {
            player.win();
            player.stop();
            finish = true;
            mess.done = true;
            mess.getPlayerX(player);
            mess.move("RESET");
        }

        //drawing
        graphToBack.drawImage(bg, 0, 0, this);
        
        player.draw(graphToBack, cam);

        for (Block block : blocks) {
            block.draw(graphToBack, cam);
            //animate certain blocks
            if (!paused && block instanceof Fan || block instanceof ConveyorBelt) {
                block.tick();
            }
        }

        for (Cog cog : cogs) {
            cog.draw(graphToBack, cam);
        }

        for (Enemy en : enemies) {
            en.draw(graphToBack, cam);
            if (!paused) {
                en.tick();
                en.move(null);
            }
        }

        levelEnd.draw(graphToBack, cam);

        //start/stop messages
        //start
        if (!mess.done && mess.getX() > -2000) {
            if (!paused) {
                mess.move("MOVE");
            }
            mess.draw(graphToBack, cam);
        } //stop
        else if (mess.done) {
            if (mess.getX() - cam.getX() > 325) {
                mess.move("MOVE");
            }
            mess.draw(graphToBack, cam);
        }
        if (mess.done && mess.getX() - cam.getX() == 325) {
            if (mess.endTick > 150) {
                exiting = true;
            } else {
                mess.tick();
            }
        }

        //pause menu
        if (paused) {
            graphToBack.drawImage(menu, 400, 300, 200, 150, null);
            if (menuIndex == 0) {
                graphToBack.drawImage(selector, 425, 345, 16, 16, null);
            }
            if (menuIndex == 1) {
                graphToBack.drawImage(selector, 425, 379, 16, 16, null);
            }
            if (menuIndex == 2) {
                graphToBack.drawImage(selector, 425, 409, 16, 16, null);
            }
        }

        graphToBack.setColor(Color.WHITE);
        graphToBack.setFont(FONT);
        graphToBack.drawString("SCORE: " + score, 825, 70);
        graphToBack.drawString("TIME: " + time, 825, 90);

        //curtain raise/lower upon reset or exit
        if ((resetting || exiting) && (curt.getY() != -50 || curt.getY() != 900)) {
            curt.move("LOWER");
            curt.draw(graphToBack);
        }
        if (resetting && curt.getY() == -50) {
            try {
                reset();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            curt.move("LOWER");
        }
        if (exiting && curt.getY() == -50) {
            if (finish) {
                done = true;
            } else {
                exit = true;
            }
            exiting = false;
        }
        if (resetting && curt.getY() == 900) {
            curt.move("RAISE");
            resetting = false;
        }

        //tick for animation
        if (!paused) {
            player.tick();
            levelEnd.tick();
        }

        twoDGraph.drawImage(back, null, 0, 0);
    }

    //Key Controls
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keys[0] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keys[1] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keys[2] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            keys[3] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            keys[4] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keys[5] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[6] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keys[7] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[8] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keys[9] = true;
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keys[0] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keys[1] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keys[2] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            keys[3] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            keys[4] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keys[5] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[6] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keys[7] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[8] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keys[9] = false;
        }
        repaint();
    }

    //reset level
    public void reset() throws FileNotFoundException {
        cam.kill();

        bgColor = ogBG;

        score = 0;
        blocks.clear();
        enemies.clear();
        cogs.clear();
        buildLevel(fileName);
        player.revive();
        cam.revive();
        mess.getPlayerX(player);
        mess.move("RESET");
        tick = 0;
        time = 0;

        done = false;
        exit = false;
        finish = false;

        paused = false;
    }

    //returns finish score; if exit, return -1
    public int getFinishStatus() {
        if (done) {
            for (int i = 0; i < keys.length; i++) {
                keys[i] = false;
            }
            //done = false;
            return score;
        } else if (exit) {
            for (int i = 0; i < keys.length; i++) {
                keys[i] = false;
            }
            //exit = false;
            return -1;
        } else {
            return -2;
        }
    }

    public void run() {
        try {
            while (true) {
                Thread.currentThread().sleep(15);
                if (!paused) {
                    tick++;
                }
                if (tick % 75 == 0) {
                    time++;
                }
                repaint();
            }
        } catch (Exception e) {
        }
    }

    public void buildLevel(String file) throws FileNotFoundException {
        String[][] chars = getBlocks(file);
        int y = -16;
        for (int r = 0; r < chars.length; r++) {
            int x = -512;

            for (int c = 0; c < chars[r].length; c++) {
                switch (chars[r][c]) {
                    //blocks
                    case "/":
                        break;
                    case "|":
                        blocks.add(new Wall(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "G":
                        blocks.add(new Grass(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "F":
                        blocks.add(new Fence(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "B":
                        blocks.add(new Crate(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "J":
                        blocks.add(new Junk(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "P":
                        blocks.add(new Plating(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "A":
                        blocks.add(new Asphalt(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "S":
                        blocks.add(new Scaffolding(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;

                    //special blocks/hazards
                    case "~":
                        blocks.add(new Fan(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "C":
                        blocks.add(new Cloud(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case "W":
                        enemies.add(new Water(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 0, 0));
                        break;
                    case "0":
                        blocks.add(new Balloon(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64));
                        break;
                    case ">":
                        blocks.add(new ConveyorBelt(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64, true));
                        break;
                    case "<":
                        blocks.add(new ConveyorBelt(x, y, getWidth(chars[r], chars[r][c], c) * 64, 64, 64, false));
                        break;

                    //special
                    case "+":
                        cogs.add(new Cog(x + 16, y + 16, 32, 32));
                        break;

                    //enemies
                    case "R":
                        enemies.add(new Rat(x, y + 40, 64, 24, 6, 100, true));
                        break;
                    case "r":
                        enemies.add(new Rat(x, y + 40, 64, 24, 6, 100, false));
                        break;
                    case "V":
                        enemies.add(new Raven(x, y + 40, 64, 24, 5, 200, true));
                        break;
                    case "v":
                        enemies.add(new Raven(x, y + 40, 64, 24, 5, 200, false));
                        break;
                    case "8":
                        enemies.add(new Fish(x + 4, y + 22, 22, 48, 5, 250));
                        break;
                    case "E":
                        enemies.add(new EvilBot(x, y, 69, 64, 5, 350, true));
                        break;
                    case "e":
                        enemies.add(new EvilBot(x, y, 69, 64, 5, 350, false));
                        break;
                    case "X":
                        enemies.add(new FanBot(x+2, y+15, 60, 25, 3, 200, true));
                        break;
                    case "x":
                        enemies.add(new FanBot(x+2, y+15, 60, 25, 3, 200, false));
                        break;
                        

                    //BOSS
                    case "1":
                        enemies.add(new UFO(x,y,96,96,3,200));
                        break;
                    case "2":
                        enemies.add(new RatKing(x,y-32,96,96,6,200));
                        break;
                    case "3":
                        enemies.add(new FanBoss(x,y,270,113,2,300));
                        break;
                    case "4":
                        enemies.add(new Murder(-600, 0, 64, 750, 3, 0));
                        break;
                    case "5":
                        enemies.add(new Crusher(x, y, 64, 192, 0, 0));
                        break;

                    //end level
                    case ";":
                        levelEnd = new ChargingStation(x, y - 21, 64, 85);
                        break;
                }
                x += 64;

            }
            y += 64;
        }
    }

    protected String[][] getBlocks(String file) throws FileNotFoundException {
        Scanner chop = new Scanner(new File(file));
        String[][] chars = new String[12][];
        for (int i = 0; i < 12; i++) {
            chars[i] = chop.nextLine().split(" ");
        }
        return chars;
    }

    private int getWidth(String[] arr, String s, int i) {
        if (i >= arr.length || !arr[i].equals(s)) {
            return 0;
        }
        arr[i] = "/";
        return 1 + getWidth(arr, s, i + 1);
    }
}
