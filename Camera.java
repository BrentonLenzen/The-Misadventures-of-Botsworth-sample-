
import java.awt.Rectangle;

//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH
public class Camera extends Moveable implements Hitbox {

    private int speed;
    private int initSpeed;
    
    public Camera(int x, int y, int w, int h, int s) {
        super(x, y, w, h);
        speed = s;
        initSpeed = s;
    }
    
    //reset protocols
    public void kill(){
        speed = 0;
    }
    
    public void revive(){
        setPos(-350,0);
        speed = initSpeed;
    }
    
    @Override
    public void move(String direction) {
        switch (direction) {
            case "LEFT":
                setPos(getX() - speed, getY());
                break;
            case "RIGHT":
                setPos(getX() + speed, getY());
                break;
        }
    }

    @Override
    public int getLeftEdge() {
        return getX() - speed - 5;
    }

    @Override
    public int getRightEdge() {
        return getX() + getWidth() + speed + 5;
    }

    @Override
    public int getTopEdge() {
        return getY() - 10;
    }

    @Override
    public int getBotEdge() {
        return getY() + getHeight() + 10;
    }

    @Override
    public boolean collision(Hitbox other) {
        return getRect().intersects(other.getRect());
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getX()-speed-5,getY()-10,getWidth()+(2*speed)+5,getHeight()+20);
    }

}
