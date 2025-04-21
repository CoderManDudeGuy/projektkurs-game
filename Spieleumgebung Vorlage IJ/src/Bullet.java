import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Bullet extends Ellipse2D {

    private int xPos;
    private int yPos;
    private int height;
    private int width;
    private int spd;
    private int dmg;

    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isEmpty() {
        return false;
    }

    public void setFrame(double x, double y, double w, double h) {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
    }

    public Rectangle2D getBounds2D() {
        return null;
    }

    public void paintMe(Graphics g){
        g.fillOval(xPos,yPos,width,height);
        xPos += spd;
    }

    public Bullet(int pX, int pY, int pW, int pH, int pSpd, int pDmg){
        xPos = pX;
        yPos = pY;
        width = pW;
        height = pH;
        spd = pSpd;
        dmg = pDmg;
    }
}
