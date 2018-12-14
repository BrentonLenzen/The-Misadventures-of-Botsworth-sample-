//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH

import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

//The building blocks that make up the levels
public abstract class Block implements Hitbox {

    //Different image tiles - C=Center, L=Left, R=Right
    Image image;
    //Dimensions should be in multiples of 64
    private int xPos, yPos, width, height, dim;

    public Block(int x, int y, int w, int h, int d) {
        setPos(x, y);
        setDimensions(w, h, d);
        setTiles();
    }

    public void setPos(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public void setDimensions(int w, int h, int d) {
        width = w;
        height = h;
        dim = d;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public abstract void setTiles();

    public void draw(Graphics window, Camera cam) {
        for (int r = getY(); r < getY() + getHeight() - 1; r += getDim()) {
            for (int c = getX(); c < getX() + getWidth() - 1; c += getDim()) {
                if (c + getDim() > cam.getLeftEdge() && c < cam.getRightEdge()) {
                    window.drawImage(image, c - cam.getX(), r, getDim(), getDim(), null);
                }
            }
        }
    }
    
    public void tick(){
        
    }
}
