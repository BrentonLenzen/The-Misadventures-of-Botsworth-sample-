//Brenton Lenzen
//THE MISADVENTURES OF BOTSWORTH

//abstract class for anything that moves on the screen
public abstract class Moveable 
{
    private int xPos;
    private int yPos;
    protected int width;
    protected int height;
    
    public Moveable()
    {
        setPos(0,0);
        setDimensions(10,10);
    }
    
    public Moveable(int x, int y, int w, int h)
    {
        setPos(x,y);
        setDimensions(w,h);
    }
    
    public void setPos(int x, int y)
    {
        setX(x);
        setY(y);
    }
    
    public void setDimensions(int w, int h)
    {
        width = w;
        height = h;
    }
    
    public void setX(int x)
    {
        xPos = x;
    }
    
    public void setY(int y)
    {
        yPos = y;
    }
    
    public int getX()
    {
        return xPos;
    }
    
    public int getY()
    {
        return yPos;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public abstract void move(String direction);
    
}
