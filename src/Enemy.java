import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D;

public class Enemy {

    Ellipse2D hitbox;
    private Image image;
    private int xPos;
    private int yPos;
    private int ySpeed = 0;
    private final int width;
    private final int height;
    private int moveSeq = 0;
    private int moveSeqSleep = 0;
    private SpriteSheet sprites;
    private Control control;
    Point check1 = new Point();
    Point check2 = new Point();
    Point check3 = new Point();
    Point check4 = new Point();
    Point check5 = new Point();
    Point check6 = new Point();



    public Enemy(int pXPos, int pYPos, int pWidth, int pHeight, SpriteSheet pSpriteSheet, Control pControl) {
        sprites = pSpriteSheet;
        image = sprites.getSpriteElement(0, 1);
        xPos = pXPos;
        yPos = pYPos;
        width = pWidth;
        height = pHeight;
        control = pControl;
        hitbox = new Ellipse2D.Double(xPos+20, yPos+16, 24, 48);
    }

    public void paintMe(Graphics g){
        g.fillOval(xPos-control.camera.getXOffset()+20, yPos-control.camera.getYOffset()+16,  24, 48);
    }
}
